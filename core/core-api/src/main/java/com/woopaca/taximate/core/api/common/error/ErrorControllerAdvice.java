package com.woopaca.taximate.core.api.common.error;

import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.domain.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.concurrent.CompletionException;

import static com.woopaca.taximate.core.api.common.model.ApiResults.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ErrorControllerAdvice {

    /**
     * 비즈니스 예외 처리
     * @param exception {@link BusinessException} 예외
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        String message = exception.getMessage();
        log.warn(message, exception);
        ErrorResponse errorResponse = ApiResults.error(exception);
        return ResponseEntity.status(exception.getErrorHttpStatus().getValue())
                .body(errorResponse);
    }

    /**
     * 요청 파라미터 누락 예외 처리
     * @param exception {@link MissingServletRequestParameterException} 예외
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        String message = exception.getMessage();
        log.warn(message, exception);
        ErrorResponse errorResponse = ApiResults.error(message, "");
        return ResponseEntity.badRequest()
                .body(errorResponse);
    }

    /**
     * HTTP 메시지 변환 예외 처리
     * @param exception {@link HttpMessageConversionException} 예외
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageConversionException(HttpMessageConversionException exception) {
        String message = exception.getMessage();
        log.warn(message, exception);
        ErrorResponse errorResponse = ApiResults.error(message, "");
        return ResponseEntity.badRequest()
                .body(errorResponse);
    }

    /**
     * 잘못된 인자 예외 처리
     * @param exception {@link IllegalArgumentException} 예외
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        String message = exception.getMessage();
        log.warn(message, exception);
        ErrorResponse errorResponse = ApiResults.error(message, "");
        return ResponseEntity.badRequest()
                .body(errorResponse);
    }

    /**
     * 잘못된 상태 예외 처리
     * @param exception {@link IllegalStateException} 예외
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException exception) {
        String message = exception.getMessage();
        log.warn(message, exception);
        ErrorResponse errorResponse = ApiResults.error(message, "");
        return ResponseEntity.badRequest()
                .body(errorResponse);
    }

    /**
     * 바인딩 예외 처리
     * @param exception {@link BindException} 예외
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException exception) {
        String message = exception.getMessage();
        log.warn(message, exception);
        ErrorResponse errorResponse = ApiResults.error(message, "");
        return ResponseEntity.badRequest()
                .body(errorResponse);
    }

    /**
     * 메소드 인자 타입 불일치 예외 처리
     * @param exception {@link MethodArgumentTypeMismatchException} 예외
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        String message = exception.getMessage();
        String name = exception.getName();
        log.warn(message, exception);
        ErrorResponse errorResponse = ApiResults.error(String.join(": ", message, name), "");
        return ResponseEntity.badRequest()
                .body(errorResponse);
    }

    /**
     * 요청 쿠키 누락 예외 처리
     * @param exception {@link MissingRequestCookieException} 예외
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestCookieException(MissingRequestCookieException exception) {
        String message = exception.getMessage();
        log.warn(message, exception);
        ErrorResponse errorResponse = ApiResults.error(message, "");
        return ResponseEntity.badRequest()
                .body(errorResponse);
    }

    /**
     * HTTP 상태 코드 예외 처리
     * @param exception {@link HttpStatusCodeException} 예외
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<ErrorResponse> handleHttpStatusCodeException(HttpStatusCodeException exception) {
        String message = exception.getMessage();
        log.warn(message, exception);
        ErrorResponse errorResponse = ApiResults.error(message, "");
        return ResponseEntity.status(exception.getStatusCode())
                .body(errorResponse);
    }

    /**
     * CompletableFuture 예외 처리
     * @param exception {@link CompletionException} 예외
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(CompletionException.class)
    public ResponseEntity<ErrorResponse> handleCompletionException(CompletionException exception) {
        Throwable cause = exception.getCause();
        if (cause instanceof BusinessException) {
            return handleBusinessException((BusinessException) cause);
        }
        if (cause instanceof HttpStatusCodeException) {
            return handleHttpStatusCodeException((HttpStatusCodeException) cause);
        }
        if (cause instanceof RestClientException) {
            return handleException((RestClientException) cause);
        }
        return handleException(exception);
    }

    /**
     * 핸들러 없음 예외 처리
     * @param exception {@link NoHandlerFoundException} 예외
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException exception) {
        String message = exception.getMessage();
        log.warn(message, exception);
        ErrorResponse errorResponse = ApiResults.error(message, "");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    /**
     * 리소스 없음 예외 처리
     * @param exception {@link NoResourceFoundException} 예외
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException exception) {
        String message = exception.getMessage();
        log.warn(message, exception);
        ErrorResponse errorResponse = ApiResults.error(message, "");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    /**
     * 예외 처리
     * @param exception {@link Exception} 예외
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        String message = exception.getMessage();
        log.error(message, exception);
        ErrorResponse errorResponse = ApiResults.error(message, "");
        return ResponseEntity.internalServerError()
                .body(errorResponse);
    }
}
