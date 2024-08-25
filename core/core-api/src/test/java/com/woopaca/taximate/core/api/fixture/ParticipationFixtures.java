package com.woopaca.taximate.core.api.fixture;

import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;

public final class ParticipationFixtures {

    public static ParticipationEntity createParticipationEntityWith(PartyEntity partyEntity, Long userId) {
        return ParticipationEntity.builder()
                .party(partyEntity)
                .userId(userId)
                .role("PARTICIPANT")
                .status("PARTICIPATING")
                .build();
    }
}
