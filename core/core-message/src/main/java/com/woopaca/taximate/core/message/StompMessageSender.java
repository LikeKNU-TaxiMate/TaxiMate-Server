package com.woopaca.taximate.core.message;

import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.MessageSender;
import com.woopaca.taximate.core.domain.chat.WebSocketSessions;
import com.woopaca.taximate.core.domain.party.Participation;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.core.message.dto.ChatMessage;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class StompMessageSender implements MessageSender {

    public static final String DESTINATION_PREFIX = "/queue/messages/";

    private final SimpMessageSendingOperations messagingTemplate;
    private final WebSocketSessions webSocketSessions;

    public StompMessageSender(SimpMessageSendingOperations messagingTemplate, WebSocketSessions webSocketSessions) {
        this.messagingTemplate = messagingTemplate;
        this.webSocketSessions = webSocketSessions;
    }

    @Override
    public void send(Chat chat) {
        Party party = chat.getParty();
        for (Participation participation : party.getParticipationSet()) {
            User recipient = participation.getUser();
            if (chat.isLeaveMessage() && chat.getSender().equals(recipient)) {
                continue;
            }

            String identifier = webSocketSessions.getSession(recipient.getId());
            if (StringUtils.hasText(identifier)) {
                sendTo(chat, identifier);
            }
        }
    }

    @Override
    public void sendTo(Chat chat, String identifier) {
        if (!StringUtils.hasText(identifier)) {
            return;
        }
        ChatMessage chatMessage = ChatMessage.from(chat);
        messagingTemplate.convertAndSend(DESTINATION_PREFIX + identifier, chatMessage);
    }
}
