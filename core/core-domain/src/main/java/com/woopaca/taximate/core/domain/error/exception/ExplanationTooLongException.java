package com.woopaca.taximate.core.domain.error.exception;

import com.woopaca.taximate.core.domain.error.ErrorType;

public class ExplanationTooLongException extends BusinessException {

    private static final String MESSAGE = "설명은 %d자 이하이어야 합니다.";

    public ExplanationTooLongException(int maxLength) {
        super(String.format(MESSAGE, maxLength), ErrorType.EXPLANATION_TOO_LONG);
    }
}
