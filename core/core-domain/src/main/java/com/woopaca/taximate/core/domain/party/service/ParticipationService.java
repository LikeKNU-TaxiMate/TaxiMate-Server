package com.woopaca.taximate.core.domain.party.service;

import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.event.ParticipationEventProducer;
import com.woopaca.taximate.core.domain.party.ParticipationModifier;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.PartyFinder;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.core.domain.user.UserLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ParticipationService {

    private final PartyFinder partyFinder;
    private final PartyValidator partyValidator;
    private final ParticipationModifier participationModifier;
    private final ParticipationEventProducer participationEventProducer;
    private final UserLock userLock;

    public ParticipationService(PartyFinder partyFinder, PartyValidator partyValidator, ParticipationModifier participationModifier, ParticipationEventProducer participationEventProducer, UserLock userLock) {
        this.partyFinder = partyFinder;
        this.partyValidator = partyValidator;
        this.participationModifier = participationModifier;
        this.participationEventProducer = participationEventProducer;
        this.userLock = userLock;
    }

    /**
     * 팟 참여
     * @param partyId 참여할 팟 ID
     * @return 참여한 팟 ID
     */
    @Transactional
    public Long participateParty(Long partyId) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        userLock.lock(authenticatedUser);
        Party party = partyFinder.findPartyWithLock(partyId);
        partyValidator.validateParticipateParty(party, authenticatedUser);
        participationModifier.appendParticipant(party, authenticatedUser);

        participationEventProducer.publishParticipateEvent(party, authenticatedUser, LocalDateTime.now());
        return partyId;
    }

    /**
     * 팟 나가기
     * @param partyId 나갈 팟 ID
     * @return 나간 팟 ID
     */
    @Transactional
    public Long leaveParty(Long partyId) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        Party party = partyFinder.findPartyWithLock(partyId);
        partyValidator.validateLeaveParty(party, authenticatedUser);

        if (party.isHostUser(authenticatedUser)) {
            participationModifier.delegateHost(party, authenticatedUser);
        }
        participationModifier.removeParticipant(party, authenticatedUser);

        participationEventProducer.publishLeaveEvent(party, authenticatedUser, LocalDateTime.now());
        return party.getId();
    }
}
