package com.woopaca.taximate.core.api.common.error.exception;

import com.woopaca.taximate.core.api.common.error.ErrorType;

import java.time.LocalDateTime;

public class PastDepartureTimeException extends BusinessException {

    private static final String MESSAGE = "출발 시간이 현재 시간보다 이전일 수 없습니다. 시간: %s";

    public PastDepartureTimeException(LocalDateTime departureTime) {
        super(String.format(MESSAGE, departureTime), ErrorType.PAST_DEPARTURE_TIME);
    }
}
