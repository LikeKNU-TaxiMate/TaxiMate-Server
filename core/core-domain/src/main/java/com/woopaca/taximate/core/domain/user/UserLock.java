package com.woopaca.taximate.core.domain.user;

import com.woopaca.taximate.storage.db.core.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserLock {

    private final UserRepository userRepository;

    public UserLock(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void lock(User user) {
        userRepository.acquireExclusiveLock(user.getId());
    }
}
