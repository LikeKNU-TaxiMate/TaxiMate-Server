package com.woopaca.taximate.core.api.party.service;

import com.woopaca.taximate.core.api.common.error.exception.ExplanationTooLongException;
import com.woopaca.taximate.core.api.common.error.exception.ParticipantsCountException;
import com.woopaca.taximate.core.api.common.error.exception.ParticipantsFullException;
import com.woopaca.taximate.core.api.common.error.exception.ParticipationLimitException;
import com.woopaca.taximate.core.api.common.error.exception.PartyAlreadyEndedException;
import com.woopaca.taximate.core.api.common.error.exception.PartyAlreadyParticipatedException;
import com.woopaca.taximate.core.api.common.error.exception.PastDepartureTimeException;
import com.woopaca.taximate.core.api.common.error.exception.TitleTooLongException;
import com.woopaca.taximate.core.api.party.domain.Participation;
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

    public void validateCreateParty(Party party, User host) {
        validateContents(party);
        validateDepartureBeforeCurrentTime(party);
        validateParticipantsCount(party);
        validateMaxParticipationCount(host);
    }

    public void validateParticipateParty(Party party, User participant) {
        validateProgress(party);
        validateAlreadyParticipated(party, participant);
        validateMaxParticipationCount(participant);
        validateMaxParticipantsCount(party);
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

    private void validateMaxParticipationCount(User user) {
        List<Party> participatingParties = partyFinder.findParticipatingParties(user);
        if (participatingParties.size() >= Participation.MAX_PARTICIPATING_PARTIES_COUNT) {
            throw new ParticipationLimitException(Participation.MAX_PARTICIPATING_PARTIES_COUNT);
        }
    }

    private void validateProgress(Party party) {
        if (!party.isProgress()) {
            throw new PartyAlreadyEndedException(party.id());
        }
    }

    private void validateAlreadyParticipated(Party party, User participant) {
        if (party.isParticipated(participant)) {
            throw new PartyAlreadyParticipatedException(party.id(), participant.id());
        }
    }

    private void validateMaxParticipantsCount(Party party) {
        if (party.isFull()) {
            throw new ParticipantsFullException(party.maxParticipants(), party.id());
        }
    }
}
