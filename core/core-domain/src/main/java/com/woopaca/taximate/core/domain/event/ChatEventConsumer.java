package com.woopaca.taximate.core.domain.event;

import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.ChatAppender;
import com.woopaca.taximate.core.domain.chat.ChatReadRecorder;
import com.woopaca.taximate.core.domain.event.dto.ChatEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ChatEventConsumer {

    private final ChatAppender chatAppender;
    private final ChatReadRecorder chatReadRecorder;

    public ChatEventConsumer(ChatAppender chatAppender, ChatReadRecorder chatReadRecorder) {
        this.chatAppender = chatAppender;
        this.chatReadRecorder = chatReadRecorder;
    }

    @EventListener
    public void handleChatEvent(ChatEvent chatEvent) {
        Chat newChat = chatAppender.appendNew(chatEvent.chat());
        if (newChat.isParticipateMessage()) {
            chatReadRecorder.createReadHistory(newChat);
            return;
        }
        if (newChat.isLeaveMessage()) {
            chatReadRecorder.removeReadHistory(newChat);
            return;
        }
        chatReadRecorder.recordReadHistory(newChat);
    }
}
