package com.woopaca.taximate.core.domain.event;

import com.woopaca.taximate.core.domain.user.User;

import java.time.LocalDateTime;

public record ParticipateEvent(Long partyId, User user, LocalDateTime participatedAt) {
}
