package com.woopaca.taximate.core.domain.user;

import com.woopaca.taximate.storage.db.core.entity.UserEntity;
import com.woopaca.taximate.storage.db.core.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrant {

    private final UserRepository userRepository;

    public UserRegistrant(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User newUser) {
        UserEntity userEntity = UserEntity.builder()
                .email(newUser.getEmail())
                .nickname(newUser.getNickname())
                .profileImage(newUser.getProfileImage())
                .provider(newUser.getProvider().name())
                .status(User.AccountStatus.ACTIVE.name())
                .build();
        UserEntity registeredUser = userRepository.save(userEntity);
        return User.fromEntity(registeredUser);
    }
}
