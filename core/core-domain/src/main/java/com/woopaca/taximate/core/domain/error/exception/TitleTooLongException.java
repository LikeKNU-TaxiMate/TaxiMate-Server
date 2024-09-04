package com.woopaca.taximate.core.domain.error.exception;

import com.woopaca.taximate.core.domain.error.ErrorType;

public class TitleTooLongException extends BusinessException {

    private static final String MESSAGE = "제목은 %d자 이하이어야 합니다.";

    public TitleTooLongException(int maxLength) {
        super(String.format(MESSAGE, maxLength), ErrorType.TITLE_TOO_LONG);
    }
}
