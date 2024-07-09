package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.core.api.party.model.Coordinate;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PartyMapFinder {

    private final PartyRepository partyRepository;

    public PartyMapFinder(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    public List<Party> findByRange(Coordinate minCoordinate, Coordinate maxCoordinate) {
        // TODO 범위 내에 속하는 Party 리스트 DB 조회
        List<PartyEntity> partyEntities = partyRepository.findContains(
                minCoordinate.latitude(), minCoordinate.longitude(),
                maxCoordinate.latitude(), maxCoordinate.longitude()
        );
        return partyEntities.stream()
                .map(Party::fromEntity)
                .toList();
    }
}
