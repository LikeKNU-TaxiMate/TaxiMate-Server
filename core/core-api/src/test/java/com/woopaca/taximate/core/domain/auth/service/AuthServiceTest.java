package com.woopaca.taximate.core.domain.auth.service;

import com.woopaca.taximate.core.domain.auth.model.Tokens;
import com.woopaca.taximate.core.domain.auth.token.JwtProviderProxy;
import com.woopaca.taximate.core.domain.auth.token.RefreshTokenProvider;
import com.woopaca.taximate.core.domain.error.exception.InvalidRefreshTokenException;
import com.woopaca.taximate.core.domain.fixture.UserFixtures;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.core.domain.user.UserRegistrant;
import com.woopaca.taximate.storage.db.nosql.repository.KeyValueRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class AuthServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private KeyValueRepository keyValueRepository;
    @Autowired
    private JwtProviderProxy jwtProvider;
    @Autowired
    private RefreshTokenProvider refreshTokenProvider;
    @Autowired
    private UserRegistrant userRegistrant;

    @Nested
    class issueTokensFor_메서드는 {

        @Test
        void 사용자의_토큰을_발급한다() {
            // given
            User user = UserFixtures.createUser(1L);

            // when
            Tokens tokens = authService.issueTokensFor(user);

            // then
            assertThat(tokens).isNotNull();
            assertThat(tokens.accessToken()).isNotBlank();
            assertThat(tokens.refreshToken()).isNotBlank();
            assertThat(jwtProvider.verify(tokens.accessToken())).isEqualTo(user.getEmail());
            String refreshTokenKey = String.join(":", "refresh_token", tokens.refreshToken());
            assertThat(keyValueRepository.get(refreshTokenKey)).isEqualTo(user.getEmail());
        }

        @Test
        void 기존_리프레시_토큰을_만료한다() {
            // given
            User user = UserFixtures.createUser(1L);
            String oldRefreshToken = refreshTokenProvider.issueRefreshToken(user);

            // when
            String newRefreshToken = authService.issueTokensFor(user)
                    .refreshToken();

            // then
            String oldRefreshTokenKey = String.join(":", "refresh_token", oldRefreshToken);
            String newRefreshTokenKey = String.join(":", "refresh_token", newRefreshToken);
            assertThat(keyValueRepository.get(oldRefreshTokenKey)).isNull();
            assertThat(keyValueRepository.get(newRefreshTokenKey)).isEqualTo(user.getEmail());
        }
    }

    @Nested
    class reissueTokensBy_메서드는 {

        @Test
        void 리프레시_토큰으로_토큰을_재발급한다() {
            // given
            User user = UserFixtures.createUser(1L);
            userRegistrant.register(user);
            String refreshToken = refreshTokenProvider.issueRefreshToken(user);

            // when
            Tokens reissuedTokens = authService.reissueTokensBy(refreshToken);

            // then
            assertThat(reissuedTokens).isNotNull();
            assertThat(reissuedTokens.accessToken()).isNotBlank();
            assertThat(reissuedTokens.refreshToken()).isNotBlank();
            assertThat(jwtProvider.verify(reissuedTokens.accessToken())).isEqualTo(user.getEmail());
            String refreshTokenKey = String.join(":", "refresh_token", reissuedTokens.refreshToken());
            assertThat(keyValueRepository.get(refreshTokenKey)).isEqualTo(user.getEmail());
        }

        @Test
        void 리프레시_토큰으로_토큰을_재발급하면_기존_리프레시_토큰은_만료된다() {
            // given
            User user = UserFixtures.createUser(1L);
            userRegistrant.register(user);
            String oldRefreshToken = refreshTokenProvider.issueRefreshToken(user);

            // when
            String newRefreshToken = authService.reissueTokensBy(oldRefreshToken)
                    .refreshToken();

            // then
            String oldRefreshTokenKey = String.join(":", "refresh_token", oldRefreshToken);
            String newRefreshTokenKey = String.join(":", "refresh_token", newRefreshToken);
            assertThat(keyValueRepository.get(oldRefreshTokenKey)).isNull();
            assertThat(keyValueRepository.get(newRefreshTokenKey)).isEqualTo(user.getEmail());
        }

        @Test
        void 리프레시_토큰이_유효하지_않으면_예외가_발생한다() {
            // given
            String invalidRefreshToken = "invalid_refresh_token";

            // when & then
            assertThatThrownBy(() -> authService.reissueTokensBy(invalidRefreshToken))
                    .isInstanceOf(InvalidRefreshTokenException.class);
        }
    }
}