package com.woopaca.taximate.core.api.party.controller.dto.request;

import com.woopaca.taximate.core.api.common.error.exception.InvalidMapRangeException;

public record MapRange(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {

    public MapRange {
        validateMapRange(minLatitude, minLongitude, maxLatitude, maxLongitude);
    }

    private void validateMapRange(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
        if (minLatitude > maxLatitude || minLongitude > maxLongitude) {
            throw new InvalidMapRangeException(minLatitude, minLongitude, maxLatitude, maxLongitude);
        }
    }

}
