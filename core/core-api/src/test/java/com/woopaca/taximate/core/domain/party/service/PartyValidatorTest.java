package com.woopaca.taximate.core.domain.party.service;

import com.woopaca.taximate.core.domain.error.exception.ExplanationTooLongException;
import com.woopaca.taximate.core.domain.error.exception.ParticipantsCountException;
import com.woopaca.taximate.core.domain.error.exception.ParticipantsFullException;
import com.woopaca.taximate.core.domain.error.exception.ParticipationLimitException;
import com.woopaca.taximate.core.domain.error.exception.PartyAlreadyEndedException;
import com.woopaca.taximate.core.domain.error.exception.PartyAlreadyParticipatedException;
import com.woopaca.taximate.core.domain.error.exception.PastDepartureTimeException;
import com.woopaca.taximate.core.domain.error.exception.TitleTooLongException;
import com.woopaca.taximate.core.domain.fixture.ParticipationFixtures;
import com.woopaca.taximate.core.domain.fixture.PartyFixtures;
import com.woopaca.taximate.core.domain.fixture.UserFixtures;
import com.woopaca.taximate.core.domain.party.Participation;
import com.woopaca.taximate.core.domain.party.Party;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class PartyValidatorTest {

    @Autowired
    private PartyValidator partyValidator;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParticipationRepository participationRepository;

    @Nested
    class validateCreateParty_메서드는 {

        private UserEntity hostEntity;
        private User host;

        @BeforeEach
        void setUp() {
            UserEntity userEntity = UserFixtures.createUserEntityWith("test");
            hostEntity = userRepository.save(userEntity);
            host = User.fromEntity(hostEntity);
        }

        @Nested
        class 정상적인_팟_생성_요청인_경우 {

            @Test
            void 아무런_예외가_발생하지_않는다() {
                // given
                Party party = PartyFixtures.createParty();

                // when & then
                partyValidator.validateCreateParty(party, host);
            }
        }

        @Nested
        class 정상적이지_않은_팟_생성_요청_중 {

            @Test
            void 제목이_최대_글자수를_초과하는_경우_예외가_발생한다() {
                // given
                Party party = Party.builder()
                        .title("a".repeat(Party.MAX_TITLE_LENGTH + 1))
                        .explanation("팟 설명")
                        .departureTime(LocalDateTime.now().plusHours(1))
                        .maxParticipants(4)
                        .build();

                // when & then
                assertThatThrownBy(() -> partyValidator.validateCreateParty(party, host))
                        .isInstanceOf(TitleTooLongException.class)
                        .hasMessage(String.format("제목은 %d자 이하이어야 합니다.", Party.MAX_TITLE_LENGTH));
            }

            @Test
            void 설명이_최대_글자수를_초과하는_경우_예외가_발생한다() {
                // given
                Party party = Party.builder()
                        .title("팟 제목")
                        .explanation("a".repeat(Party.MAX_EXPLANATION_LENGTH + 1))
                        .departureTime(LocalDateTime.now().plusHours(1))
                        .maxParticipants(4)
                        .build();

                // when & then
                assertThatThrownBy(() -> partyValidator.validateCreateParty(party, host))
                        .isInstanceOf(ExplanationTooLongException.class)
                        .hasMessage(String.format("설명은 %d자 이하이어야 합니다.", Party.MAX_EXPLANATION_LENGTH));
            }

            @Test
            void 출발시간이_현재시간보다_이전인_경우_예외가_발생한다() {
                // given
                Party party = Party.builder()
                        .title("팟 제목")
                        .explanation("팟 설명")
                        .departureTime(LocalDateTime.now().minusHours(1))
                        .maxParticipants(4)
                        .build();

                // when & then
                assertThatThrownBy(() -> partyValidator.validateCreateParty(party, host))
                        .isInstanceOf(PastDepartureTimeException.class)
                        .hasMessage("출발 시간이 현재 시간보다 이전일 수 없습니다. 시간: " + party.getDepartureTime());
            }

            @ParameterizedTest
            @ValueSource(ints = {Party.MIN_PARTICIPANTS_COUNT - 1, Party.MAX_PARTICIPANTS_COUNT + 1})
            void 최대_참여_인원수가_범위를_벗어나는_경우_예외가_발생한다(int maxParticipants) {
                // given
                Party party = Party.builder()
                        .title("팟 제목")
                        .explanation("팟 설명")
                        .departureTime(LocalDateTime.now().plusHours(1))
                        .maxParticipants(maxParticipants)
                        .build();

                // when & then
                assertThatThrownBy(() -> partyValidator.validateCreateParty(party, host))
                        .isInstanceOf(ParticipantsCountException.class)
                        .hasMessage(String.format("설정 가능한 팟 참여 최대 인원은 %d ~ %d명입니다. 현재: %d",
                                Party.MIN_PARTICIPANTS_COUNT, Party.MAX_PARTICIPANTS_COUNT, party.getMaxParticipants()));
            }

            @Test
            void 팟_최대_참여_가능_개수를_초과하는_경우_예외가_발생한다() {
                // given
                IntStream.rangeClosed(1, Participation.MAX_PARTICIPATING_PARTIES_COUNT)
                        .forEach(i -> {
                            PartyEntity partyEntity = PartyFixtures.createPartyEntity();
                            partyRepository.save(partyEntity);
                            ParticipationEntity participationEntity = ParticipationFixtures
                                    .createParticipationEntityWith(partyEntity, hostEntity);
                            participationRepository.save(participationEntity);
                        });

                Party party = Party.builder()
                        .title("팟 제목")
                        .explanation("팟 설명")
                        .departureTime(LocalDateTime.now().plusHours(1))
                        .maxParticipants(4)
                        .build();

                // when & then
                assertThatThrownBy(() -> partyValidator.validateCreateParty(party, host))
                        .isInstanceOf(ParticipationLimitException.class)
                        .hasMessage(String.format("팟 참여 제한을 초과하였습니다. 최대 팟 참여 가능 수: %d", Participation.MAX_PARTICIPATING_PARTIES_COUNT));
            }
        }
    }

    @Nested
    class validateParticipateParty_메서드는 {

        private PartyEntity partyEntity;
        private UserEntity participantEntity;
        private User participant;
        private Party party;

        @BeforeEach
        void setUp() {
            UserEntity userEntity = UserFixtures.createUserEntityWith("test");
            participantEntity = userRepository.save(userEntity);
            participant = User.fromEntity(participantEntity);

            PartyEntity partyEntity = PartyFixtures.createPartyEntity();
            this.partyEntity = partyRepository.save(partyEntity);
            party = Party.fromEntity(this.partyEntity);
        }

        @Nested
        class 정상적인_팟_참여_요청인_경우 {

            @Test
            void 아무런_예외가_발생하지_않는다() {
                // given
                ParticipationEntity participationEntity = ParticipationFixtures
                        .createParticipationEntityWith(partyEntity, participantEntity);
                participationRepository.save(participationEntity);

                // when & then
                partyValidator.validateParticipateParty(party, participant);
            }
        }

        @Nested
        class 정상적이지_않은_팟_참여_요청_중 {

            @Test
            void 이미_종료된_팟에_참여하는_경우_예외가_발생한다() {
                // given
                PartyEntity terminatedPartyEntity = PartyFixtures.createTerminatedPartyEntity();
                PartyEntity savedPartyEntity = partyRepository.save(terminatedPartyEntity);
                Party terminatedParty = Party.fromEntity(savedPartyEntity);

                // when & then
                assertThatThrownBy(() -> partyValidator.validateParticipateParty(terminatedParty, participant))
                        .isInstanceOf(PartyAlreadyEndedException.class)
                        .hasMessage(String.format("팟이 이미 종료되었습니다. partyId: %d", terminatedParty.getId()));
            }

            @Test
            void 이미_참여한_팟에_참여하는_경우_예외가_발생한다() {
                // given
                ParticipationEntity participationEntity = ParticipationFixtures
                        .createParticipationEntityWith(partyEntity, participantEntity);
                participationRepository.save(participationEntity);

                party.getParticipationSet()
                        .add(Participation.fromEntity(participationEntity));

                // when & then
                assertThatThrownBy(() -> partyValidator.validateParticipateParty(party, participant))
                        .isInstanceOf(PartyAlreadyParticipatedException.class)
                        .hasMessage(String.format("이미 참여한 팟입니다. partyId: %d, userId: %d", party.getId(), participant.getId()));
            }

            @Test
            void 팟_최대_참여_가능_개수를_초과하는_경우_예외가_발생한다() {
                // given
                IntStream.rangeClosed(1, Participation.MAX_PARTICIPATING_PARTIES_COUNT)
                        .forEach(i -> {
                            PartyEntity partyEntity = PartyFixtures.createPartyEntity();
                            partyRepository.save(partyEntity);
                            ParticipationEntity participationEntity = ParticipationFixtures
                                    .createParticipationEntityWith(partyEntity, participantEntity);
                            participationRepository.save(participationEntity);
                        });

                // when & then
                assertThatThrownBy(() -> partyValidator.validateParticipateParty(party, participant))
                        .isInstanceOf(ParticipationLimitException.class)
                        .hasMessage(String.format("팟 참여 제한을 초과하였습니다. 최대 팟 참여 가능 수: %d", Participation.MAX_PARTICIPATING_PARTIES_COUNT));
            }

            @Test
            void 참여자가_가득_찬_팟에_참여하는_경우_예외가_발생한다() {
                // given
                IntStream.rangeClosed(1, party.getMaxParticipants())
                        .forEach(i -> {
                            UserEntity userEntity = UserFixtures.createUserEntityWith(String.valueOf(i + 100));
                            userRepository.save(userEntity);
                            ParticipationEntity participationEntity = ParticipationFixtures
                                    .createParticipationEntityWith(partyEntity, userEntity);
                            participationRepository.save(participationEntity);
                            party.getParticipationSet()
                                    .add(Participation.fromEntity(participationEntity));
                        });

                // when & then
                assertThatThrownBy(() -> partyValidator.validateParticipateParty(party, participant))
                        .isInstanceOf(ParticipantsFullException.class)
                        .hasMessage(String.format("참여 인원이 가득 찼습니다. 최대 참여 인원: %d명 partyId: %d", party.getMaxParticipants(), party.getId()));
            }
        }
    }
}
