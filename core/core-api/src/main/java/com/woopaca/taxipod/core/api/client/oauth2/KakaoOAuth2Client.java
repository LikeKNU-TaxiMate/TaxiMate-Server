package com.woopaca.taxipod.core.api.client.oauth2;

import com.woopaca.taxipod.core.api.config.KakaoOAuth2Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class KakaoOAuth2Client implements OAuth2Client {

    private final RestClient restClient;
    private final KakaoOAuth2Properties kakaoOAuth2Properties;

    public KakaoOAuth2Client(RestClient restClient, KakaoOAuth2Properties kakaoOAuth2Properties) {
        this.restClient = restClient;
        this.kakaoOAuth2Properties = kakaoOAuth2Properties;
    }

    @Override
    public OAuth2Token requestToken(String authorizationCode) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", kakaoOAuth2Properties.getClientId());
        requestBody.add("redirect_uri", kakaoOAuth2Properties.getRedirectUri());
        requestBody.add("code", authorizationCode);

        try {
            return restClient.post()
                    .uri(kakaoOAuth2Properties.getTokenUrl())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(requestBody)
                    .retrieve()
                    .body(OAuth2Token.class);
        } catch (HttpStatusCodeException exception) {
            log.warn("카카오 OAuth 2.0 토큰 요청 오류", exception);
            //TODO Handle kakao token request failure
        }
        return null;
    }

    @Override
    public KakaoUser requestUserInfo(String accessToken) {
        try {
            return restClient.get()
                    .uri(kakaoOAuth2Properties.getUserUrl())
                    .headers(headers -> headers.setBearerAuth(accessToken))
                    .retrieve()
                    .body(KakaoUser.class);
        } catch (HttpStatusCodeException exception) {
            log.warn("카카오 OAuth 2.0 사용자 정보 요청 오류", exception);
            //TODO Handle kakao user request failure
        }
        return null;
    }

}
