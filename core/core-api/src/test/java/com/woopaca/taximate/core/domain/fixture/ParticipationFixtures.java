package com.woopaca.taximate.core.domain.fixture;

import com.woopaca.taximate.core.domain.party.Participation;
import com.woopaca.taximate.core.domain.party.Participation.ParticipationRole;
import com.woopaca.taximate.core.domain.party.Participation.ParticipationStatus;
import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;

import java.time.LocalDateTime;

public final class ParticipationFixtures {

    private ParticipationFixtures() {
    }

    public static Participation createParticipantParticipationOf(Long userId) {
        return Participation.builder()
                .role(ParticipationRole.PARTICIPANT)
                .status(ParticipationStatus.PARTICIPATING)
                .user(UserFixtures.createUser(userId))
                .participatedAt(LocalDateTime.now())
                .build();
    }

    public static Participation createHostParticipationOf(long userId) {
        return Participation.builder()
                .role(ParticipationRole.HOST)
                .status(ParticipationStatus.PARTICIPATING)
                .user(UserFixtures.createUser(userId))
                .participatedAt(LocalDateTime.now())
                .build();
    }

    public static ParticipationEntity createHostParticipationEntityWith(PartyEntity partyEntity, UserEntity userEntity) {
        return ParticipationEntity.builder()
                .party(partyEntity)
                .user(userEntity)
                .role(ParticipationRole.HOST.name())
                .status(ParticipationStatus.PARTICIPATING.name())
                .build();
    }

    public static ParticipationEntity createParticipationEntityWith(PartyEntity partyEntity, UserEntity userEntity) {
        return ParticipationEntity.builder()
                .party(partyEntity)
                .user(userEntity)
                .role(ParticipationRole.PARTICIPANT.name())
                .status(ParticipationStatus.PARTICIPATING.name())
                .build();
    }

    public static ParticipationEntity createParticipationEntity() {
        return ParticipationEntity.builder()
                .role(ParticipationRole.PARTICIPANT.name())
                .status(ParticipationStatus.PARTICIPATING.name())
                .user(UserFixtures.createUserEntity())
                .party(PartyFixtures.createPartyEntity())
                .build();
    }
}
