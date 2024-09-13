package com.woopaca.taximate.core.domain.fixture;

import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.core.domain.user.User.AccountStatus;
import com.woopaca.taximate.core.domain.user.User.OAuth2Provider;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;

public final class UserFixtures {

    private UserFixtures() {
    }

    public static UserEntity createUserEntityWith(String identifier) {
        return UserEntity.builder()
                .email(identifier + "@example.com")
                .nickname(identifier)
                .provider(OAuth2Provider.KAKAO.name())
                .status(AccountStatus.ACTIVE.name())
                .build();
    }

    public static User createUser(long id) {
        return User.builder()
                .id(id)
                .email("test@email.com")
                .nickname("test")
                .provider(OAuth2Provider.KAKAO)
                .status(AccountStatus.ACTIVE)
                .build();
    }
}
