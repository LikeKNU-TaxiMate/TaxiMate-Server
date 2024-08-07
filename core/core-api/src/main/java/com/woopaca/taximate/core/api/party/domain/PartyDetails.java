package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.core.api.taxi.domain.Taxi;
import com.woopaca.taximate.core.api.user.domain.User;

public record PartyDetails(Party party, User host, Taxi taxi, User authenticatedUser) {
}
