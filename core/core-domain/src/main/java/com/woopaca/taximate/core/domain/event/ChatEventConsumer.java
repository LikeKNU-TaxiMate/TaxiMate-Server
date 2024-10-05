package com.woopaca.taximate.core.domain.event;

import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.ChatAppender;
import com.woopaca.taximate.core.domain.chat.ChatReadRecorder;
import com.woopaca.taximate.core.domain.event.dto.ChatEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ChatEventConsumer {

    private final ChatAppender chatAppender;
    private final ChatReadRecorder chatReadRecorder;

    public ChatEventConsumer(ChatAppender chatAppender, ChatReadRecorder chatReadRecorder) {
        this.chatAppender = chatAppender;
        this.chatReadRecorder = chatReadRecorder;
    }

    @Transactional
    @EventListener
    public void handleChatEvent(ChatEvent chatEvent) {
        Chat chat = chatEvent.chat();
        chatAppender.appendNew(chat);
        if (chat.isParticipateMessage()) {
            chatReadRecorder.createReadHistory(chat);
            return;
        }
        if (chat.isLeaveMessage()) {
            chatReadRecorder.removeReadHistory(chat);
            return;
        }
        chatReadRecorder.recordReadHistory(chat);
    }
}
