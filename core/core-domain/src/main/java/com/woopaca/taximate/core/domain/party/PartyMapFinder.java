package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.core.domain.party.model.Coordinate;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PartyMapFinder {

    private final PartyRepository partyRepository;

    public PartyMapFinder(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    public List<Party> findByRangeAndDateTime(Coordinate minCoordinate, Coordinate maxCoordinate, LocalDateTime dateTime) {
        List<PartyEntity> partyEntities = partyRepository.findContainsDepartureAfter(
                minCoordinate.latitude(), minCoordinate.longitude(),
                maxCoordinate.latitude(), maxCoordinate.longitude(),
                dateTime
        );
        return partyEntities.stream()
                .map(Party::fromEntity)
                .toList();
    }
}
