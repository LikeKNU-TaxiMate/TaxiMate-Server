package com.woopaca.taximate.core.api.party.service;

import com.woopaca.taximate.core.api.common.error.exception.ExplanationTooLongException;
import com.woopaca.taximate.core.api.common.error.exception.ParticipationLimitException;
import com.woopaca.taximate.core.api.common.error.exception.ParticipantsCountException;
import com.woopaca.taximate.core.api.common.error.exception.PastDepartureTimeException;
import com.woopaca.taximate.core.api.common.error.exception.TitleTooLongException;
import com.woopaca.taximate.core.api.party.domain.Party;
import com.woopaca.taximate.core.api.party.domain.PartyFinder;
import com.woopaca.taximate.core.api.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class PartyValidator {

    private final PartyFinder partyFinder;

    public PartyValidator(PartyFinder partyFinder) {
        this.partyFinder = partyFinder;
    }

    public void validateParty(Party party, User host) {
        validateContents(party);
        validateDepartureBeforeCurrentTime(party);
        validateParticipantsCount(party);
        validateMaxParticipationCount(host);
    }

    private void validateContents(Party party) {
        if (party.title().length() > Party.MAX_TITLE_LENGTH) {
            throw new TitleTooLongException(Party.MAX_TITLE_LENGTH);
        }
        if (party.explanation().length() > Party.MAX_EXPLANATION_LENGTH) {
            throw new ExplanationTooLongException(Party.MAX_EXPLANATION_LENGTH);
        }
    }

    private void validateDepartureBeforeCurrentTime(Party party) {
        LocalDateTime departureTime = party.departureTime();
        if (departureTime.isBefore(LocalDateTime.now())) {
            throw new PastDepartureTimeException(departureTime);
        }
    }

    private void validateParticipantsCount(Party party) {
        if (party.maxParticipants() > Party.MAX_PARTICIPANTS_COUNT) {
            throw new ParticipantsCountException(party.maxParticipants());
        }
        if (party.maxParticipants() < Party.MIN_PARTICIPANTS_COUNT) {
            throw new ParticipantsCountException(party.maxParticipants());
        }
    }

    public void validateMaxParticipationCount(User user) {
        List<Party> participatingParties = partyFinder.findParticipatingParties(user);
        if (participatingParties.size() >= Party.MAX_PARTIES_COUNT) {
            throw new ParticipationLimitException(Party.MAX_PARTIES_COUNT);
        }
    }
}
