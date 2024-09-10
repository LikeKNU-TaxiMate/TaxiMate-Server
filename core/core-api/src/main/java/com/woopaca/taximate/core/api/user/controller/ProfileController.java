package com.woopaca.taximate.core.api.user.controller;

import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.api.common.model.ApiResults.ApiResponse;
import com.woopaca.taximate.core.api.user.dto.response.ProfileResponse;
import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/profiles")
@RestController
public class ProfileController {

    @GetMapping
    public ApiResponse<ProfileResponse> getProfile() {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        return ApiResults.success(ProfileResponse.from(authenticatedUser));
    }
}
