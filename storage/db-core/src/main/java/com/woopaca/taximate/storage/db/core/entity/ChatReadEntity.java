package com.woopaca.taximate.storage.db.core.entity;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity(name = "chat_read")
public class ChatReadEntity extends BaseEntity {

    private Long userId;

    private Long partyId;

    private Long lastChatId;

    public ChatReadEntity() {
    }

    @Builder
    public ChatReadEntity(Long userId, Long partyId, Long lastChatId) {
        this.userId = userId;
        this.partyId = partyId;
        this.lastChatId = lastChatId;
    }

    public void updateLastChatId(Long readChatId) {
        if (lastChatId < readChatId) {
            lastChatId = readChatId;
        }
    }
}
