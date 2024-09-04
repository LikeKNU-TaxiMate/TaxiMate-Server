package com.woopaca.taximate.core.domain.error.exception;

import com.woopaca.taximate.core.domain.error.ErrorType;

public class InvalidMapRangeException extends BusinessException {

    private static final String MESSAGE = "유효하지 않은 지도 범위입니다. minLatitude: %f, maxLatitude: %f, minLongitude: %f, maxLongitude: %f";

    public InvalidMapRangeException(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
        super(String.format(MESSAGE, minLatitude, maxLatitude, minLongitude, maxLongitude), ErrorType.INVALID_MAP_RANGE);
    }
}
