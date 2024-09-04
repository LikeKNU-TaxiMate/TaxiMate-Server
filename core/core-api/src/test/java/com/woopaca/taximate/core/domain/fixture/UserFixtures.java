package com.woopaca.taximate.core.domain.fixture;

import com.woopaca.taximate.storage.db.core.entity.UserEntity;

public final class UserFixtures {

    public static UserEntity createUserEntityWith(String identifier) {
        return UserEntity.builder()
                .email(identifier + "@example.com")
                .nickname(identifier)
                .provider("KAKAO")
                .status("ACTIVE")
                .build();
    }
}
