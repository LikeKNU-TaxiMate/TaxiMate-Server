package com.woopaca.taximate.core.api.user.dto.response;

import com.woopaca.taximate.core.domain.user.User;

public record ProfileResponse(Long id, String nickname, String profileImage) {

    public static ProfileResponse from(User user) {
        return new ProfileResponse(user.getId(), user.getNickname(), user.getProfileImage());
    }
}
