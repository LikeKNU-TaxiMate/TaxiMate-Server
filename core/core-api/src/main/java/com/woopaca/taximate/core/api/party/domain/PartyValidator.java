package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.core.api.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PartyValidator {

    private final PartyFinder partyFinder;

    public PartyValidator(PartyFinder partyFinder) {
        this.partyFinder = partyFinder;
    }

    public void validateMaxPartiesCount(User user) {
        List<Party> hostingParties = partyFinder.findHostingParties(user);
        if (hostingParties.size() >= Party.MAX_PARTIES_COUNT) {
            throw new IllegalStateException("팟 개설 제한을 초과하였습니다. 최대 팟 개설 가능 수: " + Party.MAX_PARTIES_COUNT);
        }
    }
}
