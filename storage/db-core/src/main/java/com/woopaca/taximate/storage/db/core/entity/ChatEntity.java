package com.woopaca.taximate.storage.db.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "chat")
public class ChatEntity {

    @Id
    private Long id;

    private String message;

    private String type;

    private Long userId;

    private Long partyId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public ChatEntity() {
    }

    @Builder
    public ChatEntity(Long id, String message, String type, Long userId, Long partyId) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.userId = userId;
        this.partyId = partyId;
    }
}
