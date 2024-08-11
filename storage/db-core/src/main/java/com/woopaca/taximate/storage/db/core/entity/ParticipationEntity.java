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

    private Long userId;

    @JoinColumn(name = "party_id", nullable = false)
    @ManyToOne(optional = false, targetEntity = PartyEntity.class, fetch = FetchType.LAZY)
    private PartyEntity party;

    public ParticipationEntity() {
    }

    @Builder
    public ParticipationEntity(String role, String status, Long userId, PartyEntity party) {
        this.role = role;
        this.status = status;
        this.userId = userId;
        this.party = party;
    }
}
