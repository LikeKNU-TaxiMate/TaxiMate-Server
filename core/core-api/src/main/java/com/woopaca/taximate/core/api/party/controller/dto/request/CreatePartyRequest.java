package com.woopaca.taximate.core.api.party.controller.dto.request;

import com.woopaca.taximate.core.api.party.model.Coordinate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreatePartyRequest(@NotBlank String title, @NotNull LocalDateTime departureTime,
                                 @NotNull Coordinate originLocation, @NotNull Coordinate destinationLocation,
                                 int maxParticipants, String explanation) {
}
