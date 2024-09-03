package com.woopaca.taximate.core.api.local.api;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.woopaca.taximate.core.domain.error.exception.NonexistentAddressException;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

public record KakaoAddress(List<Document> documents) {

    public KakaoAddress {
        if (ObjectUtils.isEmpty(documents)) {
            throw new NonexistentAddressException();
        }
    }

    public String roadAddress() {
        Document document = documents.get(0);
        RoadAddress roadAddress = document.roadAddress;
        if (Objects.isNull(roadAddress)) {
            return null;
        }
        return roadAddress.addressName;
    }

    public String address() {
        Document document = documents.get(0);
        Address address = document.address;
        return address.addressName;
    }

    public String buildingName() {
        Document document = documents.get(0);
        RoadAddress roadAddress = document.roadAddress;
        if (Objects.isNull(roadAddress)) {
            return null;
        }
        return roadAddress.buildingName;
    }

    public String region3DepthName() {
        Document document = documents.get(0);
        Address address = document.address;
        return address.region3DepthName;
    }

    public String mainAddressNumber() {
        Document document = documents.get(0);
        Address address = document.address;
        return address.mainAddressNo;
    }

    public String subAddressNumber() {
        Document document = documents.get(0);
        Address address = document.address;
        return address.subAddressNo;
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    record Document(RoadAddress roadAddress, Address address) {
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    record RoadAddress(String addressName, String roadName, String mainBuildingNo, String subBuildingNo,
                       String buildingName) {
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    record Address(String addressName, @JsonAlias("region_3depth_name") String region3DepthName, String mainAddressNo,
                   String subAddressNo) {
    }
}
