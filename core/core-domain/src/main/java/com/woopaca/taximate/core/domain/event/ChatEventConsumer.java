package com.woopaca.taximate.core.domain.event;

import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.ChatDataBase;
import com.woopaca.taximate.core.domain.event.dto.ChatEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatEventConsumer {

    private final ChatDataBase chatDataBase;

    public ChatEventConsumer(ChatDataBase chatDataBase) {
        this.chatDataBase = chatDataBase;
    }

    @EventListener
    public void handleChatEvent(ChatEvent chatEvent) {
        Chat chat = chatEvent.chat();
        log.info("채팅 전송: [{}] {} - {} | {}", chat.getParty().getTitle(), chat.getSender().getNickname(), chat.getMessage(), chat.getSentAt());
        chatDataBase.saveChat(chat);
    }
}
