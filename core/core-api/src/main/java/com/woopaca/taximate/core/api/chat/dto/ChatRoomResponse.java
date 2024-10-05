package com.woopaca.taximate.core.api.chat.dto;

import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.ChatRoom;
import com.woopaca.taximate.core.domain.party.Party;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatRoomResponse(Long id, String title, int maxParticipants, int currentParticipants, boolean isProgress,
                               String recentMessage, LocalDateTime recentMessageTime, int unreadCount) {

    public static ChatRoomResponse from(ChatRoom chatRoom) {
        Party party = chatRoom.getParty();
        Chat recentMessage = chatRoom.getRecentMessage();
        int unreadCount = chatRoom.getUnreadCount();
        return ChatRoomResponse.builder()
                .id(party.getId())
                .title(party.getTitle())
                .maxParticipants(party.getMaxParticipants())
                .currentParticipants(party.currentParticipantsCount())
                .isProgress(party.isProgress())
                .recentMessage(recentMessage.getMessage())
                .recentMessageTime(recentMessage.getSentAt())
                .unreadCount(unreadCount)
                .build();
    }
}
