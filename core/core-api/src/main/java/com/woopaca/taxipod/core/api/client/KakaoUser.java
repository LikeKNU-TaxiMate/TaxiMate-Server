package com.woopaca.taxipod.core.api.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUser(KakaoAccount kakaoAccount) {

    public String email() {
        return kakaoAccount.email;
    }

    public String profileImageUrl() {
        return kakaoAccount.profile.profileImageUrl;
    }

    record KakaoAccount(Profile profile, String email) {
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    record Profile(String profileImageUrl) {
    }

}
