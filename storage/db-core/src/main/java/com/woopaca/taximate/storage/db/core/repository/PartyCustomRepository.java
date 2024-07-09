package com.woopaca.taximate.storage.db.core.repository;

import com.woopaca.taximate.storage.db.core.entity.PartyEntity;

import java.util.List;

public interface PartyCustomRepository {

    List<PartyEntity> findContains(double minLatitude, double minLongitude,
                                   double maxLatitude, double maxLongitude);
}
