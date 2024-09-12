package com.woopaca.taximate.core.domain.fixture;

import com.woopaca.taximate.core.domain.party.Participation;
import com.woopaca.taximate.core.domain.party.Participation.ParticipationRole;
import com.woopaca.taximate.core.domain.party.Participation.ParticipationStatus;
import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;

public final class ParticipationFixtures {

    private ParticipationFixtures() {
    }

    public static Participation createParticipantParticipationOf(Long userId) {
        return Participation.builder()
                .role(ParticipationRole.PARTICIPANT)
                .status(ParticipationStatus.PARTICIPATING)
                .userId(userId)
                .build();
    }

    public static Participation createHostParticipationOf(long userId) {
        return Participation.builder()
                .role(ParticipationRole.HOST)
                .status(ParticipationStatus.PARTICIPATING)
                .userId(userId)
                .build();
    }

    public static ParticipationEntity createParticipationEntityWith(PartyEntity partyEntity, Long userId) {
        return ParticipationEntity.builder()
                .party(partyEntity)
                .userId(userId)
                .role(ParticipationRole.PARTICIPANT.name())
                .status(ParticipationStatus.PARTICIPATING.name())
                .build();
    }

    public static ParticipationEntity createParticipationEntity() {
        return ParticipationEntity.builder()
                .role(ParticipationRole.PARTICIPANT.name())
                .status(ParticipationStatus.PARTICIPATING.name())
                .build();
    }
}
