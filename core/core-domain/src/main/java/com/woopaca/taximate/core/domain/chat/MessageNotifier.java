package com.woopaca.taximate.core.domain.chat;

import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageNotifier {

    private final MessageSender messageSender;
    private final PushNotificationSender pushNotificationSender;

    public MessageNotifier(MessageSender messageSender, PushNotificationSender pushNotificationSender) {
        this.messageSender = messageSender;
        this.pushNotificationSender = pushNotificationSender;
    }

    @Async
    public void notify(Chat chat) {
        messageSender.send(chat);
        Party party = chat.getParty();
        User sender = chat.getSender();
        List<User> notificationReceivers = party.getParticipants()
                .stream()
                .filter(user -> !user.equals(sender))
                .toList();
        pushNotificationSender.send(chat, notificationReceivers);
    }
}
