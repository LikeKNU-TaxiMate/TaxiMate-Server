package com.woopaca.taximate.storage.db.core.repository;

import com.woopaca.taximate.storage.db.core.entity.PartyEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PartyQueryDslRepository {

    List<PartyEntity> findContainsDepartureAfter(double minLatitude, double minLongitude,
                                                 double maxLatitude, double maxLongitude,
                                                 LocalDateTime after);

    Optional<PartyEntity> findByIdForUpdate(Long partyId);
}
