package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.core.api.common.error.exception.HostingPartiesLimitException;
import com.woopaca.taximate.core.api.common.error.exception.ParticipantsCountException;
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

    public void validateParty(Party party, User host) {
        validateParticipantsCount(party);
        validateMaxPartiesCount(host);
    }

    private void validateParticipantsCount(Party party) {
        if (party.maxParticipants() > Party.MAX_PARTICIPANTS_COUNT) {
            throw new ParticipantsCountException(party.maxParticipants());
        }
        if (party.maxParticipants() < Party.MIN_PARTICIPANTS_COUNT) {
            throw new ParticipantsCountException(party.maxParticipants());
        }
    }

    private void validateMaxPartiesCount(User user) {
        List<Party> hostingParties = partyFinder.findHostingParties(user);
        if (hostingParties.size() >= Party.MAX_PARTIES_COUNT) {
            throw new HostingPartiesLimitException();
        }
    }
}
