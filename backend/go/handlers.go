package main

import (
	"fmt"
	"log"
	"net/http"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
	junyousdk "github.com/junyouava/junyou-sdk-go"
)

// validateAmount 验证金额格式
func validateAmount(amount string) error {
	if amount == "" {
		return fmt.Errorf("金额不能为空")
	}
	// 简单的金额格式验证：必须是数字，可以有小数点
	// 这里可以添加更严格的验证，如最大值、最小值等
	if len(amount) > 20 {
		return fmt.Errorf("金额长度不能超过20位")
	}
	return nil
}

// createOrder 创建订单
// POST /api/order
func createOrder(c *gin.Context) {
	var req CreateOrderRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, ApiResponse{
			Code:    400,
			Message: "参数错误: " + err.Error(),
		})
		return
	}

	// 验证金额格式
	if err := validateAmount(req.Amount); err != nil {
		c.JSON(http.StatusBadRequest, ApiResponse{
			Code:    400,
			Message: "金额格式错误: " + err.Error(),
		})
		return
	}

	// 生成订单号
	orderNo := "ORDER" + time.Now().Format("20060102150405") + uuid.New().String()[:8]

	order := &Order{
		OrderNo:  orderNo,
		Amount:   req.Amount,
		Status:   "paid", // 假设订单已支付
		CreateAt: time.Now(),
	}

	// 保存订单
	saveOrder(order)

	log.Printf("✅ 创建订单成功: %s, 金额: %s", orderNo, req.Amount)

	c.JSON(http.StatusOK, ApiResponse{
		Code: 200,
		Data: order,
	})
}

// getReleaseMessage 获取释放权证数据
// GET /api/release-message/:order_no
func getReleaseMessage(c *gin.Context) {
	orderNo := c.Param("order_no")

	// 先检查订单是否存在
	order, exists := getOrderByNo(orderNo)
	if !exists {
		c.JSON(http.StatusNotFound, ApiResponse{
			Code:    404,
			Message: "订单不存在",
		})
		return
	}

	// 根据订单信息生成释放权证数据
	// 这里可以根据实际业务逻辑计算比例
	releaseMessage := &ReleaseMessage{
		Amount:       order.Amount,
		Ratio:        "0.1",  // 用户奖励比例 10%
		Level1Ratio:  "0.15", // 一度用户比例 15%
		Level2Ratio:  "0.1",  // 二度用户比例 10%
		Level1OpenID: "level1_open_id_" + orderNo[:8],
		Level2OpenID: "level2_open_id_" + orderNo[:8],
	}

	log.Printf("✅ 获取释放权证数据成功: 订单号 %s", orderNo)

	c.JSON(http.StatusOK, ApiResponse{
		Code: 200,
		Data: releaseMessage,
	})
}

// storeEwtReleaseBizNo 存储权证释放业务编号并确认释放
// POST /api/ewt/release-biz-no
func storeEwtReleaseBizNo(c *gin.Context) {
	var req StoreEwtReleaseBizNoRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, ApiResponse{
			Code:    400,
			Message: "参数错误: " + err.Error(),
		})
		return
	}

	// 检查订单是否存在
	_, exists := getOrderByNo(req.OrderNo)
	if !exists {
		c.JSON(http.StatusNotFound, ApiResponse{
			Code:    404,
			Message: "订单不存在",
		})
		return
	}

	// 检查是否已经存储过
	_, alreadyExists := getEwtReleaseBizNoByOrderNo(req.OrderNo)
	if alreadyExists {
		c.JSON(http.StatusConflict, ApiResponse{
			Code:    409,
			Message: "该订单的业务编号已存在",
		})
		return
	}

	// 使用 JunYou SDK 确认权证释放
	client := GetJunyouClient()
	if client == nil {
		c.JSON(http.StatusInternalServerError, ApiResponse{
			Code:    500,
			Message: "SDK 客户端未初始化",
		})
		return
	}

	// 调用 SDK 确认权证释放
	ewtBizNoInfo := junyousdk.EWTBizNoInfo{
		EWTBizNo: req.EwtBizNo,
	}

	log.Printf("📞 调用 JunYou SDK 确认权证释放: 业务编号 %s", req.EwtBizNo)
	result, err := client.API().ConfirmEWTReleaseByPartner(ewtBizNoInfo)
	if err != nil {
		log.Printf("❌ SDK 调用失败: %v", err)
		c.JSON(http.StatusInternalServerError, ApiResponse{
			Code:    500,
			Message: fmt.Sprintf("SDK 调用失败: %v", err),
		})
		return
	}

	if !result.Success {
		log.Printf("❌ 权证释放确认失败: %s (错误代码: %s, 状态码: %d)",
			result.Message, result.ErrCode, result.Code)
		c.JSON(http.StatusBadRequest, ApiResponse{
			Code:    result.Code,
			Message: fmt.Sprintf("权证释放确认失败: %s", result.Message),
		})
		return
	}

	log.Printf("✅ 权证释放确认成功: 业务编号 %s", req.EwtBizNo)

	// 保存业务编号到本地存储
	ewtBizNo := &EwtReleaseBizNo{
		EwtBizNo: req.EwtBizNo,
		OrderNo:  req.OrderNo,
		CreateAt: time.Now(),
	}

	saveEwtReleaseBizNo(ewtBizNo)

	log.Printf("✅ 存储业务编号成功: 订单号 %s, 业务编号 %s", req.OrderNo, req.EwtBizNo)

	c.JSON(http.StatusOK, ApiResponse{
		Code: 200,
		Data: gin.H{
			"success":    true,
			"message":    "权证释放确认成功，业务编号已保存",
			"ewt_biz_no": req.EwtBizNo,
			"order_no":   req.OrderNo,
			"sdk_result": result.Data,
		},
	})
}

// getSDKSignatureWithOpenAuth 获取 SDK 签名（带 open_id 和 token）
// POST /api/sdk/signature-with-open-auth
func getSDKSignatureWithOpenAuth(c *gin.Context) {
	var req struct {
		Method string `json:"method" binding:"required"`
		Path   string `json:"path" binding:"required"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, ApiResponse{
			Code:    400,
			Message: "参数错误: " + err.Error(),
		})
		return
	}

	// 使用 JunYou SDK 生成签名（带认证 OpenAuth）
	client := GetJunyouClient()
	if client == nil {
		c.JSON(http.StatusInternalServerError, ApiResponse{
			Code:    500,
			Message: "SDK 客户端未初始化",
		})
		return
	}

	// 从配置获取 open_id
	openId := GetJunyouOpenId()
	openIdToken := junyousdk.OpenIdToken{
		OpenId: openId,
	}

	signature, err := client.Auth().GenerateSignatureWithOpenAuth(req.Method, req.Path, openIdToken)
	if err != nil {
		log.Printf("❌ 生成签名（带认证 OpenAuth）失败: %v", err)
		c.JSON(http.StatusInternalServerError, ApiResponse{
			Code:    500,
			Message: fmt.Sprintf("生成签名失败: %v", err),
		})
		return
	}

	log.Printf("✅ 生成签名（带认证 OpenAuth）成功: open_id=%s", openId)

	c.JSON(http.StatusOK, ApiResponse{
		Code: 200,
		Data: gin.H{
			"access_id": signature.AccessId,
			"signature": signature.Signature,
			"nonce":     signature.Nonce,
			"timestamp": signature.Timestamp,
			"open_auth": signature.OpenAuth,
		},
	})
}
