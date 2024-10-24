package com.woopaca.taximate.core.domain.local.model;

import lombok.Builder;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Builder
public record Address(String roadAddress, String address, String buildingName, String region3DepthName,
                      String mainAddressNumber, String subAddressNumber) {

    public static Address empty() {
        return new Address(null, null, null, null, null, null);
    }

    public String fullAddress() {
        return Objects.requireNonNullElse(roadAddress, address);
    }

    public String name() {
        if (StringUtils.hasText(buildingName)) {
            return buildingName;
        }
        return buildName();
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
