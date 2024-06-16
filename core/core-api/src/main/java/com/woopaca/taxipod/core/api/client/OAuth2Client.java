package com.woopaca.taxipod.core.api.client;

/**
 * @param <T> User account type
 */
public interface OAuth2Client<T> {

    //TODO Retry handling for 5xx responses

    //    @Retryable(retryFor = HttpServerErrorException.class, maxAttempts = 2)
    OAuth2Tokens requestToken(String authorizationCode);

    //    @Retryable(retryFor = HttpServerErrorException.class, maxAttempts = 2)
    T requestUserInfo(String accessToken);

}
