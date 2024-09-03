package com.woopaca.taximate.core.domain.error.exception;

import com.woopaca.taximate.core.domain.error.ErrorType;

public class ParticipantsFullException extends BusinessException {

    private static final String MESSAGE = "참여 인원이 가득 찼습니다. 최대 참여 인원: %d명 partyId: %d";

    public ParticipantsFullException(int maxParticipants, Long partyId) {
        super(String.format(MESSAGE, maxParticipants, partyId), ErrorType.PARTICIPANTS_FULL);
    }
}
