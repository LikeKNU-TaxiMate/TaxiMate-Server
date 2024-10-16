package com.woopaca.taximate.core.domain.party.service;

import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.event.ParticipationEventPublisher;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
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
    private ParticipationEventPublisher participationEventPublisher;

    @Nested
    class participateParty_메서드는 {

        private PartyEntity partyEntity;

        @BeforeEach
        void setUp() {
            partyEntity = partyRepository.save(PartyFixtures.createPartyEntity());
            UserEntity userEntity = UserFixtures.createUserEntityWith("host");
            userRepository.save(userEntity);
            ParticipationEntity participationEntity = ParticipationFixtures.createParticipationEntityWith(partyEntity, userEntity);
            participationRepository.save(participationEntity);
        }

        @Test
        void 팟_참여_정보를_생성한다() {
            // given
            UserEntity userEntity = UserFixtures.createUserEntity();
            UserEntity savedUserEntity = userRepository.save(userEntity);
            User user = UserFixtures.createUser(savedUserEntity.getId());
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
                                            entity.getUser().getId().equals(user.getId()) &&
                                            entity.getParty().getId().equals(partyEntity.getId())
                            ),
                    () -> verify(participationEventPublisher).publishParticipateEvent(any(), any(), any())
            );
        }
    }
}