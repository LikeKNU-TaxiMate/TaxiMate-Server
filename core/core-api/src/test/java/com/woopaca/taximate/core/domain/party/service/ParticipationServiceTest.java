package com.woopaca.taximate.core.domain.party.service;

import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.event.ParticipationEventProducer;
import com.woopaca.taximate.core.domain.fixture.ParticipationFixtures;
import com.woopaca.taximate.core.domain.fixture.PartyFixtures;
import com.woopaca.taximate.core.domain.fixture.UserFixtures;
import com.woopaca.taximate.core.domain.party.Participation.ParticipationRole;
import com.woopaca.taximate.core.domain.party.Participation.ParticipationStatus;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;
import com.woopaca.taximate.storage.db.core.repository.ParticipationRepository;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import com.woopaca.taximate.storage.db.core.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;

@Slf4j
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ParticipationServiceTest {

    @Autowired
    private ParticipationService participationService;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private ParticipationRepository participationRepository;
    @Autowired
    private UserRepository userRepository;

    @MockBean
    private ParticipationEventProducer participationEventProducer;

    @Nested
    class participateParty_메서드는 {

        private PartyEntity partyEntity;

        @BeforeEach
        void setUp() {
            partyEntity = partyRepository.save(PartyFixtures.createPartyEntity());
            UserEntity userEntity = UserFixtures.createUserEntityWith("host");
            userRepository.save(userEntity);
            ParticipationEntity participationEntity = ParticipationFixtures.createParticipationEntityWith(partyEntity, userEntity.getId());
            participationRepository.save(participationEntity);
        }

        @Test
        void 팟_참여_정보를_생성한다() {
            // given
            User user = UserFixtures.createUser(2L);
            AuthenticatedUserHolder.setAuthenticatedUser(user);

            // when
            Long participatedPartyId = participationService.participateParty(partyEntity.getId());

            // then
            List<ParticipationEntity> participationEntities = participationRepository.findByPartyId(partyEntity.getId());
            assertAll(
                    () -> assertThat(participatedPartyId).isEqualTo(partyEntity.getId()),
                    () -> assertThat(participationEntities).hasSize(2),
                    () -> assertThat(participationEntities)
                            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                            .anyMatch(entity ->
                                    entity.getRole().equals(ParticipationRole.PARTICIPANT.name()) &&
                                            entity.getStatus().equals(ParticipationStatus.PARTICIPATING.name()) &&
                                            entity.getUserId().equals(user.getId()) &&
                                            entity.getParty().getId().equals(partyEntity.getId())
                            ),
                    () -> verify(participationEventProducer).publishParticipateEvent(partyEntity.getId(), user.getId())
            );
        }
    }
}