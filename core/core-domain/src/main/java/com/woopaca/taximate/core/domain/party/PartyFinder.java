package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.core.domain.error.exception.NonexistentPartyException;
import com.woopaca.taximate.core.domain.user.User;
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

    public Party findPartyWithLock(Long partyId) {
        PartyEntity partyEntity = partyRepository.findByIdForUpdate(partyId)
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

    public List<Party> findParticipatedAllParties(User user) {
        List<Long> partyIds = participationRepository.findPartyIdByUserId(user.getId());
        return partyRepository.findByIdIn(partyIds)
                .stream()
                .map(Party::fromEntity)
                .toList();
    }
}
