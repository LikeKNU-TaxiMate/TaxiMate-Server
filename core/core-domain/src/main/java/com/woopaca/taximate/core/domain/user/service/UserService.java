package com.woopaca.taximate.core.domain.user.service;

import com.woopaca.taximate.core.domain.auth.OAuth2User;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.core.domain.user.UserFinder;
import com.woopaca.taximate.core.domain.user.UserRegistrant;
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
