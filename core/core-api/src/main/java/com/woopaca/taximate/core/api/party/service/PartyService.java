package com.woopaca.taximate.core.api.party.service;

import com.woopaca.taximate.core.api.party.controller.dto.request.MapBound;
import com.woopaca.taximate.core.api.party.domain.Parties;
import com.woopaca.taximate.core.api.party.domain.Party;
import com.woopaca.taximate.core.api.party.domain.PartyDetails;
import com.woopaca.taximate.core.api.party.domain.PartyFinder;
import com.woopaca.taximate.core.api.party.domain.PartyMapFinder;
import com.woopaca.taximate.core.api.party.model.Coordinate;
import com.woopaca.taximate.core.api.taxi.api.KakaoMobilityClient;
import com.woopaca.taximate.core.api.taxi.domain.Taxi;
import com.woopaca.taximate.core.api.user.domain.User;
import com.woopaca.taximate.core.api.user.domain.UserFinder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PartyService {

    private final PartyMapFinder partyMapFinder;
    private final PartyFinder partyFinder;
    private final UserFinder userFinder;
    private final KakaoMobilityClient kakaoMobilityClient;

    public PartyService(PartyMapFinder partyMapFinder, PartyFinder partyFinder, UserFinder userFinder, KakaoMobilityClient kakaoMobilityClient) {
        this.partyMapFinder = partyMapFinder;
        this.partyFinder = partyFinder;
        this.userFinder = userFinder;
        this.kakaoMobilityClient = kakaoMobilityClient;
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

        Taxi taxi = kakaoMobilityClient.requestTaxi(party.originLocation(), party.destinationLocation());
        return new PartyDetails(party, host, taxi, authenticatedUser);
    }
}
