package com.junyou.config;

import com.junyouava.sdk.Client;
import com.junyouava.sdk.Config;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "junyou")
public class JunyouConfig {
    private String accessId;
    private String accessKey;
    private String openId;
    private String apiAddress = "https://open-api.junyouchain.com";

    /**
     * 初始化 JunYou SDK 客户端
     */
    @Bean
    public Client junyouClient() {
        // 如果环境变量未设置，使用默认值（仅用于开发测试）
        String accessIdValue = accessId != null && !accessId.isEmpty() 
            ? accessId 
            : "";
        String accessKeyValue = accessKey != null && !accessKey.isEmpty() 
            ? accessKey 
            : "";
        String openIdValue = openId != null && !openId.isEmpty() 
            ? openId 
            : "";

        if (accessId == null || accessId.isEmpty()) {
            log.warn("⚠️  警告: 使用默认 AccessId，请设置环境变量 JUNYOU_ACCESS_ID");
        }
        if (accessKey == null || accessKey.isEmpty()) {
            log.warn("⚠️  警告: 使用默认 AccessKey，请设置环境变量 JUNYOU_ACCESS_KEY");
        }
        if (openId == null || openId.isEmpty()) {
            log.warn("⚠️  警告: 使用默认 OpenId，请设置环境变量 JUNYOU_OPEN_ID");
        }

        Config config = Config.DefaultConfig()
                .WithAccessId(accessIdValue)
                .WithAccessKey(accessKeyValue)
                .WithAddress(apiAddress);

        Client client = Client.NewClient(config);
        log.info("✅ JunYou SDK 客户端初始化成功");
        return client;
    }
}

