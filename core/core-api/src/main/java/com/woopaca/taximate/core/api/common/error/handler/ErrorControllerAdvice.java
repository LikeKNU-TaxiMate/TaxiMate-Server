package com.woopaca.taximate.core.api.common.error.handler;

import com.woopaca.taximate.core.api.common.error.exception.BusinessException;
import com.woopaca.taximate.core.api.common.model.ApiResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.woopaca.taximate.core.api.common.model.ApiResults.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        String message = exception.getMessage();
        log.warn(message, exception);
        ErrorResponse errorResponse = ApiResults.error(exception);
        return ResponseEntity.status(exception.getHttpStatus())
                .body(errorResponse);
    }
}
