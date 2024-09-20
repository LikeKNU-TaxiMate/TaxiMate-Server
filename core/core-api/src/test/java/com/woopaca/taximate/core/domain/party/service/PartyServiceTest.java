package com.woopaca.taximate.core.domain.party.service;

import com.woopaca.taximate.core.domain.fixture.PartyFixtures;
import com.woopaca.taximate.core.domain.party.Parties;
import com.woopaca.taximate.core.domain.party.model.Coordinate;
import com.woopaca.taximate.core.domain.party.model.MapBound;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import org.h2gis.functions.factory.H2GISFunctions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
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
    }
}