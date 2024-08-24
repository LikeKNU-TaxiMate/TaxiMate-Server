package com.woopaca.taximate.core.api.local.domain;

import com.woopaca.taximate.core.api.local.api.KakaoLocalClient;
import com.woopaca.taximate.core.api.local.model.Address;
import com.woopaca.taximate.core.api.party.domain.Party;
import com.woopaca.taximate.core.api.party.model.Coordinate;
import org.springframework.stereotype.Component;

@Component
public class AddressAllocator {

    private final KakaoLocalClient kakaoLocalClient;

    public AddressAllocator(KakaoLocalClient kakaoLocalClient) {
        this.kakaoLocalClient = kakaoLocalClient;
    }

    public Party allocateAddress(Party party) {
        Coordinate originLocation = party.getOriginLocation();
        Coordinate destinationLocation = party.getDestinationLocation();
        Address originAddress = kakaoLocalClient.requestConvertCoordinate(originLocation);
        Address destinationAddress = kakaoLocalClient.requestConvertCoordinate(destinationLocation);
        return party.allocateAddress(originAddress, destinationAddress);
    }
}
