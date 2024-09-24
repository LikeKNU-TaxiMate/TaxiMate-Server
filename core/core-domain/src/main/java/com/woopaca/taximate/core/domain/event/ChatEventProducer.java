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

    public void publishChatEvent(Chat chat) { // TODO consumer는 이벤트를 받아 DB에 채팅을 저장하는 로직 수행
        ChatEvent event = new ChatEvent(chat);
        eventPublisher.publishEvent(event);
    }
}
