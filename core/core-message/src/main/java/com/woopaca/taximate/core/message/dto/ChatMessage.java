package com.woopaca.taximate.core.message.dto;

import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.MessageType;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessage(Long id, Long partyId, String partyTitle, String message, MessageType type,
                          LocalDateTime createdAt, Sender sender) {

    public static ChatMessage from(Chat chat) {
        Party party = chat.getParty();
        return ChatMessage.builder()
                .id(chat.getId())
                .partyId(party.getId())
                .partyTitle(party.getTitle())
                .message(chat.getMessage())
                .type(chat.getType())
                .createdAt(chat.getSentAt())
                .sender(Sender.from(chat.getSender()))
                .build();
    }

    record Sender(Long id, String nickname, String profileImage) {

        public static Sender from(User sender) {
            return new Sender(sender.getId(), sender.getNickname(), sender.getProfileImage());
        }
    }
}
