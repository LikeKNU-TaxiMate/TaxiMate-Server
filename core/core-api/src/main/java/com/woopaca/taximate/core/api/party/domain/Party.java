package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.core.api.party.model.Coordinate;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record Party(Long id, String title, LocalDateTime departureTime, String origin, String destination,
                    Coordinate originLocation, int maxPassengers, Set<Participation> participationSet) {

    public static Party fromEntity(PartyEntity entity) {
        Set<Participation> participationSet = entity.getParticipationSet()
                .stream()
                .map(Participation::fromEntity)
                .collect(Collectors.toSet());
        return Party.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .departureTime(entity.getDepartureTime())
                .origin(entity.getOrigin())
                .destination(entity.getDestination())
                .originLocation(Coordinate.of(
                        entity.getOriginLatitude(),
                        entity.getOriginLongitude()
                ))
                .maxPassengers(entity.getMaxPassengers())
                .participationSet(participationSet)
                .build();
    }

    public int currentParticipants() {
        return participationSet.size();
    }
}
