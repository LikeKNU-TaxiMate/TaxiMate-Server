package com.woopaca.taximate.core.domain.error.exception;

import com.woopaca.taximate.core.domain.error.ErrorType;

public class PartyAlreadyEndedException extends BusinessException {

    private static final String MESSAGE = "팟이 이미 종료되었습니다. partyId: %d";

    public PartyAlreadyEndedException(Long partyId) {
        super(String.format(MESSAGE, partyId), ErrorType.PARTY_ALREADY_ENDED);
    }
}
