package com.woopaca.taximate.core.domain.party.service.concurrent;

import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.context.DatabaseCleaner;
import com.woopaca.taximate.core.domain.fixture.ParticipationFixtures;
import com.woopaca.taximate.core.domain.fixture.PartyFixtures;
import com.woopaca.taximate.core.domain.fixture.UserFixtures;
import com.woopaca.taximate.core.domain.party.Participation;
import com.woopaca.taximate.core.domain.party.service.ParticipationService;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;
import com.woopaca.taximate.storage.db.core.repository.ParticipationRepository;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import com.woopaca.taximate.storage.db.core.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("ParticipationService 동시성 테스트")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ParticipationServiceConcurrencyTest extends ConcurrencyTest {

    @Autowired
    private ParticipationService participationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private ParticipationRepository participationRepository;
    @Autowired
    private DatabaseCleaner databaseCleaner;

    private static final int MAX_PARTICIPANTS = 4;
    private static final int USERS_SIZE = MAX_PARTICIPANTS + 1;

    private List<UserEntity> userEntities;
    private PartyEntity partyEntity;

    @BeforeEach
    void setUp() {
        userEntities = createAndSaveUsers();
        partyEntity = createAndSaveParty();
        createAndSaveInitialParticipation();
    }

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    private List<UserEntity> createAndSaveUsers() {
        List<UserEntity> users = IntStream.rangeClosed(1, USERS_SIZE)
                .mapToObj(i -> UserFixtures.createUserEntityWith("test" + i))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        return userRepository.saveAll(users);
    }

    private PartyEntity createAndSaveParty() {
        PartyEntity party = PartyFixtures.createPartyEntityWith(MAX_PARTICIPANTS);
        return partyRepository.save(party);
    }

    private void createAndSaveInitialParticipation() {
        ParticipationEntity participation = ParticipationFixtures
                .createParticipationEntityWith(partyEntity, userEntities.get(0));
        participationRepository.save(participation);
    }

    @Nested
    class participateParty_메서드는 {

        @Nested
        class 동시에_여러_사용자가_팟에_참여하는_경우 {

            private static final int CONCURRENT_PARTICIPATE_REQUESTS = USERS_SIZE;

            @Test
            void 최대_참여_인원을_초과하지_않아야_한다() throws InterruptedException {
                executeTasksInParallel(index -> {
                    setSecurityContextForUser(index);
                    participationService.participateParty(partyEntity.getId());
                }, CONCURRENT_PARTICIPATE_REQUESTS);

                List<ParticipationEntity> participationEntities = participationRepository
                        .findByPartyId(partyEntity.getId());
                assertThat(participationEntities).hasSize(MAX_PARTICIPANTS);
            }
        }

        @Nested
        class 한_사용자가_동시에_여러_팟에_참여하는_경우 {

            private static final int CONCURRENT_PARTICIPATE_REQUESTS = Participation.MAX_PARTICIPATING_PARTIES_COUNT + 1;

            private final List<PartyEntity> partyEntities = new ArrayList<>();

            @Test
            void 최대_참여_팟_개수를_초과하지_않아야_한다() throws InterruptedException {
                IntStream.rangeClosed(1, CONCURRENT_PARTICIPATE_REQUESTS)
                        .forEach(i -> {
                            PartyEntity partyEntity = PartyFixtures.createPartyEntity();
                            PartyEntity savedPartyEntity = partyRepository.save(partyEntity);
                            partyEntities.add(savedPartyEntity);
                        });

                int userIndex = 0;
                executeTasksInParallel(index -> {
                    setSecurityContextForUser(userIndex);
                    participationService.participateParty(partyEntities.get(index).getId());
                }, CONCURRENT_PARTICIPATE_REQUESTS);

                List<ParticipationEntity> participationEntities = participationRepository
                        .findByUserId(userEntities.get(userIndex).getId());
                assertThat(participationEntities).hasSize(Participation.MAX_PARTICIPATING_PARTIES_COUNT);
            }
        }

        @Nested
        class 한_사용자가_한_팟에_여러_참여_요청을_하는_경우 {

            private static final int CONCURRENT_PARTICIPATE_REQUESTS = USERS_SIZE;

            @Test
            void 최대_참여_인원을_초과하지_않아야_한다() throws InterruptedException {
                executeTasksInParallel(index -> {
                    setSecurityContextForUser(0);
                    participationService.participateParty(partyEntity.getId());
                }, CONCURRENT_PARTICIPATE_REQUESTS);

                List<ParticipationEntity> participationEntities = participationRepository
                        .findByPartyId(partyEntity.getId());
                assertThat(participationEntities).hasSize(1);
            }
        }

        private void setSecurityContextForUser(int index) {
            AuthenticatedUserHolder.setAuthenticatedUser(User.fromEntity(userEntities.get(index)));
        }
    }
}
