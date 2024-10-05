package com.woopaca.taximate.core.domain.chat;

import com.woopaca.taximate.core.domain.party.Party;
import lombok.Getter;

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
}
