package com.junyou.controller;

import com.junyou.model.ApiResponse;
import com.junyou.model.EwtReleaseBizNo;
import com.junyou.model.request.StoreEwtReleaseBizNoRequest;
import com.junyou.service.JunyouSdkService;
import com.junyou.storage.StorageService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/ewt")
@CrossOrigin(origins = "*")
public class EwtController {
    
    @Autowired
    private StorageService storageService;
    
    @Autowired
    private JunyouSdkService sdkService;
    
    /**
     * 存储权证释放业务编号并确认释放
     * POST /api/ewt/release-biz-no
     */
    @PostMapping("/release-biz-no")
    public ResponseEntity<ApiResponse<Map<String, Object>>> storeEwtReleaseBizNo(
            @Valid @RequestBody StoreEwtReleaseBizNoRequest request) {
        try {
            // 检查订单是否存在
            if (storageService.getOrderByNo(request.getOrderNo()) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "订单不存在"));
            }
            
            // 检查是否已经存储过
            if (storageService.getEwtReleaseBizNoByOrderNo(request.getOrderNo()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(409, "该订单的业务编号已存在"));
            }
            
            // 调用 SDK 确认权证释放
            Map<String, Object> sdkResult = sdkService.confirmEWTReleaseByPartner(request.getEwtBizNo());
            
            if (!Boolean.TRUE.equals(sdkResult.get("success"))) {
                String message = (String) sdkResult.getOrDefault("message", "权证释放确认失败");
                log.error("❌ 权证释放确认失败: {}", message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(400, "权证释放确认失败: " + message));
            }
            
            log.info("✅ 权证释放确认成功: 业务编号 {}", request.getEwtBizNo());
            
            // 保存业务编号到本地存储
            EwtReleaseBizNo ewtBizNo = new EwtReleaseBizNo();
            ewtBizNo.setEwtBizNo(request.getEwtBizNo());
            ewtBizNo.setOrderNo(request.getOrderNo());
            ewtBizNo.setCreateAt(LocalDateTime.now());
            
            storageService.saveEwtReleaseBizNo(ewtBizNo);
            
            log.info("✅ 存储业务编号成功: 订单号 {}, 业务编号 {}", request.getOrderNo(), request.getEwtBizNo());
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "权证释放确认成功，业务编号已保存");
            result.put("ewt_biz_no", request.getEwtBizNo());
            result.put("order_no", request.getOrderNo());
            result.put("sdk_result", sdkResult.get("data"));
            
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            log.error("❌ 存储业务编号失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "存储业务编号失败: " + e.getMessage()));
        }
    }
}

