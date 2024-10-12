package com.woopaca.taximate.core.domain.chat;

import com.woopaca.taximate.core.domain.notification.PushNotification;
import com.woopaca.taximate.core.domain.notification.PushNotificationClient;
import com.woopaca.taximate.core.domain.notification.PushTokenFinder;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PushNotificationSender {

    private final PushNotificationClient pushNotificationClient;
    private final PushTokenFinder pushTokenFinder;

    public PushNotificationSender(PushNotificationClient pushNotificationClient, PushTokenFinder pushTokenFinder) {
        this.pushNotificationClient = pushNotificationClient;
        this.pushTokenFinder = pushTokenFinder;
    }

    /**
     * 채팅 푸시 알림 전송
     * @param chat 채팅
     */
    public void send(Chat chat, List<User> receivers) {
        Party party = chat.getParty();
        User sender = chat.getSender();
        PushNotification pushNotification = new PushNotification(party.getTitle(), sender.getNickname(),
                chat.getMessage(), 0, Map.of("partyId", chat.getParty().getId()));
        List<String> tokens = pushTokenFinder.findTokens(receivers);
        pushNotificationClient.sendNotifications(pushNotification, tokens);
    }
}
