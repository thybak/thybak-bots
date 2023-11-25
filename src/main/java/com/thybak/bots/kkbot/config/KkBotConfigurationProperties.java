package com.thybak.bots.kkbot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="kkbot")
@Getter
@Setter
public class KkBotConfigurationProperties {
    private String username;
    private String token;
    private Long groupChatId;
}
