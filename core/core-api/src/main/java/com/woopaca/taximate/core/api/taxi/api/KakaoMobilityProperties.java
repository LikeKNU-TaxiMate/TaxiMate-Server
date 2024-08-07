package com.woopaca.taximate.core.api.taxi.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "kakao.mobility")
public class KakaoMobilityProperties {

    private String directionsUrl;
    private String apiKey;
}
