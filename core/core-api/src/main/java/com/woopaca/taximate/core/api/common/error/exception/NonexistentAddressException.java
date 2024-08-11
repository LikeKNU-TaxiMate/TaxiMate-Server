package com.woopaca.taximate.core.api.common.error.exception;

import com.woopaca.taximate.core.api.common.error.ErrorType;

public class NonexistentAddressException extends BusinessException {

    private static final String MESSAGE = "주소가 존재하지 않는 좌표입니다.";

    public NonexistentAddressException() {
        super(MESSAGE, ErrorType.NONEXISTENT_ADDRESS);
    }
}
