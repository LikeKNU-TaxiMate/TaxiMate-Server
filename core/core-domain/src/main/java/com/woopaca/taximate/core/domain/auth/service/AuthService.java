package com.woopaca.taximate.core.domain.auth.service;

import com.woopaca.taximate.core.domain.auth.model.Tokens;
import com.woopaca.taximate.core.domain.auth.token.JwtProviderProxy;
import com.woopaca.taximate.core.domain.auth.token.RefreshTokenProvider;
import com.woopaca.taximate.core.domain.error.exception.InvalidRefreshTokenException;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.core.domain.user.UserFinder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserFinder userFinder;
    private final JwtProviderProxy jwtProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    public AuthService(UserFinder userFinder, JwtProviderProxy jwtProvider, RefreshTokenProvider refreshTokenProvider) {
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
