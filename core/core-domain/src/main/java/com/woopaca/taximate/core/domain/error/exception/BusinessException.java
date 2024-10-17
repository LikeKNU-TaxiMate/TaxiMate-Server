package com.woopaca.taximate.core.domain.error.exception;

import com.woopaca.taximate.core.domain.error.ErrorType;
import com.woopaca.taximate.core.domain.error.ErrorType.ErrorHttpStatus;
import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final ErrorType errorType;

    protected BusinessException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorHttpStatus getErrorHttpStatus() {
        ErrorHttpStatus errorHttpStatus = errorType.getErrorHttpStatus();
        if (errorHttpStatus == null) {
            return ErrorHttpStatus.BAD_REQUEST;
        }
        return errorHttpStatus;
    }
}
