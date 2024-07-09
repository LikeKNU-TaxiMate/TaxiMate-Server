package com.woopaca.taximate.core.api.party.service;

import com.woopaca.taximate.core.api.party.controller.dto.request.MapBound;
import com.woopaca.taximate.core.api.party.domain.Parties;
import com.woopaca.taximate.core.api.party.domain.Party;
import com.woopaca.taximate.core.api.party.domain.PartyMapFinder;
import com.woopaca.taximate.core.api.party.model.Coordinate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartyService {

    private final PartyMapFinder partyMapFinder;

    PartyService(PartyMapFinder partyMapFinder) {
        this.partyMapFinder = partyMapFinder;
    }

    public Parties getPartiesInRange(MapBound mapBound) {
        List<Party> partiesInRange = partyMapFinder.findByRange(
                Coordinate.of(mapBound.minLatitude(), mapBound.minLongitude()),
                Coordinate.of(mapBound.maxLatitude(), mapBound.maxLongitude())
        );
        return new Parties(partiesInRange);
    }
}
