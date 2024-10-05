package com.woopaca.taximate.core.domain.user;

import com.woopaca.taximate.core.domain.error.exception.NonexistentUserException;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;
import com.woopaca.taximate.storage.db.core.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserFinder {

    private final UserRepository userRepository;

    public UserFinder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(NonexistentUserException::new);
        return User.fromEntity(userEntity);
    }

    // TODO cache 적용
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::fromEntity);
    }
}
