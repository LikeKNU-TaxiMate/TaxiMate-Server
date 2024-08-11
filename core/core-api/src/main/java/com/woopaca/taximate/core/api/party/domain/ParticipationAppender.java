package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.core.api.common.error.exception.NonexistentPartyException;
import com.woopaca.taximate.core.api.user.domain.User;
import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.repository.ParticipationRepository;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import org.springframework.stereotype.Component;

import static com.woopaca.taximate.core.api.party.domain.Participation.ParticipationRole;

@Component
public class ParticipationAppender {

    private final ParticipationRepository participationRepository;
    private final PartyRepository partyRepository;

    public ParticipationAppender(ParticipationRepository participationRepository, PartyRepository partyRepository) {
        this.participationRepository = participationRepository;
        this.partyRepository = partyRepository;
    }

    public void appendHost(Long partyId, User user) {
        PartyEntity partyEntity = partyRepository.findById(partyId)
                .orElseThrow(() -> new NonexistentPartyException(partyId));
        ParticipationEntity participationEntity = ParticipationEntity.builder()
                .role(ParticipationRole.HOST.name())
                .userId(user.id())
                .party(partyEntity)
                .status(Participation.ParticipationStatus.PARTICIPATING.name())
                .build();
        participationRepository.save(participationEntity);
    }
}
