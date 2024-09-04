package com.woopaca.taximate.core.domain.fixture;

import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.model.Coordinate;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;

import java.time.LocalDateTime;

public final class PartyFixtures {

    public static PartyEntity createPartyEntity() {
        return PartyEntity.builder()
                .title("test title")
                .explanation("test explanation")
                .departureTime(LocalDateTime.now().plusMinutes(30))
                .origin("test origin")
                .destination("test destination")
                .originAddress("test origin address")
                .destinationAddress("test destination address")
                .originLatitude(37.123456)
                .originLongitude(127.123456)
                .destinationLatitude(37.654321)
                .destinationLongitude(127.654321)
                .maxParticipants(4)
                .build();
    }

    public static PartyEntity createPartyEntityWith(int maxParticipants) {
        return PartyEntity.builder()
                .title("test title")
                .explanation("test explanation")
                .departureTime(LocalDateTime.now().plusMinutes(30))
                .origin("test origin")
                .destination("test destination")
                .originAddress("test origin address")
                .destinationAddress("test destination address")
                .originLatitude(37.123456)
                .originLongitude(127.123456)
                .destinationLatitude(37.654321)
                .destinationLongitude(127.654321)
                .maxParticipants(maxParticipants)
                .build();
    }

    public static Party createParty() {
        return Party.builder()
                .title("test title")
                .explanation("test explanation")
                .departureTime(LocalDateTime.now().plusMinutes(30))
                .origin("test origin")
                .destination("test destination")
                .originAddress("test origin address")
                .destinationAddress("test destination address")
                .originLocation(Coordinate.of(37.123456, 127.123456))
                .destinationLocation(Coordinate.of(37.654321, 127.654321))
                .maxParticipants(4)
                .build();
    }
}
