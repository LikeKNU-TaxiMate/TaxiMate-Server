package com.woopaca.taximate.core.api.chat.dto;

import java.time.LocalDateTime;

public record ChatRoomResponse(Long id, String title, int maxParticipants, int currentParticipants, boolean isProgress,
                               String recentMessage, LocalDateTime recentMessageTime, int unreadCount) {
}
