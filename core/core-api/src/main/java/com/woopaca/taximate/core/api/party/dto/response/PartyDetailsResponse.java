package com.woopaca.taximate.core.api.party.dto.response;

import com.woopaca.taximate.core.domain.party.Participation;
import com.woopaca.taximate.core.domain.party.Participation.ParticipationRole;
import com.woopaca.taximate.core.domain.party.Participation.ParticipationStatus;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.PartyDetails;
import com.woopaca.taximate.core.domain.party.model.Coordinate;
import com.woopaca.taximate.core.domain.taxi.Taxi;
import com.woopaca.taximate.core.domain.user.User;
import lombok.Builder;

import java.util.Collection;
import java.util.List;

@Builder
public record PartyDetailsResponse(Long id, String title, String departureTime, String explanation,
                                   String origin, String originAddress, Coordinate originLocation,
                                   String destination, String destinationAddress, Coordinate destinationLocation,
                                   int maxParticipants, int currentParticipants, int views, String status,
                                   String createdAt, List<ParticipantResponse> participants, TaxiResponse taxi) {

    public static PartyDetailsResponse from(PartyDetails partyDetails) {
        Party party = partyDetails.getParty();
        ParticipationStatus status = party
                .participationStatusOf(partyDetails.getAuthenticatedUser());
        return PartyDetailsResponse.of(party, party.getParticipationSet(), partyDetails.getTaxi(), status);
    }

    public static PartyDetailsResponse of(Party party, Collection<Participation> participationList, Taxi taxi, ParticipationStatus status) {
        List<ParticipantResponse> participantResponses = participationList.stream()
                .map(ParticipantResponse::from)
                .toList();
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
                .currentParticipants(party.currentParticipantsCount())
                .views(party.getViews())
                .status(status.name())
                .createdAt(party.getCreatedAt().toString())
                .participants(participantResponses)
                .taxi(TaxiResponse.from(taxi))
                .build();
    }

    record ParticipantResponse(Long id, String nickname, String profileImage, ParticipationRole role) {

        public static ParticipantResponse from(Participation participation) {
            User user = participation.getUser();
            ParticipationRole role = participation.getRole();
            return new ParticipantResponse(user.getId(), user.getNickname(), user.getProfileImage(), role);
        }
    }

    record TaxiResponse(List<Coordinate> route, int fare, int duration) {

        public static TaxiResponse from(Taxi taxi) {
            return new TaxiResponse(taxi.getRoute(), taxi.getFare(), taxi.getDuration());
        }
    }
}
