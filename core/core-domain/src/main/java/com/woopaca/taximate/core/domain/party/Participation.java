package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Participation {

    public static final int MAX_PARTICIPATING_PARTIES_COUNT = 3;

    @EqualsAndHashCode.Include
    private Long id;
    private ParticipationRole role;
    private ParticipationStatus status;
    private LocalDateTime participatedAt;
    private Long userId;

    @Builder
    public Participation(Long id, ParticipationRole role, ParticipationStatus status, LocalDateTime participatedAt, Long userId) {
        this.id = id;
        this.role = role;
        this.status = status;
        this.participatedAt = participatedAt;
        this.userId = userId;
    }

    public static Participation fromEntity(ParticipationEntity entity) {
        return Participation.builder()
                .id(entity.getId())
                .role(ParticipationRole.valueOf(entity.getRole()))
                .status(ParticipationStatus.valueOf(entity.getStatus()))
                .participatedAt(entity.getCreatedAt())
                .userId(entity.getUserId())
                .build();
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
        LEFT,
        WARNED,
        BANNED
    }
}
