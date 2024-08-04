package com.woopaca.taximate.core.api.user.domain;

import com.woopaca.taximate.storage.db.core.entity.UserEntity;

public record User(Long id, String email, String nickname, String profileImage, OAuth2Provider provider,
                   AccountStatus status, boolean isCurrentUser) {

    public static final User GUEST = new User(-1L, "guest", "guest", null, null, AccountStatus.ACTIVE, false);

    public static User of(UserEntity entity, boolean isCurrentUser) {
        return new User(entity.getId(), entity.getEmail(), entity.getNickname(), entity.getProfileImage(),
                OAuth2Provider.valueOf(entity.getProvider()), AccountStatus.valueOf(entity.getStatus()), isCurrentUser);
    }

    public enum AccountStatus {

        ACTIVE,
        INACTIVE,
        DELETED
    }

    public enum OAuth2Provider {

        KAKAO
    }
}
