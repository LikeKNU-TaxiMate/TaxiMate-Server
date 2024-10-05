package com.woopaca.taximate.core.api.chat.dto;

import com.woopaca.taximate.core.api.party.dto.response.PartiesResponse;
import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.MessageType;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record ChatListResponse(PartiesResponse party, List<ChatResponse> chats) {

    public static ChatListResponse of(Party party, List<Chat> chats) {
        List<ChatResponse> chatResponses = chats.stream()
                .map(ChatResponse::from)
                .toList();
        PartiesResponse partiesResponse = PartiesResponse.from(party);
        return new ChatListResponse(partiesResponse, chatResponses);
    }

    @Builder
    record ChatResponse(Long id, String message, MessageType type, LocalDateTime createdAt, Sender sender) {

        public static ChatResponse from(Chat chat) {
            User user = chat.getSender();
            Sender sender = Sender.from(user);
            return ChatResponse.builder()
                    .id(chat.getId())
                    .message(chat.getMessage())
                    .type(chat.getType())
                    .createdAt(chat.getSentAt())
                    .sender(sender)
                    .build();
        }
    }

    @Builder
    record Sender(Long id, String nickname, String profileImage, boolean isMe) {

        public static Sender from(User user) {
            return Sender.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profileImage(user.getProfileImage())
                    .isMe(user.isCurrentUser())
                    .build();
        }
    }
}
