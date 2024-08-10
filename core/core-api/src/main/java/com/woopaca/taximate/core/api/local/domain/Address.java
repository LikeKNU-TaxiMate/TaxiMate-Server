package com.woopaca.taximate.core.api.local.domain;

import org.springframework.util.StringUtils;

import java.util.Objects;

public record Address(String roadAddress, String address, String buildingName, String region3DepthName,
                      String mainAddressNumber, String subAddressNumber) {

    public static Address empty() {
        return new Address(null, null, null, null, null, null);
    }

    public String fullAddress() {
        return Objects.requireNonNullElse(roadAddress, address);
    }

    public String name() {
        return Objects.requireNonNullElse(buildingName, buildName());
    }

    private String buildName() {
        StringBuilder name = new StringBuilder(region3DepthName);
        if (StringUtils.hasText(mainAddressNumber)) {
            name.append(" ").append(mainAddressNumber);
        }
        if (StringUtils.hasText(subAddressNumber)) {
            name.append("-").append(subAddressNumber);
        }
        return name.toString();
    }
}
