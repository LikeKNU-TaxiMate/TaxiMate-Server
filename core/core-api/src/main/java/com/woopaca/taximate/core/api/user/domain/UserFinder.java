package com.woopaca.taximate.core.api.user.domain;

import com.woopaca.taximate.core.api.common.error.exception.NonexistentUserException;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;
import com.woopaca.taximate.storage.db.core.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserFinder {

    private final UserRepository userRepository;
    private final AuthenticatedUserFinder authenticatedUserFinder;

    public UserFinder(UserRepository userRepository, AuthenticatedUserFinder authenticatedUserFinder) {
        this.userRepository = userRepository;
        this.authenticatedUserFinder = authenticatedUserFinder;
    }

    public User findUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NonexistentUserException(userId));
        User authenticatedUser = authenticatedUserFinder.findAuthenticatedUser();
        return User.of(userEntity, Objects.equals(userId, authenticatedUser.id()));
    }
}
