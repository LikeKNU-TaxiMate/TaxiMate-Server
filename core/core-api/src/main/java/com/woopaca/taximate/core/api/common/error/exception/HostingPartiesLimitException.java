package com.woopaca.taximate.core.api.common.error.exception;

import com.woopaca.taximate.core.api.common.error.ErrorType;
import com.woopaca.taximate.core.api.party.domain.Party;

public class HostingPartiesLimitException extends BusinessException {

    private static final String MESSAGE = "팟 개설 제한을 초과하였습니다. 최대 팟 개설 가능 수: %d";

    public HostingPartiesLimitException() {
        super(String.format(MESSAGE, Party.MAX_PARTIES_COUNT), ErrorType.HOSTING_PARTIES_LIMIT);
    }
}
