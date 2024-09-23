package com.woopaca.taximate.core.domain.event;

import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;

import java.time.LocalDateTime;

public record LeaveEvent(Party party, User leaver, LocalDateTime leftAt) {
}
