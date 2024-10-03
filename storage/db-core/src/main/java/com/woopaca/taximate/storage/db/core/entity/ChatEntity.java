package com.woopaca.taximate.storage.db.core.entity;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity(name = "chat")
public class ChatEntity extends BaseEntity {

    private String message;

    private Long userId;

    private Long partyId;

    public ChatEntity() {
    }

    @Builder
    public ChatEntity(String message, Long userId, Long partyId) {
        this.message = message;
        this.userId = userId;
        this.partyId = partyId;
    }
}
