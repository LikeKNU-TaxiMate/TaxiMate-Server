package com.woopaca.taximate.core.domain.party.service;

import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.fixture.PartyFixtures;
import com.woopaca.taximate.core.domain.fixture.UserFixtures;
import com.woopaca.taximate.core.domain.local.AddressAllocator;
import com.woopaca.taximate.core.domain.party.Participation.ParticipationRole;
import com.woopaca.taximate.core.domain.party.Participation.ParticipationStatus;
import com.woopaca.taximate.core.domain.party.Parties;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.model.Coordinate;
import com.woopaca.taximate.core.domain.party.model.MapBound;
import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.repository.ParticipationRepository;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import org.h2gis.functions.factory.H2GISFunctions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class PartyServiceTest {

    @Autowired
    private PartyService partyService;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private ParticipationRepository participationRepository;

    @MockBean
    private AddressAllocator addressAllocator;

    @Nested
    class getPartiesInRange_메서드는 {

        @BeforeEach
        void setUp() throws SQLException {
            Connection connection = DriverManager
                    .getConnection("jdbc:h2:mem:test;MODE=MySQL;NON_KEYWORDS=user;", "sa", "");
            H2GISFunctions.load(connection);
        }

        @Test
        void 주어진_좌표_범위_내에_있는_팟을_반환한다() {
            //given
            partyRepository.save(PartyFixtures.createPartyEntityWith(Coordinate.of(37.123456, 127.123456)));
            partyRepository.save(PartyFixtures.createPartyEntityWith(Coordinate.of(36.123456, 127.123456)));
            partyRepository.save(PartyFixtures.createPartyEntityWith(Coordinate.of(37.123456, 128.123456)));

            //when
            MapBound mapBound = new MapBound(37.0, 127.0, 38.0, 128.0);
            Parties partiesInRange = partyService.getPartiesInRange(mapBound);

            //then
            assertThat(partiesInRange.stream()).hasSize(1);
        }

        @Test
        void 주어진_좌표_범위_내에_있는_팟이_없으면_빈_팟_목록을_반환한다() {
            //given
            partyRepository.save(PartyFixtures.createPartyEntityWith(Coordinate.of(37.123456, 127.123456)));
            partyRepository.save(PartyFixtures.createPartyEntityWith(Coordinate.of(36.123456, 127.123456)));
            partyRepository.save(PartyFixtures.createPartyEntityWith(Coordinate.of(37.123456, 128.123456)));

            //when
            MapBound mapBound = new MapBound(39.0, 129.0, 40.0, 130.0);
            Parties partiesInRange = partyService.getPartiesInRange(mapBound);

            //then
            assertThat(partiesInRange.stream()).isEmpty();
        }
    }

    @Nested
    class createParty_메서드는 {

        @Test
        void 새로운_팟을_생성하고_참여_정보를_생성한다() {
            //given
            Party party = PartyFixtures.createParty();
            AuthenticatedUserHolder.setAuthenticatedUser(UserFixtures.createUser(1L));

            //when
            Long partyId = partyService.createParty(party);

            //then
            PartyEntity partyEntity = partyRepository.findById(partyId)
                    .orElseThrow();
            ParticipationEntity participationEntity = participationRepository.findByPartyId(partyId)
                    .get(0);
            Assertions.assertAll(
                    () -> assertThat(partyEntity.getId()).isEqualTo(partyId),
                    () -> assertThat(participationEntity).isNotNull(),
                    () -> assertThat(participationEntity.getUserId()).isEqualTo(1L),
                    () -> assertThat(participationEntity.getRole()).isEqualTo(ParticipationRole.HOST.name()),
                    () -> assertThat(participationEntity.getStatus()).isEqualTo(ParticipationStatus.PARTICIPATING.name())
            );
        }
    }
}