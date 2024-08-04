package com.woopaca.taximate.storage.db.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @ManyToOne(optional = false, targetEntity = PartyEntity.class)
    private PartyEntity party;
}
