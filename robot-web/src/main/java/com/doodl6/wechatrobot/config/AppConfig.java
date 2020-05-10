package com.doodl6.wechatrobot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 应用配置类
 */
@Configuration
@Getter
@Setter
@ConfigurationProperties("app")
public class AppConfig {

    /**
     * 令牌
     */
    public String token;

    /**
     * 图灵机器人应用key
     */
    public String apiKey;
}
