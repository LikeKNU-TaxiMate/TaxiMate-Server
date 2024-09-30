package com.woopaca.taximate.core.api.auth.controller;

import com.woopaca.taximate.core.api.auth.dto.response.TokensResponse;
import com.woopaca.taximate.core.api.auth.utils.RefreshTokenCookies;
import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.api.common.model.ApiResults.ApiResponse;
import com.woopaca.taximate.core.domain.auth.model.Tokens;
import com.woopaca.taximate.core.domain.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/tokens")
    public ApiResponse<TokensResponse> reissueTokens(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
        Tokens tokens = authService.reissueTokensBy(refreshToken);

        Cookie refreshTokenCookie = RefreshTokenCookies.generateCookie(tokens.refreshToken());
        response.addCookie(refreshTokenCookie);

        return ApiResults.success(new TokensResponse(tokens.accessToken()));
    }
}
