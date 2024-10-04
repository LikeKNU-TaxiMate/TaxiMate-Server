package com.woopaca.taximate.core.domain.error.exception;

import com.woopaca.taximate.core.domain.error.ErrorType;

public class NotParticipatedPartyException extends BusinessException {

    private static final String MESSAGE = "참여하지 않은 팟입니다.";

    public NotParticipatedPartyException() {
        super(MESSAGE, ErrorType.NOT_PARTICIPATED_PARTY);
    }
}
