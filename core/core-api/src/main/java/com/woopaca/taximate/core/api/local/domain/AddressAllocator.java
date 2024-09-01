package com.woopaca.taximate.core.api.local.domain;

import com.woopaca.taximate.core.api.local.api.KakaoLocalClient;
import com.woopaca.taximate.core.api.party.domain.Party;
import com.woopaca.taximate.core.api.party.model.Coordinate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.concurrent.CompletionException;

@Slf4j
@Component
public class AddressAllocator {

    private final KakaoLocalClient kakaoLocalClient;

    public AddressAllocator(KakaoLocalClient kakaoLocalClient) {
        this.kakaoLocalClient = kakaoLocalClient;
    }

    public void allocateAddress(Party party) {
        Coordinate originLocation = party.getOriginLocation();
        Coordinate destinationLocation = party.getDestinationLocation();
        try {
            kakaoLocalClient.requestConvertCoordinateAsynchronous(originLocation)
                    .thenCombine(kakaoLocalClient.requestConvertCoordinateAsynchronous(destinationLocation), party::allocateAddress)
                    .join();
        } catch (CompletionException exception) {
            throw (HttpStatusCodeException) exception.getCause();
        }
    }
}
