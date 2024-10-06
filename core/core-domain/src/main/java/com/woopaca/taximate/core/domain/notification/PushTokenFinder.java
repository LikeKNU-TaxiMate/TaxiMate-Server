package com.woopaca.taximate.core.domain.notification;

import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.PushTokenEntity;
import com.woopaca.taximate.storage.db.core.repository.PushTokenRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PushTokenFinder {

    private final PushTokenRepository pushTokenRepository;

    public PushTokenFinder(PushTokenRepository pushTokenRepository) {
        this.pushTokenRepository = pushTokenRepository;
    }

    public List<String> findTokens(List<User> users) {
        List<Long> userIds = users.stream()
                .map(User::getId)
                .toList();
        return pushTokenRepository.findByUserIdIn(userIds)
                .stream()
                .map(PushTokenEntity::getToken)
                .toList();
    }
}
