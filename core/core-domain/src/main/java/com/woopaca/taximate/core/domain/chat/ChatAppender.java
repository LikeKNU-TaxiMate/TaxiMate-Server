package com.woopaca.taximate.core.domain.chat;

import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.ChatEntity;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;
import com.woopaca.taximate.storage.db.core.repository.ChatRepository;
import com.woopaca.taximate.storage.db.core.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatAppender {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatAppender(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    public void appendNew(Chat chat) {
        User sender = chat.getSender();
        UserEntity userEntity = userRepository.findById(sender.getId())
                .orElse(null);
        ChatEntity chatEntity = chat.toEntity(userEntity);
        chatRepository.save(chatEntity);
    }
}
