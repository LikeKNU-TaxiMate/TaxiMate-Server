package com.woopaca.taximate.core.api.party.controller.dto.response;

import com.woopaca.taximate.core.api.party.domain.Party;
import com.woopaca.taximate.core.api.party.model.Coordinate;
import lombok.Builder;

@Builder
public record PartiesResponse(
        Long id, String title, String departureTime, String origin,
        String destination, int maxParticipants, int currentParticipants, Coordinate originLocation
) {

    public static PartiesResponse from(Party party) {
        return PartiesResponse.builder()
                .id(party.getId())
                .title(party.getTitle())
                .departureTime(party.getDepartureTime().toString())
                .origin(party.getOrigin())
                .destination(party.getDestination())
                .maxParticipants(party.getMaxParticipants())
                .currentParticipants(party.currentParticipants())
                .originLocation(party.getOriginLocation())
                .build();
    }
}
