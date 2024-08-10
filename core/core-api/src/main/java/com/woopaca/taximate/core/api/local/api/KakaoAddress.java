package com.woopaca.taximate.core.api.local.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoAddress(List<Document> documents) {

    public String roadAddress() {
        Document document = documents.get(0);
        RoadAddress roadAddress = document.roadAddress();
        return roadAddress.addressName;
    }

    public String address() {
        Document document = documents.get(0);
        Address address = document.address();
        return address.addressName;
    }

    public String buildingName() {
        Document document = documents.get(0);
        RoadAddress roadAddress = document.roadAddress();
        return roadAddress.buildingName;
    }

    public String region3DepthName() {
        Document document = documents.get(0);
        RoadAddress roadAddress = document.roadAddress();
        return roadAddress.region3DepthName;
    }

    public String mainAddressNumber() {
        Document document = documents.get(0);
        Address address = document.address();
        return address.mainAddressNo;
    }

    public String subAddressNumber() {
        Document document = documents.get(0);
        Address address = document.address();
        return address.subAddressNo;
    }

    record Document(RoadAddress roadAddress, Address address) {
    }

    record RoadAddress(String addressName, String region1DepthName, String region2DepthName, String region3DepthName,
                       String roadName, String undergroundYn, String mainBuildingNo, String subBuildingNo,
                       String buildingName, String zoneNo) {
    }

    record Address(String addressName, String region1DepthName, String region2DepthName, String region3DepthName,
                   String mountainYn, String mainAddressNo, String subAddressNo, String zipCode) {
    }
}
