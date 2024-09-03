package com.woopaca.taximate.core.domain.error.exception;

import com.woopaca.taximate.core.domain.error.ErrorType;

public class NonexistentPartyException extends BusinessException {

    private static final String MESSAGE = "존재하지 않는 팟입니다. partyId: %d";

    public NonexistentPartyException(Long partyId) {
        super(String.format(MESSAGE, partyId), ErrorType.NONEXISTENT_PARTY);
    }
}
