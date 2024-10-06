package com.woopaca.taximate.core.domain.user;

import com.woopaca.taximate.storage.db.core.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserFinder {

    private final UserRepository userRepository;

    public UserFinder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO cache 적용
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::fromEntity);
    }
}
