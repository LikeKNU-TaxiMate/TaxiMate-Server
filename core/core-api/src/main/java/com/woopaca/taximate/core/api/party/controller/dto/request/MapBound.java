package com.woopaca.taximate.core.api.party.controller.dto.request;

import com.woopaca.taximate.core.api.common.error.exception.InvalidMapRangeException;

public record MapBound(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {

    public MapBound {
        validateBound(minLatitude, minLongitude, maxLatitude, maxLongitude);
    }

    private void validateBound(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
        if (minLatitude > maxLatitude || minLongitude > maxLongitude) {
            throw new InvalidMapRangeException(minLatitude, minLongitude, maxLatitude, maxLongitude);
        }
    }
}
