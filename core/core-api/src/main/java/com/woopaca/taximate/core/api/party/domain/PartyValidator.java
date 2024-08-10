package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.core.api.user.domain.User;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PartyValidator {

    private final PartyFinder partyFinder;
    private final PartyRepository partyRepository;

    public PartyValidator(PartyFinder partyFinder, PartyRepository partyRepository) {
        this.partyFinder = partyFinder;
        this.partyRepository = partyRepository;
    }

    public void validateMaxPartiesCount(User user) {
        List<Party> hostingParties = partyFinder.findHostingParties(user);
        if (hostingParties.size() >= Party.MAX_PARTIES_COUNT) {
            throw new IllegalStateException("팟 개설 제한을 초과하였습니다. 최대 팟 개설 가능 수: " + Party.MAX_PARTIES_COUNT);
        }
    }
}
