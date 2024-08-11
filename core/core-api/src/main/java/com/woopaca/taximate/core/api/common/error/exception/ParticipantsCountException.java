package com.woopaca.taximate.core.api.common.error.exception;

import com.woopaca.taximate.core.api.common.error.ErrorType;
import com.woopaca.taximate.core.api.party.domain.Party;

public class ParticipantsCountException extends BusinessException {

    private static final String MESSAGE = "설정 가능한 팟 참여 최대 인원은 %d ~ %d명입니다. 현재: %d";

    public ParticipantsCountException(int maxParticipants) {
        super(String.format(MESSAGE, Party.MIN_PARTICIPANTS_COUNT, Party.MAX_PARTICIPANTS_COUNT, maxParticipants),
                ErrorType.PARTICIPANTS_COUNT);
    }
}
