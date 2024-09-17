package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.core.domain.error.exception.NonexistentPartyException;
import com.woopaca.taximate.core.domain.fixture.ParticipationFixtures;
import com.woopaca.taximate.core.domain.fixture.PartyFixtures;
import com.woopaca.taximate.core.domain.fixture.UserFixtures;
import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import com.woopaca.taximate.storage.db.core.repository.ParticipationRepository;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PartyFinderTest {

    @InjectMocks
    private PartyFinder partyFinder;

    @Mock
    private PartyRepository partyRepository;
    @Mock
    private ParticipationRepository participationRepository;

    @Nested
    class findParty_메서드는 {

        @Test
        void 팟_ID로_팟을_찾아서_반환한다() {
            // given
            final long partyId = 1L;
            when(partyRepository.findById(partyId)).thenReturn(Optional.of(PartyFixtures.createPartyEntity()));

            // when
            Party party = partyFinder.findParty(partyId);

            // then
            assertThat(party).isNotNull();
        }

        @Test
        void 존재하지_않는_팟_ID로_팟을_찾을_경우_예외를_던진다() {
            // given
            final long nonexistentId = -1L;
            when(partyRepository.findById(nonexistentId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> partyFinder.findParty(nonexistentId))
                    .isInstanceOf(NonexistentPartyException.class);
        }
    }

    @Nested
    class findPartyWithLock_메서드는 {

        @Test
        void 팟_ID로_팟을_찾아서_반환한다() {
            // given
            final long partyId = 1L;
            when(partyRepository.findByIdForUpdate(partyId)).thenReturn(Optional.of(PartyFixtures.createPartyEntity()));

            // when
            Party party = partyFinder.findPartyWithLock(partyId);

            // then
            assertThat(party).isNotNull();
        }

        @Test
        void 존재하지_않는_팟_ID로_팟을_찾을_경우_예외를_던진다() {
            // given
            final long nonexistentId = -1L;
            when(partyRepository.findByIdForUpdate(nonexistentId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> partyFinder.findPartyWithLock(nonexistentId))
                    .isInstanceOf(NonexistentPartyException.class);
        }
    }

    @Nested
    class findParticipatingParties_메서드는 {

        @Test
        void 참여중인_팟을_찾아서_반환한다() {
            // given
            final long userId = 1L;
            final int partiesSize = 2;
            List<ParticipationEntity> participationEntities = IntStream.rangeClosed(1, partiesSize)
                    .mapToObj(index -> ParticipationFixtures.createParticipationEntityWith(PartyFixtures.createPartyEntity(), userId))
                    .collect(Collectors.toList());
            participationEntities.add(ParticipationFixtures.createParticipationEntityWith(PartyFixtures.createTerminatedPartyEntity(), userId));
            when(participationRepository.findByUserId(userId)).thenReturn(participationEntities);

            // when
            var parties = partyFinder.findParticipatingParties(UserFixtures.createUser(userId));

            // then
            assertThat(parties).isNotEmpty();
            assertThat(parties).hasSize(partiesSize);
        }
    }
}