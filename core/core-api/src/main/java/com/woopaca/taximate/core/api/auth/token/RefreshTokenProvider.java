package com.woopaca.taximate.core.api.auth.token;

import com.woopaca.taximate.core.api.common.error.exception.InvalidRefreshTokenException;
import com.woopaca.taximate.core.api.user.domain.User;
import com.woopaca.taximate.storage.db.nosql.repository.KeyValueRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.UUID;

@Component
public class RefreshTokenProvider {

    public static final Duration DEFAULT_VALID_DURATION = Duration.ofDays(14);

    private final KeyValueRepository keyValueRepository;

    public RefreshTokenProvider(KeyValueRepository keyValueRepository) {
        this.keyValueRepository = keyValueRepository;
    }

    public String issueRefreshToken(User principal) {
        String refreshToken = generateRefreshToken();
        String key = generateKey(refreshToken);
        keyValueRepository.set(key, principal.getEmail(), DEFAULT_VALID_DURATION);
        return refreshToken;
    }

    /**
     * 주제를 꺼내고 refresh token 제거
     * @param refreshToken 유효한 refresh token
     * @return
     */
    public String takeOutSubject(String refreshToken) {
        String key = generateKey(refreshToken);
        String email = keyValueRepository.get(key);
        if (!StringUtils.hasText(email)) {
            throw new InvalidRefreshTokenException();
        }
        keyValueRepository.remove(key);
        return email;
    }

    private String generateRefreshToken() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-", "");
    }

    private String generateKey(String refreshToken) {
        return String.join(":", "refresh_token", refreshToken);
    }
}
