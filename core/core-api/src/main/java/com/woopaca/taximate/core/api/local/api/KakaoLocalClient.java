package com.woopaca.taximate.core.api.local.api;

import com.woopaca.taximate.core.api.local.domain.Address;
import com.woopaca.taximate.core.api.party.model.Coordinate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class KakaoLocalClient {

    private final RestClient restClient;
    private final KakaoLocalProperties kakaoLocalProperties;

    public KakaoLocalClient(KakaoLocalProperties kakaoLocalProperties, RestClient restClient) {
        this.kakaoLocalProperties = kakaoLocalProperties;
        this.restClient = restClient;
    }

    public Address requestConvertCoordinate(Coordinate coordinate) {
        try {
            KakaoAddress address = restClient.get()
                    .uri(UriComponentsBuilder.fromUriString(kakaoLocalProperties.getCoordinateToAddressUrl())
                            .queryParam("x", coordinate.longitude())
                            .queryParam("y", coordinate.latitude())
                            .build()
                            .toUri())
                    .header("Authorization", "KakaoAK " + kakaoLocalProperties.getApiKey())
                    .retrieve()
                    .body(KakaoAddress.class);
            if (address == null) {
                return Address.empty();
            }

            return new Address(address.roadAddress(), address.address(), address.buildingName(),
                    address.region3DepthName(), address.mainAddressNumber(), address.subAddressNumber());
        } catch (HttpStatusCodeException exception) {
            log.warn("카카오 로컬 좌표->주소 변환 요청 오류", exception);
            //TODO Handle kakao local request failure
            throw exception;
        }
    }
}
