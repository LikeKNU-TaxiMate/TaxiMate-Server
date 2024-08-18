package com.woopaca.taximate.core.api.auth.service;

import com.woopaca.taximate.core.api.auth.oauth2.KakaoOAuth2Client;
import com.woopaca.taximate.core.api.auth.oauth2.KakaoUser;
import com.woopaca.taximate.core.api.auth.oauth2.OAuth2Token;
import org.springframework.stereotype.Service;

@Service
public class KakaoOAuth2Service {

    private final KakaoOAuth2Client kakaoOAuth2Client;

    public KakaoOAuth2Service(KakaoOAuth2Client kakaoOAuth2Client) {
        this.kakaoOAuth2Client = kakaoOAuth2Client;
    }

    public KakaoUser authenticate(String code) {
        OAuth2Token oAuth2Token = kakaoOAuth2Client.requestToken(code);
        return kakaoOAuth2Client.requestUserInfo(oAuth2Token.accessToken());
    }
}
