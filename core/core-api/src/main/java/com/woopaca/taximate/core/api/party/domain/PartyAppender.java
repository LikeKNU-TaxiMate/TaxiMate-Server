package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import org.springframework.stereotype.Component;

@Component
public class PartyAppender {

    private final PartyRepository partyRepository;

    public PartyAppender(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    public Long appendNew(Party party) {
        PartyEntity partyEntity = PartyEntity.builder()
                .title(party.title())
                .explanation(party.explanation())
                .departureTime(party.departureTime())
                .origin(party.origin())
                .originAddress(party.originAddress())
                .originLatitude(party.originLocation().latitude())
                .originLongitude(party.originLocation().longitude())
                .destination(party.destination())
                .destinationAddress(party.destinationAddress())
                .destinationLatitude(party.destinationLocation().latitude())
                .destinationLongitude(party.destinationLocation().longitude())
                .maxParticipants(party.maxParticipants())
                .build();
        PartyEntity savedPartyEntity = partyRepository.save(partyEntity);
        return savedPartyEntity.getId();
    }
}
