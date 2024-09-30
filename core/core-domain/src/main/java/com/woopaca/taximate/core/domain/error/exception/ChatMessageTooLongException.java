package com.woopaca.taximate.core.domain.error.exception;

import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.error.ErrorType;

public class ChatMessageTooLongException extends BusinessException {

    private static final String MESSAGE = "메시지는 %d자 이하이어야 합니다.";

    public ChatMessageTooLongException() {
        super(String.format(MESSAGE, Chat.MAX_MESSAGE_LENGTH), ErrorType.MESSAGE_TOO_LONG);
    }
}
