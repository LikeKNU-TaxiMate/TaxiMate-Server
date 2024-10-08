package com.woopaca.taximate.storage.db.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity(name = "chat_read")
public class ChatReadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}
