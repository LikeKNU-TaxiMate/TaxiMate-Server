package com.woopaca.taximate.storage.db.core.entity;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity(name = "push_token")
public class PushTokenEntity extends BaseEntity {

    private Long userId;

    private String token;

    public PushTokenEntity() {
    }

    @Builder
    public PushTokenEntity(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
