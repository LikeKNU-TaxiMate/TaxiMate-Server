package com.woopaca.taximate.core.api.party.service;

import com.woopaca.taximate.core.api.fixture.ParticipationFixtures;
import com.woopaca.taximate.core.api.fixture.PartyFixtures;
import com.woopaca.taximate.core.api.fixture.UserFixtures;
import com.woopaca.taximate.core.api.user.domain.User;
import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;
import com.woopaca.taximate.storage.db.core.repository.ParticipationRepository;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import com.woopaca.taximate.storage.db.core.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class ParticipationServiceTest {

    @Autowired
    private ParticipationService participationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private ParticipationRepository participationRepository;

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
                .createParticipationEntityWith(partyEntity, userEntities.get(0).getId());
        participationRepository.save(participation);
    }

    @Nested
    class participateParty_메서드는 {

        @Nested
        class 동시에_여러_사용자가_팟에_참여하는_경우 {

            private static final int CONCURRENT_PARTICIPATE_REQUESTS = USERS_SIZE;

            @Test
            void 최대_참여_인원을_초과하지_않아야_한다() throws InterruptedException {
                ExecutorService executorService = Executors.newFixedThreadPool(CONCURRENT_PARTICIPATE_REQUESTS);
                CountDownLatch countDownLatch = new CountDownLatch(CONCURRENT_PARTICIPATE_REQUESTS);

                IntStream.rangeClosed(1, CONCURRENT_PARTICIPATE_REQUESTS)
                        .forEach(i -> executorService.execute(() -> {
                            try {
                                setSecurityContextForUser(i);
                                participationService.participateParty(partyEntity.getId());
                            } catch (Exception e) {
                                log.error("Error: ", e);
                            } finally {
                                countDownLatch.countDown();
                            }
                        }));

                countDownLatch.await();
                executorService.shutdown();

                List<ParticipationEntity> participationEntities = participationRepository
                        .findByPartyId(partyEntity.getId());
                assertThat(participationEntities).hasSize(MAX_PARTICIPANTS);
            }

            private void setSecurityContextForUser(int index) {
                SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
                User user = User.of(userEntities.get(index), true);
                UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken
                        .authenticated(user, null, Collections.emptyList());
                emptyContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(emptyContext);
            }
        }
    }
}
