package com.woopaca.taximate.core.domain.local;

import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.model.Coordinate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AddressAllocator {

    private final KakaoLocalClientProxy kakaoLocalClient;

    public AddressAllocator(KakaoLocalClientProxy kakaoLocalClient) {
        this.kakaoLocalClient = kakaoLocalClient;
    }

    public void allocateAddress(Party party) {
        Coordinate originLocation = party.getOriginLocation();
        Coordinate destinationLocation = party.getDestinationLocation();
        kakaoLocalClient.requestConvertCoordinate(originLocation)
                .thenCombine(kakaoLocalClient.requestConvertCoordinate(destinationLocation), party::allocateAddress)
                .join();
    }
}