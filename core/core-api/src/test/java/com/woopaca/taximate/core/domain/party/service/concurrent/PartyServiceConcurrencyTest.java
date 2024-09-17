package com.woopaca.taximate.core.domain.party.service.concurrent;

import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.context.DatabaseCleaner;
import com.woopaca.taximate.core.domain.fixture.ParticipationFixtures;
import com.woopaca.taximate.core.domain.fixture.PartyFixtures;
import com.woopaca.taximate.core.domain.fixture.UserFixtures;
import com.woopaca.taximate.core.domain.local.AddressAllocator;
import com.woopaca.taximate.core.domain.party.Participation;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.service.PartyService;
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
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("PartyService 동시성 테스트")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class PartyServiceConcurrencyTest extends ConcurrencyTest {

    @Autowired
    private PartyService partyService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private ParticipationRepository participationRepository;
    @MockBean
    private AddressAllocator addressAllocator;
    @Autowired
    private DatabaseCleaner databaseCleaner;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = UserFixtures.createUserEntityWith("test");
        userRepository.save(userEntity);
        PartyEntity partyEntity1 = PartyFixtures.createPartyEntity();
        partyRepository.save(partyEntity1);
        PartyEntity partyEntity2 = PartyFixtures.createPartyEntity();
        partyRepository.save(partyEntity2);

        ParticipationEntity participationEntity1 = ParticipationFixtures
                .createParticipationEntityWith(partyEntity1, userEntity.getId());
        ParticipationEntity participationEntity2 = ParticipationFixtures
                .createParticipationEntityWith(partyEntity2, userEntity.getId());
        participationRepository.save(participationEntity1);
        participationRepository.save(participationEntity2);
    }

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @Nested
    class createParty_메서드는 {

        @Test
        void 한_사용자가_동시에_많은_팟생성_요청을_해도_최대_개수를_초과하지_않아야_한다() throws InterruptedException {
            int executionCount = Participation.MAX_PARTICIPATING_PARTIES_COUNT - 1;

            executeTasksInParallel(index -> {
                setSecurityContext();
                Party party = PartyFixtures.createParty();
                partyService.createParty(party);
            }, executionCount);

            assertThat(partyRepository.count()).isEqualTo(Participation.MAX_PARTICIPATING_PARTIES_COUNT);
        }

        private void setSecurityContext() {
            AuthenticatedUserHolder.setAuthenticatedUser(User.fromEntity(userEntity));
        }
    }
}
