package com.woopaca.taximate.core.api.user.domain;

import com.woopaca.taximate.core.api.auth.oauth2.OAuth2User;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;
import lombok.Builder;

@Builder
public record User(Long id, String email, String nickname, String profileImage, OAuth2Provider provider,
                   AccountStatus status, boolean isCurrentUser) {

    public static final User GUEST = new User(-1L, "guest", "guest", null, null, AccountStatus.ACTIVE, false);

    public static User of(UserEntity entity, boolean isCurrentUser) {
        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .profileImage(entity.getProfileImage())
                .provider(OAuth2Provider.valueOf(entity.getProvider()))
                .status(AccountStatus.valueOf(entity.getStatus()))
                .isCurrentUser(isCurrentUser)
                .build();
    }

    public static User fromOAuth2User(OAuth2User oAuth2User) {
        return User.builder()
                .email(oAuth2User.email())
                .nickname(oAuth2User.nickname())
                .profileImage(oAuth2User.profileImageUrl())
                .provider(OAuth2Provider.valueOf(oAuth2User.provider()))
                .status(AccountStatus.ACTIVE)
                .isCurrentUser(true)
                .build();
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
