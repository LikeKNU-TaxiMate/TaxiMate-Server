package com.woopaca.taximate.core.domain.fixture;

import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.model.Coordinate;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;

import java.time.LocalDateTime;

public final class PartyFixtures {

    public static final String TEST_TITLE = "test title";
    public static final String TEST_EXPLANATION = "test explanation";
    public static final LocalDateTime TEST_DEPARTURE_TIME = LocalDateTime.now().plusMinutes(30);
    public static final String TEST_ORIGIN = "test origin";
    public static final String TEST_DESTINATION = "test destination";
    public static final String TEST_ORIGIN_ADDRESS = "test origin address";
    public static final String TEST_DESTINATION_ADDRESS = "test destination address";
    public static final Coordinate TEST_ORIGIN_LOCATION = Coordinate.of(37.123456, 127.123456);
    public static final Coordinate TEST_DESTINATION_LOCATION = Coordinate.of(37.654321, 127.654321);
    public static final int TEST_MAX_PARTICIPANTS = 4;

    private PartyFixtures() {
    }

    public static PartyEntity createPartyEntity() {
        return partyEntityBuilder()
                .maxParticipants(TEST_MAX_PARTICIPANTS)
                .build();
    }

    public static PartyEntity createPartyEntityWith(int maxParticipants) {
        return partyEntityBuilder()
                .maxParticipants(maxParticipants)
                .build();
    }

    public static Party createParty() {
        return partyBuilder().build();
    }

    private static PartyEntity.PartyEntityBuilder partyEntityBuilder() {
        return PartyEntity.builder()
                .title(TEST_TITLE)
                .explanation(TEST_EXPLANATION)
                .departureTime(TEST_DEPARTURE_TIME)
                .origin(TEST_ORIGIN)
                .destination(TEST_DESTINATION)
                .originAddress(TEST_ORIGIN_ADDRESS)
                .destinationAddress(TEST_DESTINATION_ADDRESS)
                .originLatitude(TEST_ORIGIN_LOCATION.latitude())
                .originLongitude(TEST_ORIGIN_LOCATION.longitude())
                .destinationLatitude(TEST_DESTINATION_LOCATION.latitude())
                .destinationLongitude(TEST_DESTINATION_LOCATION.longitude());
    }

    private static Party.PartyBuilder partyBuilder() {
        return Party.builder()
                .title(TEST_TITLE)
                .explanation(TEST_EXPLANATION)
                .departureTime(TEST_DEPARTURE_TIME)
                .origin(TEST_ORIGIN)
                .destination(TEST_DESTINATION)
                .originAddress(TEST_ORIGIN_ADDRESS)
                .destinationAddress(TEST_DESTINATION_ADDRESS)
                .originLocation(TEST_ORIGIN_LOCATION)
                .destinationLocation(TEST_DESTINATION_LOCATION)
                .maxParticipants(TEST_MAX_PARTICIPANTS);
    }
}
