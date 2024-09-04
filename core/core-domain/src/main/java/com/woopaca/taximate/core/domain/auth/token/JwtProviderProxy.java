package com.woopaca.taximate.core.domain.auth.token;

import com.woopaca.taximate.core.domain.user.User;

public interface JwtProviderProxy {

    String issueAccessToken(User principal);

    String verify(String accessToken);
}
