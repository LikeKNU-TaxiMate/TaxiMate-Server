package com.woopaca.taximate.core.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendChat(@NotNull Long partyId, @NotBlank String message) {
}
