package com.woopaca.taximate.core.api.auth.oauth2;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "oauth2.kakao")
public class KakaoOAuth2Properties {

    private String tokenUrl;
    private String userUrl;
    private String redirectUri;
    private String clientId;
}
