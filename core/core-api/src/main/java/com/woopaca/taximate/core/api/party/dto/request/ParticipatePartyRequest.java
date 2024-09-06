package com.woopaca.taximate.core.api.party.dto.request;

import jakarta.validation.constraints.Positive;

public record ParticipatePartyRequest(@Positive long partyId) {
}
