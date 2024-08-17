package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;

public record Participation(Long id, ParticipationRole role, ParticipationStatus status, Long userId) {

    public static final int MAX_PARTICIPATING_PARTIES_COUNT = 3;

    public static Participation fromEntity(ParticipationEntity entity) {
        return new Participation(entity.getId(), ParticipationRole.valueOf(entity.getRole()), ParticipationStatus.valueOf(entity.getStatus()), entity.getUserId());
    }

    public boolean isHost() {
        return role == ParticipationRole.HOST;
    }

    public boolean isParticipating() {
        return status == ParticipationStatus.PARTICIPATING;
    }

    public enum ParticipationRole {

        HOST,
        PARTICIPANT
    }

    public enum ParticipationStatus {

        NONE,
        PARTICIPATING,
        TERMINATED,
        WARNED,
        BANNED
    }
}
