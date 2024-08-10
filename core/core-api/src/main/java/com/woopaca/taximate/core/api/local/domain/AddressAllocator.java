package com.woopaca.taximate.core.api.local.domain;

import com.woopaca.taximate.core.api.local.api.KakaoLocalClient;
import com.woopaca.taximate.core.api.party.domain.Party;
import org.springframework.stereotype.Component;

@Component
public class AddressAllocator {

    private final KakaoLocalClient kakaoLocalClient;

    public AddressAllocator(KakaoLocalClient kakaoLocalClient) {
        this.kakaoLocalClient = kakaoLocalClient;
    }

    public void allocateAddress(Party party) {
        // TODO 팟 출발지와 도착지 좌표를 이용해 주소 할당
    }
}
