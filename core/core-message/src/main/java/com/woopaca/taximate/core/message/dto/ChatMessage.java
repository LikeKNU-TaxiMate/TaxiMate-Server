package com.woopaca.taximate.core.message.dto;

import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.MessageType;
import com.woopaca.taximate.core.domain.user.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessage(Long partyId, String partyTitle, String message, MessageType type, LocalDateTime createdAt,
                          Sender sender) {

    public static ChatMessage from(Chat chat) {
        if (chat.getParty() == null) {
            return ChatMessage.builder()
                    .message(chat.getMessage())
                    .createdAt(chat.getSentAt())
                    .build();
        }

        return ChatMessage.builder()
                .partyId(chat.getParty().getId())
                .partyTitle(chat.getParty().getTitle())
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
