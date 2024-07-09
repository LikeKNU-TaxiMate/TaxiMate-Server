package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.storage.db.core.entity.PartyEntity;

public record Party(Long id, String title) {

    public static Party fromEntity(PartyEntity entity) {
        return new Party(entity.getId(), entity.getTitle());
    }
}
