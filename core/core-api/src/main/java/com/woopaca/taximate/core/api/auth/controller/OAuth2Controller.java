package com.woopaca.taximate.core.api.auth.controller;

import com.woopaca.taximate.core.api.auth.oauth2.KakaoOAuth2Client;
import com.woopaca.taximate.core.api.auth.oauth2.KakaoUser;
import com.woopaca.taximate.core.api.auth.oauth2.OAuth2Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/oauth2")
@RestController
public class OAuth2Controller {

    private final KakaoOAuth2Client kakaoOAuth2Client;

    public OAuth2Controller(KakaoOAuth2Client kakaoOAuth2Client) {
        this.kakaoOAuth2Client = kakaoOAuth2Client;
    }

    @GetMapping("/kakao")
    public void kakaoLogin(@RequestParam("code") String code) {
        log.info("code: {}", code);
        OAuth2Token oAuth2Token = kakaoOAuth2Client.requestToken(code);
        log.info("oAuth2Token: {}", oAuth2Token);
        KakaoUser kakaoUser = kakaoOAuth2Client.requestUserInfo(oAuth2Token.accessToken());
        log.info("kakaoUser: {}", kakaoUser);
    }
}
