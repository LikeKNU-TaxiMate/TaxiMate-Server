package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.core.domain.party.model.Coordinate;
import com.woopaca.taximate.core.domain.taxi.Taxi;

public interface KakaoMobilityClientProxy {

    Taxi requestTaxi(Coordinate origin, Coordinate destination);
}
