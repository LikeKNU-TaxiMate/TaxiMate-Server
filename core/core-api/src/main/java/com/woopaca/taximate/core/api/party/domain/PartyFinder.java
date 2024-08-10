package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.core.api.common.error.exception.NonexistentPartyException;
import com.woopaca.taximate.core.api.user.domain.User;
import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.repository.ParticipationRepository;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.woopaca.taximate.core.api.party.domain.Participation.ParticipationRole;

@Component
public class PartyFinder {

    private final PartyRepository partyRepository;
    private final ParticipationRepository participationRepository;

    public PartyFinder(PartyRepository partyRepository, ParticipationRepository participationRepository) {
        this.partyRepository = partyRepository;
        this.participationRepository = participationRepository;
    }

    public Party findParty(Long partyId) {
        PartyEntity partyEntity = partyRepository.findById(partyId)
                .orElseThrow(() -> new NonexistentPartyException(partyId));
        return Party.fromEntity(partyEntity);
    }

    public List<Party> findHostingParties(User user) {
        return participationRepository.findByUserIdAndRole(user.id(), ParticipationRole.HOST.name())
                .stream()
                .map(ParticipationEntity::getParty)
                .map(Party::fromEntity)
                .filter(Party::isProgress)
                .toList();
    }
}
