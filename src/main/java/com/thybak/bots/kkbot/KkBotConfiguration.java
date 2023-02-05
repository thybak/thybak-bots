package com.thybak.bots.kkbot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="kkbot")
@Getter
@Setter
public class KkBotConfiguration {
    private String username;
    private String token;
}
