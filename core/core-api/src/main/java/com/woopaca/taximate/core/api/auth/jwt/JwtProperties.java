package com.woopaca.taximate.core.api.auth.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String issuer;
    private String clientSecret;
    private long accessTokenExpiry;
    private long refreshTokenExpiry;
}
