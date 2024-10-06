package com.woopaca.taximate.storage.db.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity(name = "participation")
public class ParticipationEntity extends BaseEntity {

    @Column(columnDefinition = "CHAR(11)")
    private String role;

    @Column(columnDefinition = "CHAR(13)")
    private String status;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(optional = false, targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    private UserEntity user;

    @JoinColumn(name = "party_id", nullable = false)
    @ManyToOne(optional = false, targetEntity = PartyEntity.class, fetch = FetchType.LAZY)
    private PartyEntity party;

    public ParticipationEntity() {
    }

    @Builder
    public ParticipationEntity(String role, String status, UserEntity user, PartyEntity party) {
        this.role = role;
        this.status = status;
        this.user = user;
        this.party = party;
    }

    public void leave() {
        this.status = "LEFT";
    }
}
