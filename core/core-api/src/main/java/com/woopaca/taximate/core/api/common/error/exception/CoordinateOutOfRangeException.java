package com.woopaca.taximate.core.api.common.error.exception;

import com.woopaca.taximate.core.api.common.error.ErrorType;

public class CoordinateOutOfRangeException extends BusinessException {

    private static final String MESSAGE = "범위를 벗어난 좌표값입니다. latitude: %f, longitude: %f";

    public CoordinateOutOfRangeException(double latitude, double longitude) {
        super(String.format(MESSAGE, latitude, longitude), ErrorType.COORDINATE_OUT_OF_RANGE);
    }
}
