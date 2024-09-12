package com.woopaca.taximate.core.domain.user;

import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.auth.OAuth2User;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    public static final User GUEST = new User(-1L, "guest", "guest", null, null, AccountStatus.ACTIVE);

    @EqualsAndHashCode.Include
    private Long id;
    private String email;
    private String nickname;
    private String profileImage;
    private OAuth2Provider provider;
    private AccountStatus status;

    public User(Long id, String email, String nickname, String profileImage, OAuth2Provider provider, AccountStatus status) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.provider = provider;
        this.status = status;
    }

    public static User of(UserEntity entity, boolean isCurrentUser) {
        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .profileImage(entity.getProfileImage())
                .provider(OAuth2Provider.valueOf(entity.getProvider()))
                .status(AccountStatus.valueOf(entity.getStatus()))
                .build();
    }

    public static User fromOAuth2User(OAuth2User oAuth2User) {
        return User.builder()
                .email(oAuth2User.email())
                .nickname(oAuth2User.nickname())
                .profileImage(oAuth2User.profileImageUrl())
                .provider(OAuth2Provider.valueOf(oAuth2User.provider()))
                .status(AccountStatus.ACTIVE)
                .build();
    }

    public boolean isCurrentUser() {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        return authenticatedUser != null && authenticatedUser.equals(this);
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
