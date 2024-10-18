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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager entityManager;

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
            ParticipationEntity participationEntity = ParticipationFixtures.createHostParticipationEntityWith(partyEntity, userEntity);
            participationRepository.save(participationEntity);

            entityManager.clear();
        }

        @Test
        void 팟_참여_정보를_생성한다() {
            // given
            UserEntity userEntity = UserFixtures.createUserEntity();
            UserEntity savedUserEntity = userRepository.save(userEntity);
            User user = User.fromEntity(savedUserEntity);
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

    @Nested
    class leaveParty_메서드는 {

        private PartyEntity partyEntity;
        private UserEntity host;
        private UserEntity participant;

        @BeforeEach
        void setUp() {
            partyEntity = partyRepository.save(PartyFixtures.createPartyEntity());
            host = userRepository.save(UserFixtures.createUserEntityWith("host"));
            participant = userRepository.save(UserFixtures.createUserEntityWith("participant"));
            participationRepository.save(ParticipationFixtures.createHostParticipationEntityWith(partyEntity, host));
            participationRepository.save(ParticipationFixtures.createParticipationEntityWith(partyEntity, participant));

            entityManager.clear();
        }

        @Nested
        class 호스트가_아닌_참여자가_나가는_경우 {

            @Test
            void 팟_참여_상태를_LEFT로_변경한다() {
                // given
                User user = User.fromEntity(participant);
                AuthenticatedUserHolder.setAuthenticatedUser(user);

                // when
                participationService.leaveParty(partyEntity.getId());

                // then
                ParticipationEntity participationEntity = participationRepository
                        .findByPartyIdAndUserId(partyEntity.getId(), participant.getId())
                        .orElseThrow();
                assertAll(
                        () -> assertThat(participationEntity.getRole()).isEqualTo(ParticipationRole.PARTICIPANT.name()),
                        () -> assertThat(participationEntity.getStatus()).isEqualTo(ParticipationStatus.LEFT.name()),
                        () -> verify(participationEventPublisher).publishLeaveEvent(any(), any(), any())
                );
            }
        }

        @Nested
        class 호스트인_참여자가_나가는_경우 {

            @Test
            void 팟_참여_상태를_LEFT로_변경하고_다음_참여자에게_호스트를_위임한다() {
                // given
                User user = User.fromEntity(host);
                AuthenticatedUserHolder.setAuthenticatedUser(user);

                // when
                participationService.leaveParty(partyEntity.getId());

                // then
                ParticipationEntity hostEntity = participationRepository
                        .findByPartyIdAndUserId(partyEntity.getId(), host.getId())
                        .orElseThrow();
                ParticipationEntity participantEntity = participationRepository
                        .findByPartyIdAndUserId(partyEntity.getId(), participant.getId())
                        .orElseThrow();
                assertAll(
                        () -> assertThat(hostEntity.getRole()).isEqualTo(ParticipationRole.PARTICIPANT.name()),
                        () -> assertThat(hostEntity.getStatus()).isEqualTo(ParticipationStatus.LEFT.name()),
                        () -> verify(participationEventPublisher).publishLeaveEvent(any(), any(), any()),
                        () -> assertThat(participantEntity.getRole()).isEqualTo(ParticipationRole.HOST.name())
                );
            }
        }
    }
}