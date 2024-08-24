package com.woopaca.taximate.core.api.user.domain;

import com.woopaca.taximate.core.api.common.error.exception.NonexistentUserException;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;
import com.woopaca.taximate.storage.db.core.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class UserFinder {

    private final UserRepository userRepository;

    public UserFinder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        if (String.valueOf(principal).equals("anonymousUser")) {
            return User.GUEST;
        }
        return (User) principal;
    }

    public User findUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NonexistentUserException(userId));
        User authenticatedUser = findAuthenticatedUser();
        return User.of(userEntity, Objects.equals(userId, authenticatedUser.getId()));
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userEntity -> User.of(userEntity, true));
    }
}
