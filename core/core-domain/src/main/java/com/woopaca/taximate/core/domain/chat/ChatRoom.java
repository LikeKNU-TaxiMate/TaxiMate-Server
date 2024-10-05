package com.woopaca.taximate.core.domain.chat;

import com.woopaca.taximate.core.domain.party.Party;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoom {

    private Party party;
    private Chat recentMessage;
    private int unreadCount;

    public ChatRoom(Party party, Chat recentMessage, int unreadCount) {
        this.party = party;
        this.recentMessage = recentMessage;
        this.unreadCount = unreadCount;
    }

    public Boolean isProgress() {
        return party.isProgress();
    }

    public LocalDateTime getRecentMessageTime() {
        return recentMessage.getSentAt();
    }
}
