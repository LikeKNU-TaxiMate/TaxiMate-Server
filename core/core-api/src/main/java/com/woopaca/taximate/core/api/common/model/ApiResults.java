package com.woopaca.taximate.core.api.common.model;

import com.woopaca.taximate.core.api.common.error.ErrorType;

public record ApiResults() {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, null, data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static ErrorResponse error(ErrorType errorType) {
        return error(errorType.getMessage(), errorType.getErrorCode());
    }

    public static ErrorResponse error(String message, String errorCode) {
        return new ErrorResponse(false, message, errorCode);
    }

    public record ApiResponse<T>(boolean success, String message, T data) {
    }

    public record ErrorResponse(boolean success, String message, String errorCode) {
    }

}
