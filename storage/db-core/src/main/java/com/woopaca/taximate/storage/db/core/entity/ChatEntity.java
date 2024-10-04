package com.woopaca.taximate.storage.db.core.entity;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity(name = "chat")
public class ChatEntity extends BaseEntity {

    private String message;

    private String type;

    private Long userId;

    private Long partyId;

    public ChatEntity() {
    }

    @Builder
    public ChatEntity(String message, String type, Long userId, Long partyId) {
        this.message = message;
        this.type = type;
        this.userId = userId;
        this.partyId = partyId;
    }
}
