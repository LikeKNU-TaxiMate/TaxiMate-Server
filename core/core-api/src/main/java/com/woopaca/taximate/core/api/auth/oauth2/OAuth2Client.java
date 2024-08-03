package com.woopaca.taximate.core.api.auth.oauth2;

public interface OAuth2Client {

    //TODO Retry handling for 5xx responses

    //    @Retryable(retryFor = HttpServerErrorException.class, maxAttempts = 2)
    OAuth2Token requestToken(String authorizationCode);

    //    @Retryable(retryFor = HttpServerErrorException.class, maxAttempts = 2)
    OAuth2User requestUserInfo(String accessToken);
}
