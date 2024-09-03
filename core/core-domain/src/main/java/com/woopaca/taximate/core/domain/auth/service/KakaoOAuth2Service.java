package com.woopaca.taximate.core.domain.auth.service;

import com.woopaca.taximate.core.domain.auth.OAuth2Client;
import com.woopaca.taximate.core.domain.auth.OAuth2Token;
import com.woopaca.taximate.core.domain.auth.OAuth2User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class KakaoOAuth2Service {

    private final OAuth2Client oAuth2Client;

    public KakaoOAuth2Service(@Qualifier("kakaoOAuth2Client") OAuth2Client oAuth2Client) {
        this.oAuth2Client = oAuth2Client;
    }

    public OAuth2User authenticate(String code) {
        OAuth2Token kakaoToken = oAuth2Client.requestToken(code);
        return oAuth2Client.requestUserInfo(kakaoToken.accessToken());
    }
}
