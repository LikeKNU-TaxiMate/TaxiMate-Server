package com.woopaca.taximate.core.domain.auth.token;

import com.woopaca.taximate.core.domain.error.exception.InvalidRefreshTokenException;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.nosql.repository.KeyValueRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
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

    /**
     * refresh token 만료 ({@code user}의 refresh token을 만료)
     * @param user {@code null}이 아닌 사용자
     */
    public void expireRefreshToken(User user) {
        String email = user.getEmail();
        keyValueRepository.getAll()
                .entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), email))
                .map(Map.Entry::getKey)
                .forEach(keyValueRepository::remove);
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
