package com.woopaca.taximate.core.api.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    // TODO error code 상세 설정 및 오류 형식별 type 분리하기
    COORDINATE_OUT_OF_RANGE("", HttpStatus.BAD_REQUEST),
    INVALID_MAP_RANGE("", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final HttpStatus httpStatus;

    ErrorType(String errorCode, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
