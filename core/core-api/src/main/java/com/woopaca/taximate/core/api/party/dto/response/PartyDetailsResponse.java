package com.woopaca.taximate.core.api.party.dto.response;

import com.woopaca.taximate.core.domain.party.Participation.ParticipationStatus;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.PartyDetails;
import com.woopaca.taximate.core.domain.party.model.Coordinate;
import com.woopaca.taximate.core.domain.taxi.Taxi;
import com.woopaca.taximate.core.domain.user.User;
import lombok.Builder;

import java.util.List;

@Builder
public record PartyDetailsResponse(Long id, String title, String departureTime, String explanation,
                                   String origin, String originAddress, Coordinate originLocation,
                                   String destination, String destinationAddress, Coordinate destinationLocation,
                                   int maxParticipants, int currentParticipants, int views, String status,
                                   String createdAt, HostResponse host, TaxiResponse taxi) {

    public static PartyDetailsResponse from(PartyDetails partyDetails) {
        ParticipationStatus status = partyDetails.getParty()
                .participationStatusOf(partyDetails.getAuthenticatedUser());
        return PartyDetailsResponse.of(partyDetails.getParty(), partyDetails.getHost(), partyDetails.getTaxi(), status);
    }

    public static PartyDetailsResponse of(Party party, User user, Taxi taxi, ParticipationStatus status) {
        return PartyDetailsResponse.builder()
                .id(party.getId())
                .title(party.getTitle())
                .departureTime(party.getDepartureTime().toString())
                .explanation(party.getExplanation())
                .origin(party.getOrigin())
                .originAddress(party.getOriginAddress())
                .originLocation(party.getOriginLocation())
                .destination(party.getDestination())
                .destinationAddress(party.getDestinationAddress())
                .destinationLocation(party.getDestinationLocation())
                .maxParticipants(party.getMaxParticipants())
                .currentParticipants(party.currentParticipants())
                .views(party.getViews())
                .status(status.name())
                .createdAt(party.getCreatedAt().toString())
                .host(HostResponse.from(user))
                .taxi(TaxiResponse.from(taxi))
                .build();
    }

    record HostResponse(Long id, String nickname, String profileImage, boolean isMe) {

        public static HostResponse from(User user) {
            return new HostResponse(user.getId(), user.getNickname(), user.getProfileImage(), user.isCurrentUser());
        }
    }

    record TaxiResponse(List<Coordinate> route, int fare, int duration) {

        public static TaxiResponse from(Taxi taxi) {
            return new TaxiResponse(taxi.getRoute(), taxi.getFare(), taxi.getDuration());
        }
    }
}
