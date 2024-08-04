package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.core.api.party.model.Coordinate;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record Party(Long id, String title, String explanation, LocalDateTime departureTime,
                    String origin, String originAddress, Coordinate originLocation,
                    String destination, String destinationAddress, Coordinate destinationLocation,
                    int maxParticipants, int views, LocalDateTime createdAt, Set<Participation> participationSet) {

    public static Party fromEntity(PartyEntity entity) {
        Set<Participation> participationSet = entity.getParticipationSet()
                .stream()
                .map(Participation::fromEntity)
                .collect(Collectors.toSet());
        return Party.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .explanation(entity.getExplanation())
                .departureTime(entity.getDepartureTime())
                .origin(entity.getOrigin())
                .originAddress(entity.getOriginAddress())
                .originLocation(Coordinate.of(
                        entity.getOriginLatitude(),
                        entity.getOriginLongitude()
                ))
                .destination(entity.getDestination())
                .destinationAddress(entity.getDestinationAddress())
                .destinationLocation(Coordinate.of(
                        entity.getDestinationLatitude(),
                        entity.getDestinationLongitude()
                ))
                .maxParticipants(entity.getMaxParticipants())
                .participationSet(participationSet)
                .build();
    }

    public int currentParticipants() {
        return participationSet.size();
    }
}
