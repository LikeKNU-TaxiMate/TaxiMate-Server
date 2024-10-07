package com.woopaca.taximate.core.api.auth.oauth2;

import com.woopaca.taximate.core.domain.auth.OAuth2Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Component
public class KakaoOAuth2Client implements OAuth2Client {

    private final RestClient restClient;
    private final KakaoOAuth2Properties kakaoOAuth2Properties;

    public KakaoOAuth2Client(RestClient restClient, KakaoOAuth2Properties kakaoOAuth2Properties) {
        this.restClient = restClient;
        this.kakaoOAuth2Properties = kakaoOAuth2Properties;
    }

    /**
     * 카카오 OAuth 2.0 토큰 요청
     * @param authorizationCode 인가 코드
     * @return OAuth 2.0 토큰
     * @see <a href="https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api">카카오 로그인 REST API</a>
     */
    @Override
    public KakaoToken requestToken(String authorizationCode) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", kakaoOAuth2Properties.getApiKey());
        requestBody.add("redirect_uri", kakaoOAuth2Properties.getRedirectUri());
        requestBody.add("code", authorizationCode);

        try {
            return restClient.post()
                    .uri(kakaoOAuth2Properties.getTokenUrl())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(requestBody)
                    .retrieve()
                    .body(KakaoToken.class);
        } catch (HttpStatusCodeException exception) {
            log.warn("카카오 OAuth 2.0 토큰 요청 오류", exception);
            //TODO Handle kakao token request failure
            throw exception;
        }
    }

    @Override
    public KakaoUser requestUserInfo(String accessToken) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(kakaoOAuth2Properties.getUserUrl())
                    .queryParam("secure_resource", true)
                    .build()
                    .toUri();
            return restClient.post()
                    .uri(uri)
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
