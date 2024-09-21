package com.woopaca.taximate.core.domain.party.service;

import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.event.ParticipationEventProducer;
import com.woopaca.taximate.core.domain.party.Participation;
import com.woopaca.taximate.core.domain.party.ParticipationAppender;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.PartyFinder;
import com.woopaca.taximate.core.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParticipationService {

    private final PartyFinder partyFinder;
    private final PartyValidator partyValidator;
    private final ParticipationAppender participationAppender;
    private final ParticipationEventProducer participationEventProducer;

    public ParticipationService(PartyFinder partyFinder, PartyValidator partyValidator, ParticipationAppender participationAppender, ParticipationEventProducer participationEventProducer) {
        this.partyFinder = partyFinder;
        this.partyValidator = partyValidator;
        this.participationAppender = participationAppender;
        this.participationEventProducer = participationEventProducer;
    }

    /**
     * 팟 참여
     * @param partyId 참여할 팟 ID
     * @return 참여한 팟 ID
     */
    @Transactional
    public Long participateParty(Long partyId) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        Party party = partyFinder.findPartyWithLock(partyId);
        partyValidator.validateParticipateParty(party, authenticatedUser);
        Participation participation = participationAppender.appendParticipant(party, authenticatedUser);

        participationEventProducer.publishParticipateEvent(partyId, authenticatedUser, participation.getParticipatedAt());
        return partyId;
    }
}
