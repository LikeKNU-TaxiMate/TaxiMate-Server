package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.core.api.common.error.exception.NonexistentPartyException;
import com.woopaca.taximate.core.api.user.domain.User;
import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.repository.ParticipationRepository;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public List<Party> findParticipatingParties(User user) {
        return participationRepository.findByUserId(user.getId())
                .stream()
                .map(ParticipationEntity::getParty)
                .map(Party::fromEntityExcludeParticipants)
                .filter(Party::isProgress)
                .toList();
    }
}
