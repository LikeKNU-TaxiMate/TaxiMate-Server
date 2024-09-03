package com.woopaca.taximate.core.api.auth.oauth2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.woopaca.taximate.core.domain.auth.OAuth2Token;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoToken(String accessToken, String refreshToken) implements OAuth2Token {
}
