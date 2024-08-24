package com.woopaca.taximate.core.api.party.service;

import com.woopaca.taximate.core.api.party.domain.ParticipationAppender;
import com.woopaca.taximate.core.api.party.domain.Party;
import com.woopaca.taximate.core.api.party.domain.PartyFinder;
import com.woopaca.taximate.core.api.user.domain.User;
import com.woopaca.taximate.core.api.user.domain.UserFinder;
import com.woopaca.taximate.core.message.event.ParticipationEventProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParticipationService {

    private final UserFinder userFinder;
    private final PartyFinder partyFinder;
    private final PartyValidator partyValidator;
    private final ParticipationAppender participationAppender;
    private final ParticipationEventProducer participationEventProducer;

    public ParticipationService(UserFinder userFinder, PartyFinder partyFinder, PartyValidator partyValidator, ParticipationAppender participationAppender, ParticipationEventProducer participationEventProducer) {
        this.userFinder = userFinder;
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
    public Long participateParty(Long partyId) { // TODO 동시성 문제 발생 가능성 고려
        User authenticatedUser = userFinder.findAuthenticatedUser();
        Party party = partyFinder.findParty(partyId);
        partyValidator.validateParticipateParty(party, authenticatedUser);
        participationAppender.appendParticipant(party, authenticatedUser);

        participationEventProducer.publishParticipateEvent(partyId, authenticatedUser.getId());
        return partyId;
    }
}
