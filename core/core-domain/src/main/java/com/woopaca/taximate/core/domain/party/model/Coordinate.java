package com.woopaca.taximate.core.domain.party.model;

import com.woopaca.taximate.core.domain.error.exception.CoordinateOutOfRangeException;

public record Coordinate(double latitude, double longitude) {

    public static final int LATITUDE_RANGE = 90;
    public static final int LONGITUDE_RANGE = 180;

    public Coordinate {
        validateCoordinateRange(latitude, longitude);
    }

    public static Coordinate of(double latitude, double longitude) {
        return new Coordinate(latitude, longitude);
    }

    private void validateCoordinateRange(double latitude, double longitude) {
        if (latitude <= -LATITUDE_RANGE || latitude >= LATITUDE_RANGE
                || longitude <= -LONGITUDE_RANGE || longitude >= LONGITUDE_RANGE) {
            throw new CoordinateOutOfRangeException(latitude, longitude);
        }
    }
}
