package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;

public record Participation(Long id) {

    public static Participation fromEntity(ParticipationEntity entity) {
        return new Participation(entity.getId());
    }
}
