package com.woopaca.taximate.core.api.party.dto.response;

import com.woopaca.taximate.core.domain.party.Party;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ParticipatingPartyResponse(Long id, String title, LocalDateTime departureTime, String origin,
                                         String destination, int maxParticipants, int currentParticipants,
                                         boolean isProgress) {

    public static ParticipatingPartyResponse from(Party party) {
        return ParticipatingPartyResponse.builder()
                .id(party.getId())
                .title(party.getTitle())
                .departureTime(party.getDepartureTime())
                .origin(party.getOrigin())
                .destination(party.getDestination())
                .maxParticipants(party.getMaxParticipants())
                .currentParticipants(party.currentParticipantsCount())
                .isProgress(party.isProgress())
                .build();
    }
}
