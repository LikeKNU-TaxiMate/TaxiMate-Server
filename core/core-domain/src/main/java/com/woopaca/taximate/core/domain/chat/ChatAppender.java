package com.woopaca.taximate.core.domain.chat;

import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.ChatEntity;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;
import com.woopaca.taximate.storage.db.core.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class ChatAppender {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public ChatAppender(UserRepository userRepository, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public void appendNew(Chat chat) {
        User sender = chat.getSender();
        UserEntity userEntity = userRepository.findById(sender.getId())
                .orElse(null);
        ChatEntity chatEntity = chat.toEntity(userEntity);
        entityManager.persist(chatEntity);
    }
}
