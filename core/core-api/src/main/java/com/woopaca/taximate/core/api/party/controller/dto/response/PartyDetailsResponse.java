package com.woopaca.taximate.core.api.party.controller.dto.response;

import com.woopaca.taximate.core.api.party.domain.Participation.ParticipationStatus;
import com.woopaca.taximate.core.api.party.domain.Party;
import com.woopaca.taximate.core.api.party.domain.PartyDetails;
import com.woopaca.taximate.core.api.party.model.Coordinate;
import com.woopaca.taximate.core.api.taxi.domain.Taxi;
import com.woopaca.taximate.core.api.user.domain.User;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PartyDetailsResponse(Long id, String title, LocalDateTime departureTime, String explanation,
                                   String origin, String originAddress, Coordinate originLocation,
                                   String destination, String destinationAddress, Coordinate destinationLocation,
                                   int maxParticipants, int currentParticipants, int views, String status,
                                   LocalDateTime createdAt, HostResponse host, TaxiResponse taxi) {

    public static PartyDetailsResponse from(PartyDetails partyDetails) {
        ParticipationStatus status = partyDetails.party()
                .participationStatusOf(partyDetails.authenticatedUser());
        return PartyDetailsResponse.of(partyDetails.party(), partyDetails.host(), partyDetails.taxi(), status);
    }

    public static PartyDetailsResponse of(Party party, User user, Taxi taxi, ParticipationStatus status) {
        return PartyDetailsResponse.builder()
                .id(party.id())
                .title(party.title())
                .departureTime(party.departureTime())
                .explanation(party.explanation())
                .origin(party.origin())
                .originAddress(party.originAddress())
                .originLocation(party.originLocation())
                .destination(party.destination())
                .destinationAddress(party.destinationAddress())
                .destinationLocation(party.destinationLocation())
                .maxParticipants(party.maxParticipants())
                .currentParticipants(party.currentParticipants())
                .views(party.views())
                .status(status.name())
                .createdAt(party.createdAt())
                .host(HostResponse.from(user))
                .taxi(TaxiResponse.from(taxi))
                .build();
    }

    record HostResponse(Long id, String name, String profileImageUrl, boolean isMe) {

        public static HostResponse from(User user) {
            return new HostResponse(user.id(), user.nickname(), user.profileImage(), user.isCurrentUser());
        }
    }

    record TaxiResponse(List<Coordinate> route, int fare, int duration) {

        public static TaxiResponse from(Taxi taxi) {
            return new TaxiResponse(taxi.route(), taxi.fare(), taxi.duration());
        }
    }
}
