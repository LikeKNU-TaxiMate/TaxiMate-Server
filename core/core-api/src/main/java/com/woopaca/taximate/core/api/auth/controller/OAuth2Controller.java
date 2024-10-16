package com.woopaca.taximate.core.api.auth.controller;

import com.woopaca.taximate.core.api.auth.dto.response.TokensResponse;
import com.woopaca.taximate.core.api.auth.utils.RefreshTokenCookies;
import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.api.common.model.ApiResults.ApiResponse;
import com.woopaca.taximate.core.domain.auth.OAuth2User;
import com.woopaca.taximate.core.domain.auth.model.Tokens;
import com.woopaca.taximate.core.domain.auth.service.AuthService;
import com.woopaca.taximate.core.domain.auth.service.KakaoOAuth2Service;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.core.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/oauth2")
@RestController
public class OAuth2Controller {

    private final KakaoOAuth2Service kakaoOAuth2Service;
    private final UserService userService;
    private final AuthService authService;

    public OAuth2Controller(KakaoOAuth2Service kakaoOAuth2Service, UserService userService, AuthService authService) {
        this.kakaoOAuth2Service = kakaoOAuth2Service;
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/kakao")
    public ApiResponse<TokensResponse> kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) {
        OAuth2User kakaoUser = kakaoOAuth2Service.authenticate(code);
        User user = userService.registerOAuth2User(kakaoUser);
        Tokens tokens = authService.issueTokensFor(user);

        Cookie refreshTokenCookie = RefreshTokenCookies.generateCookie(tokens.refreshToken());
        response.addCookie(refreshTokenCookie);

        return ApiResults.success(new TokensResponse(tokens.accessToken()));
    }
}
