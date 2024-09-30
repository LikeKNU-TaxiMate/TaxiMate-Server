package com.woopaca.taximate.core.domain.error.exception;

import com.woopaca.taximate.core.domain.error.ErrorType;
import com.woopaca.taximate.core.domain.party.Party;

public class ExplanationTooLongException extends BusinessException {

    private static final String MESSAGE = "설명은 %d자 이하이어야 합니다.";

    public ExplanationTooLongException() {
        super(String.format(MESSAGE, Party.MAX_EXPLANATION_LENGTH), ErrorType.EXPLANATION_TOO_LONG);
    }
}
