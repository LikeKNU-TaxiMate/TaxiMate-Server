package com.woopaca.taximate.core.domain.error.exception;

import com.woopaca.taximate.core.domain.error.ErrorType;
import com.woopaca.taximate.core.domain.party.Party;

public class TitleTooLongException extends BusinessException {

    private static final String MESSAGE = "제목은 %d자 이하이어야 합니다.";

    public TitleTooLongException() {
        super(String.format(MESSAGE, Party.MAX_TITLE_LENGTH), ErrorType.TITLE_TOO_LONG);
    }
}
