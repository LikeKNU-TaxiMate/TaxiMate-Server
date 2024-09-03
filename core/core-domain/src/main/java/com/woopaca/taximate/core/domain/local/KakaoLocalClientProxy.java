package com.woopaca.taximate.core.domain.local;

import com.woopaca.taximate.core.domain.local.model.Address;
import com.woopaca.taximate.core.domain.party.model.Coordinate;

import java.util.concurrent.CompletableFuture;

public interface KakaoLocalClientProxy {

    CompletableFuture<Address> requestConvertCoordinateAsynchronous(Coordinate coordinate);
}
