package com.woopaca.taximate.core.domain.event;

import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.event.dto.ChatEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ChatEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public ChatEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishChatEvent(Chat chat) {
        ChatEvent event = new ChatEvent(chat);
        eventPublisher.publishEvent(event);
    }
}
