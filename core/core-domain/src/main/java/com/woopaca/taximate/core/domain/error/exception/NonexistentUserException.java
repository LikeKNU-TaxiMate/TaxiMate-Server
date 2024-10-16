package com.woopaca.taximate.core.domain.error.exception;

import com.woopaca.taximate.core.domain.error.ErrorType;

public class NonexistentUserException extends BusinessException {

    private static final String MESSAGE = "존재하지 않는 사용자입니다.";

    public NonexistentUserException() {
        super(MESSAGE, ErrorType.NONEXISTENT_USER);
    }
}
