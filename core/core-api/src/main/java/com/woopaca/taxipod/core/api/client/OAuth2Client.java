package com.woopaca.taxipod.core.api.client;

import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpServerErrorException;

public interface OAuth2Client {

    @Retryable(retryFor = HttpServerErrorException.class, maxAttempts = 2)
    OAuth2Tokens requestToken(String authorizationCode);

}
