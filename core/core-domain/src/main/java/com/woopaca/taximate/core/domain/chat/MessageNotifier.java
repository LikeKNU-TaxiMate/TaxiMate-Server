package com.woopaca.taximate.core.domain.chat;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MessageNotifier {

    private final MessageSender messageSender;

    public MessageNotifier(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Async
    public void notify(Chat chat) {
        messageSender.send(chat);
        // TODO multicast push notifications
    }
}
