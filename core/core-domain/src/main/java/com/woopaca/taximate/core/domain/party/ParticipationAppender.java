package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.core.domain.error.exception.NonexistentPartyException;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.repository.ParticipationRepository;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import org.springframework.stereotype.Component;

import static com.woopaca.taximate.core.domain.party.Participation.ParticipationRole;
import static com.woopaca.taximate.core.domain.party.Participation.ParticipationStatus;

@Component
public class ParticipationAppender {

    private final ParticipationRepository participationRepository;
    private final PartyRepository partyRepository;

    public ParticipationAppender(ParticipationRepository participationRepository, PartyRepository partyRepository) {
        this.participationRepository = participationRepository;
        this.partyRepository = partyRepository;
    }

    public Participation appendHost(Party party, User user) {
        PartyEntity partyEntity = partyRepository.findById(party.getId())
                .orElseThrow(() -> new NonexistentPartyException(party.getId()));
        ParticipationEntity participationEntity = ParticipationEntity.builder()
                .role(ParticipationRole.HOST.name())
                .userId(user.getId())
                .party(partyEntity)
                .status(ParticipationStatus.PARTICIPATING.name())
                .build();
        ParticipationEntity savedParticipationEntity = participationRepository.save(participationEntity);
        return Participation.fromEntity(savedParticipationEntity);
    }

    public Participation appendParticipant(Party party, User user) {
        PartyEntity partyEntity = partyRepository.findById(party.getId())
                .orElseThrow(() -> new NonexistentPartyException(party.getId()));
        ParticipationEntity participationEntity = ParticipationEntity.builder()
                .role(ParticipationRole.PARTICIPANT.name())
                .userId(user.getId())
                .party(partyEntity)
                .status(ParticipationStatus.PARTICIPATING.name())
                .build();
        ParticipationEntity savedParticipationEntity = participationRepository.save(participationEntity);
        return Participation.fromEntity(savedParticipationEntity);
    }
}
