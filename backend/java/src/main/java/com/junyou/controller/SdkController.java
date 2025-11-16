package com.junyou.controller;

import com.junyou.model.ApiResponse;
import com.junyou.model.request.SignatureRequest;
import com.junyou.service.JunyouSdkService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SdkController {
    
    @Autowired
    private JunyouSdkService sdkService;
    
    /**
     * 获取 SDK 签名（带 open_id 和 token）
     * POST /api/signature-with-open-auth
     */
    @PostMapping("/signature-with-open-auth")
    public ResponseEntity<ApiResponse<Map<String, String>>> getSDKSignatureWithOpenAuth(
            @Valid @RequestBody SignatureRequest request) {
        try {
            Map<String, String> signature = sdkService.generateSignatureWithOpenAuth(
                    request.getMethod(), request.getPath());
            
            return ResponseEntity.ok(ApiResponse.success(signature));
        } catch (Exception e) {
            log.error("❌ 生成签名失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "生成签名失败: " + e.getMessage()));
        }
    }
}

