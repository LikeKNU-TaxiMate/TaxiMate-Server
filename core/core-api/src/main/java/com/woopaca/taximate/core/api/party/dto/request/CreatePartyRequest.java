package com.woopaca.taximate.core.api.party.dto.request;

import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.model.Coordinate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreatePartyRequest(@NotBlank String title, @NotNull LocalDateTime departureTime,
                                 @NotNull Coordinate originLocation, @NotNull Coordinate destinationLocation,
                                 int maxParticipants, String explanation) {

    public Party toDomain() {
        return Party.builder()
                .title(this.title)
                .explanation(this.explanation)
                .departureTime(this.departureTime)
                .originLocation(this.originLocation)
                .destinationLocation(this.destinationLocation)
                .maxParticipants(this.maxParticipants)
                .build();
    }
}
