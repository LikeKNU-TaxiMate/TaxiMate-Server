package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.core.api.party.domain.Participation.ParticipationStatus;
import com.woopaca.taximate.core.api.party.model.Coordinate;
import com.woopaca.taximate.core.api.user.domain.User;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Objects;
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
                .views(entity.getViews())
                .createdAt(entity.getCreatedAt())
                .participationSet(participationSet)
                .build();
    }

    public int currentParticipants() {
        return participationSet.size();
    }

    public Long hostId() {
        // TODO 호스트가 존재하지 않는 경우 예외 처리
        return participationSet.stream()
                .filter(Participation::isHost)
                .findAny()
                .map(Participation::userId)
                .orElse(-1L);
    }

    public ParticipationStatus participationStatusOf(User user) {
        return participationSet.stream()
                .filter(participation -> Objects.equals(participation.userId(), user.id()))
                .findAny()
                .map(Participation::status)
                .orElse(ParticipationStatus.NONE);
    }
}
