package com.woopaca.taximate.core.api.auth.service;

import com.woopaca.taximate.core.api.auth.model.Tokens;
import com.woopaca.taximate.core.api.auth.token.JwtProvider;
import com.woopaca.taximate.core.api.auth.token.RefreshTokenProvider;
import com.woopaca.taximate.core.api.common.error.exception.InvalidRefreshTokenException;
import com.woopaca.taximate.core.api.user.domain.User;
import com.woopaca.taximate.core.api.user.domain.UserFinder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserFinder userFinder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    public AuthService(UserFinder userFinder, JwtProvider jwtProvider, RefreshTokenProvider refreshTokenProvider) {
        this.userFinder = userFinder;
        this.jwtProvider = jwtProvider;
        this.refreshTokenProvider = refreshTokenProvider;
    }

    public Tokens issueTokensFor(User user) {
        refreshTokenProvider.expireRefreshToken(user);
        String accessToken = jwtProvider.issueAccessToken(user);
        String refreshToken = refreshTokenProvider.issueRefreshToken(user);
        return new Tokens(accessToken, refreshToken);
    }

    public Tokens reissueTokensBy(String refreshToken) {
        String subject = refreshTokenProvider.takeOutSubject(refreshToken);
        User principal = userFinder.findUserByEmail(subject)
                .orElseThrow(InvalidRefreshTokenException::new);
        String reissuedAccessToken = jwtProvider.issueAccessToken(principal);
        String reissuedRefreshToken = refreshTokenProvider.issueRefreshToken(principal);
        return new Tokens(reissuedAccessToken, reissuedRefreshToken);
    }
}
