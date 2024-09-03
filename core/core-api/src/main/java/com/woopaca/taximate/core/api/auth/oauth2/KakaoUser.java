package com.woopaca.taximate.core.api.auth.oauth2;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.woopaca.taximate.core.domain.auth.OAuth2User;
import org.springframework.util.StringUtils;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUser(KakaoAccount kakaoAccount) implements OAuth2User {

    @Override
    public String email() {
        return kakaoAccount.email;
    }

    @Override
    public String profileImageUrl() {
        if (!StringUtils.hasText(kakaoAccount.profile.profileImageUrl)
                || kakaoAccount.profile.profileImageUrl.contains("/account_images/default_profile")) {
            return null;
        }
        return kakaoAccount.profile.profileImageUrl;
    }

    @Override
    public String nickname() {
        return kakaoAccount.profile.nickname;
    }

    @Override
    public String provider() {
        return "KAKAO";
    }

    record KakaoAccount(Profile profile, String email) {
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    record Profile(String profileImageUrl, String nickname) {
    }
}
