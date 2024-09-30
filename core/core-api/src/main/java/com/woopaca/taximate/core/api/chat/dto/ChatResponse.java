package com.woopaca.taximate.core.api.chat.dto;

import com.woopaca.taximate.core.api.party.dto.response.PartiesResponse;
import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.MessageType;
import com.woopaca.taximate.core.domain.user.User;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record ChatResponse(PartiesResponse party, List<ChatListResponse> chat) {

    @Builder
    public record ChatListResponse(Long id, Long partyId, String message, MessageType type, LocalDateTime createdAt,
                                   Sender sender) {

        public static ChatListResponse from(Chat chat) {
            return ChatListResponse.builder()
                    .id(chat.getId())
                    .partyId(chat.getParty().getId())
                    .message(chat.getMessage())
                    .type(chat.getType())
                    .createdAt(chat.getSentAt())
                    .sender(Sender.from(chat.getSender()))
                    .build();
        }
    }

    @Builder
    public record Sender(Long id, String nickname, String profileImage, boolean isMe) {

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
