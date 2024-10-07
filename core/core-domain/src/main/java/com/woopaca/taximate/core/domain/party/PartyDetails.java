package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.core.domain.taxi.Taxi;
import com.woopaca.taximate.core.domain.user.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@EqualsAndHashCode
public class PartyDetails {

    private final Party party;
    private final List<Participation> participationList;
    @EqualsAndHashCode.Exclude
    private final Taxi taxi;
    private final User authenticatedUser;

    public PartyDetails(Party party, List<Participation> participationList, Taxi taxi, User authenticatedUser) {
        this.party = party;
        this.participationList = participationList;
        this.taxi = taxi;
        this.authenticatedUser = authenticatedUser;
    }
}
