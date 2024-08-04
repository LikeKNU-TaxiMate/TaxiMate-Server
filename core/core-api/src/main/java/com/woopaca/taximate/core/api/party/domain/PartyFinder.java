package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.core.api.common.error.exception.NonexistentPartyException;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import org.springframework.stereotype.Component;

@Component
public class PartyFinder {

    private final PartyRepository partyRepository;

    public PartyFinder(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    public Party findParty(Long partyId) {
        PartyEntity partyEntity = partyRepository.findById(partyId)
                .orElseThrow(() -> new NonexistentPartyException(partyId));
        return Party.fromEntity(partyEntity);
    }
}
