package com.woopaca.taximate.core.domain.notification;

import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.PushTokenEntity;
import com.woopaca.taximate.storage.db.core.repository.PushTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PushTokenAppender {

    private final PushTokenRepository pushTokenRepository;

    public PushTokenAppender(PushTokenRepository pushTokenRepository) {
        this.pushTokenRepository = pushTokenRepository;
    }

    public void appendToken(String token) {
        if (!StringUtils.hasText(token)) {
            return;
        }

        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        PushTokenEntity pushTokenEntity = PushTokenEntity.builder()
                .userId(authenticatedUser.getId())
                .token(token.trim())
                .build();
        pushTokenRepository.save(pushTokenEntity);
    }
}
