package com.woopaca.taximate.core.message.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReceivedChat(@Positive @NotNull Long partyId, @Positive @NotNull Long chatId) {
}
