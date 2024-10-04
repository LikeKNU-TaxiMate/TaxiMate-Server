package com.woopaca.taximate.core.domain.chat;

import com.woopaca.taximate.storage.db.core.entity.ChatEntity;
import com.woopaca.taximate.storage.db.core.repository.ChatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatAppender {

    private final ChatRepository chatRepository;

    public ChatAppender(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Chat appendNew(Chat chat) {
        ChatEntity chatEntity = chat.toEntity();
        ChatEntity savedChatEntity = chatRepository.save(chatEntity);
        return chat.newChat(savedChatEntity.getId(), chat);
    }
}
