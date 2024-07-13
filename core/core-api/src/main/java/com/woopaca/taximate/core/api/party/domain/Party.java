package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.storage.db.core.entity.PartyEntity;

import java.time.LocalDateTime;

public record Party(Long id, String title, LocalDateTime departureTime) {

    public static Party fromEntity(PartyEntity entity) {
        return new Party(entity.getId(), entity.getTitle(), entity.getDepartureTime());
    }
}
