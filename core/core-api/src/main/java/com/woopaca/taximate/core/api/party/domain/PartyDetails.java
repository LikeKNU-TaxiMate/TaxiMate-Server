package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.core.api.taxi.domain.Taxi;
import com.woopaca.taximate.core.api.user.domain.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PartyDetails {

    private final Party party;
    private final User host;
    @EqualsAndHashCode.Exclude
    private final Taxi taxi;
    private final User authenticatedUser;

    public PartyDetails(Party party, User host, Taxi taxi, User authenticatedUser) {
        this.party = party;
        this.host = host;
        this.taxi = taxi;
        this.authenticatedUser = authenticatedUser;
    }
}
