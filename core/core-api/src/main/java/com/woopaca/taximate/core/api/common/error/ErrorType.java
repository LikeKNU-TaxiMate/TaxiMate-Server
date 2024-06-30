package com.woopaca.taximate.core.api.common.error;

import lombok.Getter;

@Getter
public enum ErrorType {

    ;

    private final String message;
    private final String errorCode;

    ErrorType(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

}
