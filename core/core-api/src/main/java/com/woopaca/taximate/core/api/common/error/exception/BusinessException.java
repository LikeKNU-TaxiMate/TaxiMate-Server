package com.woopaca.taximate.core.api.common.error.exception;

import com.woopaca.taximate.core.api.common.error.ErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final ErrorType errorType;

    protected BusinessException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public HttpStatus getHttpStatus() {
        HttpStatus httpStatus = errorType.getHttpStatus();
        if (httpStatus == null) {
            return HttpStatus.BAD_REQUEST;
        }
        return httpStatus;
    }
}
