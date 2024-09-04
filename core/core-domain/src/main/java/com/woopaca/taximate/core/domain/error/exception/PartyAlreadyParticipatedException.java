package com.woopaca.taximate.core.domain.error.exception;

import com.woopaca.taximate.core.domain.error.ErrorType;

public class PartyAlreadyParticipatedException extends BusinessException {

    private static final String MESSAGE = "이미 참여한 팟입니다. partyId: %d, userId: %d";

    public PartyAlreadyParticipatedException(Long partyId, Long userId) {
        super(String.format(MESSAGE, partyId, userId), ErrorType.PARTY_ALREADY_PARTICIPATED);
    }
}
