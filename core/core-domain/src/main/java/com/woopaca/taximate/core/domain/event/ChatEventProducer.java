package com.woopaca.taximate.core.domain.event;

import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.event.dto.ChatEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatEventProducer {

    private final ApplicationEventPublisher eventPublisher;

    public ChatEventProducer(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishChatEvent(Chat chat) {
        ChatEvent event = new ChatEvent(chat);
        eventPublisher.publishEvent(event);
    }
}
