package com.woopaca.taximate.core.domain.error.exception;

import com.woopaca.taximate.core.domain.error.ErrorType;
import com.woopaca.taximate.core.domain.party.Participation;

public class ParticipationLimitException extends BusinessException {

    private static final String MESSAGE = "팟 참여 제한을 초과하였습니다. 최대 팟 참여 가능 수: %d";

    public ParticipationLimitException() {
        super(String.format(MESSAGE, Participation.MAX_PARTICIPATING_PARTIES_COUNT), ErrorType.PARTICIPATION_LIMIT);
    }
}
