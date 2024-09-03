package com.woopaca.taximate.core.domain.party.service;

import com.woopaca.taximate.core.domain.error.exception.ExplanationTooLongException;
import com.woopaca.taximate.core.domain.error.exception.ParticipantsCountException;
import com.woopaca.taximate.core.domain.error.exception.ParticipantsFullException;
import com.woopaca.taximate.core.domain.error.exception.ParticipationLimitException;
import com.woopaca.taximate.core.domain.error.exception.PartyAlreadyEndedException;
import com.woopaca.taximate.core.domain.error.exception.PartyAlreadyParticipatedException;
import com.woopaca.taximate.core.domain.error.exception.PastDepartureTimeException;
import com.woopaca.taximate.core.domain.error.exception.TitleTooLongException;
import com.woopaca.taximate.core.domain.party.Participation;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.PartyFinder;
import com.woopaca.taximate.core.domain.user.User;
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
        if (party.getTitle().length() > Party.MAX_TITLE_LENGTH) {
            throw new TitleTooLongException(Party.MAX_TITLE_LENGTH);
        }
        if (party.getExplanation().length() > Party.MAX_EXPLANATION_LENGTH) {
            throw new ExplanationTooLongException(Party.MAX_EXPLANATION_LENGTH);
        }
    }

    private void validateDepartureBeforeCurrentTime(Party party) {
        LocalDateTime departureTime = party.getDepartureTime();
        if (departureTime.isBefore(LocalDateTime.now())) {
            throw new PastDepartureTimeException(departureTime);
        }
    }

    private void validateParticipantsCount(Party party) {
        if (party.getMaxParticipants() > Party.MAX_PARTICIPANTS_COUNT) {
            throw new ParticipantsCountException(party.getMaxParticipants());
        }
        if (party.getMaxParticipants() < Party.MIN_PARTICIPANTS_COUNT) {
            throw new ParticipantsCountException(party.getMaxParticipants());
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
            throw new PartyAlreadyEndedException(party.getId());
        }
    }

    private void validateAlreadyParticipated(Party party, User participant) {
        if (party.isParticipated(participant)) {
            throw new PartyAlreadyParticipatedException(party.getId(), participant.getId());
        }
    }

    private void validateMaxParticipantsCount(Party party) {
        if (party.isFull()) {
            throw new ParticipantsFullException(party.getMaxParticipants(), party.getId());
        }
    }
}
