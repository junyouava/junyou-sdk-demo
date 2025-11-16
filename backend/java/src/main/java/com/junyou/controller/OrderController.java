package com.junyou.controller;

import com.junyou.model.ApiResponse;
import com.junyou.model.Order;
import com.junyou.model.ReleaseMessage;
import com.junyou.model.request.CreateOrderRequest;
import com.junyou.service.JunyouSdkService;
import com.junyou.storage.StorageService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OrderController {
    
    @Autowired
    private StorageService storageService;
    
    @Autowired
    private JunyouSdkService sdkService;
    
    /**
     * 创建订单
     * POST /api/order
     */
    @PostMapping("/order")
    public ResponseEntity<ApiResponse<Order>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            // 生成订单号
            String orderNo = "ORDER" + LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) 
                    + UUID.randomUUID().toString().substring(0, 8);
            
            Order order = new Order();
            order.setOrderNo(orderNo);
            order.setAmount(request.getAmount());
            order.setStatus("paid");
            order.setCreateAt(LocalDateTime.now());
            
            // 保存订单
            storageService.saveOrder(order);
            
            log.info("✅ 创建订单成功: {}, 金额: {}", orderNo, request.getAmount());
            
            return ResponseEntity.ok(ApiResponse.success(order));
        } catch (Exception e) {
            log.error("❌ 创建订单失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "创建订单失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取释放权证数据
     * GET /api/release-message/{order_no}
     */
    @GetMapping("/release-message/{order_no}")
    public ResponseEntity<ApiResponse<ReleaseMessage>> getReleaseMessage(
            @PathVariable("order_no") String orderNo) {
        try {
            // 检查订单是否存在
            Order order = storageService.getOrderByNo(orderNo);
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "订单不存在"));
            }
            
            // 根据订单信息生成释放权证数据
            ReleaseMessage releaseMessage = new ReleaseMessage();
            releaseMessage.setAmount(order.getAmount());
            releaseMessage.setRatio("0.1");  // 用户奖励比例 10%
            releaseMessage.setLevel1Ratio("0.15"); // 一度用户比例 15%
            releaseMessage.setLevel2Ratio("0.1");  // 二度用户比例 10%
            releaseMessage.setLevel1OpenId("level1_open_id_" + orderNo.substring(0, 8));
            releaseMessage.setLevel2OpenId("level2_open_id_" + orderNo.substring(0, 8));
            
            log.info("✅ 获取释放权证数据成功: 订单号 {}", orderNo);
            
            return ResponseEntity.ok(ApiResponse.success(releaseMessage));
        } catch (Exception e) {
            log.error("❌ 获取释放权证数据失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "获取释放权证数据失败: " + e.getMessage()));
        }
    }
}

