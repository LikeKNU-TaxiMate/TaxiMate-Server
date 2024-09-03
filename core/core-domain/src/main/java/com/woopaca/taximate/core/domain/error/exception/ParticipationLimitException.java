package com.woopaca.taximate.core.domain.error.exception;

import com.woopaca.taximate.core.domain.error.ErrorType;

public class ParticipationLimitException extends BusinessException {

    private static final String MESSAGE = "팟 참여 제한을 초과하였습니다. 최대 팟 참여 가능 수: %d";

    public ParticipationLimitException(int maxPartiesCount) {
        super(String.format(MESSAGE, maxPartiesCount), ErrorType.PARTICIPATION_LIMIT);
    }
}
