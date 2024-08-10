package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import org.springframework.stereotype.Component;

@Component
public class PartyAppender {

    private final PartyRepository partyRepository;

    public PartyAppender(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    public Long appendNew(Party newParty) {
        // TODO 새로운 팟 추가
        return null;
    }
}
