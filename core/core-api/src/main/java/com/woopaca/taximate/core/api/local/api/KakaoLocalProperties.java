package com.woopaca.taximate.core.api.local.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "kakao.local")
public class KakaoLocalProperties {

    private String coordinateToAddressUrl;
    private String apiKey;
}
