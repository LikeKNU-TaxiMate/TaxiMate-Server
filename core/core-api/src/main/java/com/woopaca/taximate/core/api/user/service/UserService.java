package com.woopaca.taximate.core.api.user.service;

import com.woopaca.taximate.core.api.auth.oauth2.OAuth2User;
import com.woopaca.taximate.core.api.user.domain.User;
import com.woopaca.taximate.core.api.user.domain.UserFinder;
import com.woopaca.taximate.core.api.user.domain.UserRegistrant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserFinder userFinder;
    private final UserRegistrant userRegistrant;

    public UserService(UserFinder userFinder, UserRegistrant userRegistrant) {
        this.userFinder = userFinder;
        this.userRegistrant = userRegistrant;
    }

    @Transactional
    public User registerOAuth2User(OAuth2User oAuth2User) {
        return userFinder.findUserByEmail(oAuth2User.email())
                .orElseGet(() -> {
                    User newUser = User.fromOAuth2User(oAuth2User);
                    return userRegistrant.register(newUser);
                });
    }
}
