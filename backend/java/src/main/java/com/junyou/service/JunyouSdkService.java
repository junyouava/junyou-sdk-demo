package com.junyou.service;

import com.junyouava.sdk.Client;
import com.junyouava.sdk.model.EWTBizNoInfo;
import com.junyouava.sdk.model.OpenIdToken;
import com.junyouava.sdk.model.Result;
import com.junyouava.sdk.model.SignatureWithOpenAuth;
import com.junyou.config.JunyouConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JunyouSdkService {
    
    @Autowired
    private Client junyouClient;
    
    @Autowired
    private JunyouConfig config;
    
    /**
     * 生成签名（带 OpenAuth）
     */
    public Map<String, String> generateSignatureWithOpenAuth(String method, String path) {
        try {
            // 从配置获取 open_id
            String openId = config.getOpenId();
            if (openId == null || openId.isEmpty()) {
                log.error("❌ OpenId 未配置");
                throw new RuntimeException("OpenId 未配置，请设置环境变量 JUNYOU_OPEN_ID 或在配置文件中配置");
            }
            
            OpenIdToken openIdToken = new OpenIdToken(openId);
            
            // 调用 SDK 生成签名并获取 OpenAuth
            SignatureWithOpenAuth signature = junyouClient.Auth().GenerateSignatureWithOpenAuth(
                    method, path, openIdToken);
            
            Map<String, String> result = new HashMap<>();
            result.put("access_id", signature.getAccessId());
            result.put("signature", signature.getSignature());
            result.put("nonce", signature.getNonce());
            result.put("timestamp", signature.getTimestamp());
            result.put("open_auth", signature.getOpenAuth());
            
            log.info("✅ 生成签名（带认证 OpenAuth）成功: open_id={}", openId);
            return result;
        } catch (IOException e) {
            log.error("❌ 生成签名失败", e);
            throw new RuntimeException("生成签名失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 确认权证释放
     */
    public Map<String, Object> confirmEWTReleaseByPartner(String ewtBizNo) {
        try {
            log.info("📞 调用 JunYou SDK 确认权证释放: 业务编号 {}", ewtBizNo);
            
            // 调用 SDK 确认权证释放
            EWTBizNoInfo ewtBizNoInfo = new EWTBizNoInfo(ewtBizNo);
            Result<String> result = junyouClient.API().ConfirmEWTReleaseByPartner(ewtBizNoInfo);
            
            Map<String, Object> response = new HashMap<>();
            
            if (!result.isSuccess()) {
                log.error("❌ 权证释放确认失败: {} (错误代码: {}, 状态码: {})",
                        result.getMessage(), result.getErrCode(), result.getCode());
                response.put("success", false);
                response.put("code", result.getCode());
                response.put("errCode", result.getErrCode());
                response.put("message", result.getMessage());
                return response;
            }
            
            log.info("✅ 权证释放确认成功: 业务编号 {}", ewtBizNo);
            response.put("success", true);
            response.put("message", "权证释放确认成功");
            response.put("data", result.getData());
            
            return response;
        } catch (IOException e) {
            log.error("❌ SDK 调用失败", e);
            throw new RuntimeException("SDK 调用失败: " + e.getMessage(), e);
        }
    }
}
