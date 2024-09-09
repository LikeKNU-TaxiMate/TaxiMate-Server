package com.woopaca.taximate.core.domain.party.service;

import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.local.AddressAllocator;
import com.woopaca.taximate.core.domain.party.KakaoMobilityClientProxy;
import com.woopaca.taximate.core.domain.party.ParticipationAppender;
import com.woopaca.taximate.core.domain.party.Parties;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.PartyAppender;
import com.woopaca.taximate.core.domain.party.PartyDetails;
import com.woopaca.taximate.core.domain.party.PartyFinder;
import com.woopaca.taximate.core.domain.party.PartyMapFinder;
import com.woopaca.taximate.core.domain.party.model.Coordinate;
import com.woopaca.taximate.core.domain.party.model.MapBound;
import com.woopaca.taximate.core.domain.taxi.Taxi;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.core.domain.user.UserFinder;
import com.woopaca.taximate.core.domain.user.UserLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PartyService {

    private final PartyMapFinder partyMapFinder;
    private final PartyFinder partyFinder;
    private final UserFinder userFinder;
    private final KakaoMobilityClientProxy kakaoMobilityClient;
    private final PartyValidator partyValidator;
    private final AddressAllocator addressAllocator;
    private final ParticipationAppender participationAppender;
    private final PartyAppender partyAppender;
    private final UserLock userLock;

    public PartyService(PartyMapFinder partyMapFinder, PartyFinder partyFinder, UserFinder userFinder, KakaoMobilityClientProxy kakaoMobilityClient, PartyValidator partyValidator, AddressAllocator addressAllocator, ParticipationAppender participationAppender, PartyAppender partyAppender, UserLock userLock) {
        this.partyMapFinder = partyMapFinder;
        this.partyFinder = partyFinder;
        this.userFinder = userFinder;
        this.kakaoMobilityClient = kakaoMobilityClient;
        this.partyValidator = partyValidator;
        this.addressAllocator = addressAllocator;
        this.participationAppender = participationAppender;
        this.partyAppender = partyAppender;
        this.userLock = userLock;
    }

    /**
     * 지도 범위 내의 팟 조회
     * @param mapBound 지도 범위
     * @return 지도 범위 내의 팟 목록
     */
    @Transactional(readOnly = true)
    public Parties getPartiesInRange(MapBound mapBound) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Party> partiesInRange = partyMapFinder.findByRangeAndDateTime(
                Coordinate.of(mapBound.minLatitude(), mapBound.minLongitude()),
                Coordinate.of(mapBound.maxLatitude(), mapBound.maxLongitude()),
                currentDateTime.minusMinutes(30)
        );
        return new Parties(partiesInRange);
    }

    /**
     * 팟 상세 조회
     * @param partyId 팟 ID
     * @return 팟 상세 정보
     */
    @Transactional
    public PartyDetails getPartyDetails(Long partyId) {
        Party party = partyFinder.findParty(partyId);
        //TODO 조회수 증가

        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        User host = userFinder.findUser(party.hostId());

        Taxi taxi = kakaoMobilityClient.requestTaxi(party.getOriginLocation(), party.getDestinationLocation());
        return new PartyDetails(party, host, taxi, authenticatedUser);
    }

    /**
     * 팟 생성
     * @param newParty 생성할 팟 정보
     * @return 생성된 팟 ID
     */
    @Transactional
    public Long createParty(Party newParty) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        userLock.lock(authenticatedUser);
        partyValidator.validateCreateParty(newParty, authenticatedUser);

        addressAllocator.allocateAddress(newParty);

        Long newPartyId = partyAppender.appendNew(newParty);
        participationAppender.appendHost(newPartyId, authenticatedUser);
        return newPartyId;
    }
}