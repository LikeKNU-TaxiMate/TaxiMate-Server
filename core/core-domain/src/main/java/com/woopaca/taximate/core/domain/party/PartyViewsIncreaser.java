package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PartyViewsIncreaser {

    private final PartyRepository partyRepository;

    public PartyViewsIncreaser(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Async
    @Transactional
    public void increaseViews(Party party) {
        partyRepository.increaseViews(party.getId());
    }
}
