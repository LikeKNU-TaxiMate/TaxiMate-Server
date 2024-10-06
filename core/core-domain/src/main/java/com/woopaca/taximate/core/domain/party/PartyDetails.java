package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.core.domain.taxi.Taxi;
import com.woopaca.taximate.core.domain.user.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PartyDetails {

    private final Party party;
    @EqualsAndHashCode.Exclude
    private final Taxi taxi;
    private final User authenticatedUser;

    public PartyDetails(Party party, Taxi taxi, User authenticatedUser) {
        this.party = party;
        this.taxi = taxi;
        this.authenticatedUser = authenticatedUser;
    }
}
