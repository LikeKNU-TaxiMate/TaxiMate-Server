package com.woopaca.taximate.core.api.party.controller.dto.response;

import com.woopaca.taximate.core.api.party.domain.Party;
import com.woopaca.taximate.core.api.party.model.Coordinate;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PartiesResponse(
        Long id, String title, LocalDateTime departureTime, String origin,
        String destination, int maxParticipants, int currentParticipants, Coordinate originLocation
) {

    public static PartiesResponse from(Party party) {
        return PartiesResponse.builder()
                .id(party.id())
                .title(party.title())
                .departureTime(party.departureTime())
                .origin(party.origin())
                .destination(party.destination())
                .maxParticipants(party.maxParticipants())
                .currentParticipants(party.currentParticipants())
                .originLocation(party.originLocation())
                .build();
    }
}
