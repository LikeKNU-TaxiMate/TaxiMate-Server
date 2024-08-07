package com.woopaca.taximate.core.api.common.error.exception;

import com.woopaca.taximate.core.api.common.error.ErrorType;

public class NonexistentUserException extends BusinessException {

    private static final String MESSAGE = "존재하지 않는 사용자입니다. userId: %d";

    public NonexistentUserException(Long userId) {
        super(String.format(MESSAGE, userId), ErrorType.NONEXISTENT_USER);
    }
}
