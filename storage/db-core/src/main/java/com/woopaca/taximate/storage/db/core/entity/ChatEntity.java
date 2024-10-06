package com.woopaca.taximate.storage.db.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import static jakarta.persistence.ConstraintMode.NO_CONSTRAINT;

@Getter
@Entity(name = "chat")
public class ChatEntity {

    @Id
    private Long id;

    private String message;

    private String type;

    private LocalDateTime createdAt;

    private Long partyId;

    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(NO_CONSTRAINT))
    @ManyToOne(optional = false, targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    private UserEntity user;

    public ChatEntity() {
    }

    @Builder
    public ChatEntity(Long id, String message, String type, LocalDateTime createdAt, Long partyId, UserEntity user) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.createdAt = createdAt;
        this.partyId = partyId;
        this.user = user;
    }
}
