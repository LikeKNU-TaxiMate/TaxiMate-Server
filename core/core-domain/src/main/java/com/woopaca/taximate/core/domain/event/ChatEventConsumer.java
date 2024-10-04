package com.woopaca.taximate.core.domain.event;

import com.woopaca.taximate.core.domain.chat.ChatAppender;
import com.woopaca.taximate.core.domain.event.dto.ChatEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ChatEventConsumer {

    private final ChatAppender chatAppender;

    public ChatEventConsumer(ChatAppender chatAppender) {
        this.chatAppender = chatAppender;
    }

    @EventListener
    public void handleChatEvent(ChatEvent chatEvent) {
        chatAppender.appendChat(chatEvent.chat());
    }
}
