package com.woopaca.taximate.core.domain.fixture;

import com.woopaca.taximate.core.domain.local.model.Address;

public final class AddressFixtures {

    public static final String TEST_ROAD_ADDRESS = "도로명";
    public static final String TEST_ADDRESS = "주소";
    public static final String TEST_BUILDING_NAME = "건물명";
    public static final String TEST_REGION = "지역";
    public static final String TEST_MAIN_ADDRESS_NUMBER = "메인주소번호";
    public static final String TEST_SUB_ADDRESS_NUMBER = "서브주소번호";

    private AddressFixtures() {
    }

    public static Address createAddress() {
        return Address.builder()
                .roadAddress(TEST_ROAD_ADDRESS)
                .address(TEST_ADDRESS)
                .buildingName(TEST_BUILDING_NAME)
                .region3DepthName(TEST_REGION)
                .mainAddressNumber(TEST_MAIN_ADDRESS_NUMBER)
                .subAddressNumber(TEST_SUB_ADDRESS_NUMBER)
                .build();
    }
}
