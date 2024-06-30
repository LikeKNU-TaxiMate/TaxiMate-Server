package com.woopaca.taximate.core.api.party.controller.dto.response;

import com.woopaca.taximate.core.api.party.model.Coordinate;

import java.time.LocalDateTime;

public record PartiesResponse(
        Long id, String title, LocalDateTime departureTime, String origin,
        String destination, int maxParticipants, int currentParticipants, Coordinate originLocation
) {
}
