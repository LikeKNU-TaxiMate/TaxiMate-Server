package com.woopaca.taximate.core.domain.auth;

public interface OAuth2Token {

    String accessToken();

    String refreshToken();
}
