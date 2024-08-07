package com.woopaca.taximate.storage.db.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity(name = "user")
public class UserEntity extends BaseEntity {

    private String email;

    private String nickname;

    private String profileImage;

    @Column(columnDefinition = "CHAR(6) DEFAULT 'KAKAO'")
    private String provider;

    @Column(columnDefinition = "CHAR(8) DEFAULT 'ACTIVE'")
    private String status;
}
