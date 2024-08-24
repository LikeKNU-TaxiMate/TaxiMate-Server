package com.woopaca.taximate.core.api.party.service;

import com.woopaca.taximate.core.api.local.domain.AddressAllocator;
import com.woopaca.taximate.core.api.party.domain.ParticipationAppender;
import com.woopaca.taximate.core.api.party.domain.Parties;
import com.woopaca.taximate.core.api.party.domain.Party;
import com.woopaca.taximate.core.api.party.domain.PartyAppender;
import com.woopaca.taximate.core.api.party.domain.PartyDetails;
import com.woopaca.taximate.core.api.party.domain.PartyFinder;
import com.woopaca.taximate.core.api.party.domain.PartyMapFinder;
import com.woopaca.taximate.core.api.party.model.Coordinate;
import com.woopaca.taximate.core.api.party.model.MapBound;
import com.woopaca.taximate.core.api.taxi.api.KakaoMobilityClient;
import com.woopaca.taximate.core.api.taxi.domain.Taxi;
import com.woopaca.taximate.core.api.user.domain.User;
import com.woopaca.taximate.core.api.user.domain.UserFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PartyService {

    private final PartyMapFinder partyMapFinder;
    private final PartyFinder partyFinder;
    private final UserFinder userFinder;
    private final KakaoMobilityClient kakaoMobilityClient;
    private final PartyValidator partyValidator;
    private final AddressAllocator addressAllocator;
    private final ParticipationAppender participationAppender;
    private final PartyAppender partyAppender;

    public PartyService(PartyMapFinder partyMapFinder, PartyFinder partyFinder, UserFinder userFinder, KakaoMobilityClient kakaoMobilityClient, PartyValidator partyValidator, AddressAllocator addressAllocator, ParticipationAppender participationAppender, PartyAppender partyAppender) {
        this.partyMapFinder = partyMapFinder;
        this.partyFinder = partyFinder;
        this.userFinder = userFinder;
        this.kakaoMobilityClient = kakaoMobilityClient;
        this.partyValidator = partyValidator;
        this.addressAllocator = addressAllocator;
        this.participationAppender = participationAppender;
        this.partyAppender = partyAppender;
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

        User authenticatedUser = userFinder.findAuthenticatedUser();
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
    public Long createParty(Party newParty) { // TODO 외부 API 호출 작업은 트랜잭션 분리 고려
        User authenticatedUser = userFinder.findAuthenticatedUser();
        partyValidator.validateCreateParty(newParty, authenticatedUser);

        Party createdParty = addressAllocator.allocateAddress(newParty);

        Long newPartyId = partyAppender.appendNew(createdParty);
        participationAppender.appendHost(newPartyId, authenticatedUser);
        return newPartyId;
    }
}
