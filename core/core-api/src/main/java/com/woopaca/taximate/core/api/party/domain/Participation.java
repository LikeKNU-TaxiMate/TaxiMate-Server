package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Participation {

    public static final int MAX_PARTICIPATING_PARTIES_COUNT = 3;

    @EqualsAndHashCode.Include
    private Long id;
    private ParticipationRole role;
    private ParticipationStatus status;
    private Long userId;

    public Participation(Long id, ParticipationRole role, ParticipationStatus status, Long userId) {
        this.id = id;
        this.role = role;
        this.status = status;
        this.userId = userId;
    }

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
