package com.woopaca.taxipod.core.api.client.oauth2;

/**
 * @param <T> User account type
 */
public interface OAuth2Client {

    //TODO Retry handling for 5xx responses

    //    @Retryable(retryFor = HttpServerErrorException.class, maxAttempts = 2)
    OAuth2Token requestToken(String authorizationCode);

    //    @Retryable(retryFor = HttpServerErrorException.class, maxAttempts = 2)
    OAuth2User requestUserInfo(String accessToken);

}
