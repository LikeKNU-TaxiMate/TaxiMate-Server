package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.core.domain.fixture.AddressFixtures;
import com.woopaca.taximate.core.domain.fixture.ParticipationFixtures;
import com.woopaca.taximate.core.domain.fixture.PartyFixtures;
import com.woopaca.taximate.core.domain.fixture.UserFixtures;
import com.woopaca.taximate.core.domain.local.model.Address;
import com.woopaca.taximate.core.domain.party.Participation.ParticipationStatus;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PartyTest {

    @Nested
    class currentParticipantsCount_메서드는 {

        @Test
        void 현재_참여_중인_참여자_수를_반환한다() {
            //given
            Set<Participation> participationSet = Set.of(
                    Participation.builder().id(1L).status(ParticipationStatus.PARTICIPATING).build(),
                    Participation.builder().id(2L).status(ParticipationStatus.PARTICIPATING).build(),
                    Participation.builder().id(3L).status(ParticipationStatus.LEFT).build()
            );
            Party party = Party.builder()
                    .participationSet(participationSet)
                    .build();

            //when
            int currentParticipants = party.currentParticipantsCount();

            //then
            assertThat(currentParticipants).isEqualTo(2);
        }
    }

    @Nested
    class hostId_메서드는 {

        @Test
        void 팟_호스트의_아이디를_반환한다() {
            //given
            final long expectedHostId = 1L;
            Participation hostParticipation = ParticipationFixtures.createHostParticipationOf(expectedHostId);
            Party party = Party.builder()
                    .participationSet(Set.of(hostParticipation))
                    .build();

            //when
            User host = party.getHost();

            //then
            assertThat(host.getId()).isEqualTo(expectedHostId);
        }
    }

    @Nested
    class participationStatusOf_메서드는 {

        @Test
        void 참여자가_팟에_참여중인_경우_PARTICIPATING을_반환한다() {
            //given
            final long userId = 1L;
            User user = UserFixtures.createUser(userId);
            Participation participation = ParticipationFixtures.createParticipantParticipationOf(userId);
            Party party = Party.builder()
                    .departureTime(LocalDateTime.now())
                    .participationSet(Set.of(participation))
                    .build();

            //when
            ParticipationStatus participationStatus = party.participationStatusOf(user);

            //then
            assertThat(participationStatus).isEqualTo(ParticipationStatus.PARTICIPATING);
        }

        @Test
        void 참여자가_팟에_참여중이_아닌_경우_NONE을_반환한다() {
            //given
            final long userId = 1L;
            User user = UserFixtures.createUser(userId);
            Party party = Party.builder()
                    .departureTime(LocalDateTime.now())
                    .participationSet(Collections.emptySet())
                    .build();

            //when
            ParticipationStatus participationStatus = party.participationStatusOf(user);

            //then
            assertThat(participationStatus).isEqualTo(ParticipationStatus.NONE);
        }

        @Test
        void 팟이_종료된_경우_TERMINATED를_반환한다() {
            //given
            final long userId = 1L;
            User user = UserFixtures.createUser(userId);
            Participation participation = ParticipationFixtures.createParticipantParticipationOf(userId);
            Party party = Party.builder()
                    .departureTime(LocalDateTime.now().minusMinutes(31))
                    .participationSet(Set.of(participation))
                    .build();

            //when
            ParticipationStatus participationStatus = party.participationStatusOf(user);

            //then
            assertThat(participationStatus).isEqualTo(ParticipationStatus.TERMINATED);
        }
    }

    @Nested
    class isProgress_메서드는 {

        @Test
        void 출발_시간이_30분_이상_지나지_않았으면_true를_반환한다() {
            //given
            Party party = Party.builder()
                    .departureTime(LocalDateTime.now().plusMinutes(29))
                    .build();

            //when
            boolean isProgress = party.isProgress();

            //then
            assertThat(isProgress).isTrue();
        }

        @Test
        void 출발_시간이_30분_이상_지났으면_false를_반환한다() {
            //given
            Party party = Party.builder()
                    .departureTime(LocalDateTime.now().minusMinutes(31))
                    .build();

            //when
            boolean isProgress = party.isProgress();

            //then
            assertThat(isProgress).isFalse();
        }
    }

    @Nested
    class allocateAddress_메서드는 {

        @Test
        void 출발지와_도착지_주소를_할당한다() {
            //given
            Party party = Party.builder()
                    .build();
            Address originAddress = AddressFixtures.createAddress();
            Address destinationAddress = AddressFixtures.createAddress();

            //when
            Party allocatedParty = party.allocateAddress(originAddress, destinationAddress);

            //then
            assertThat(allocatedParty.getOrigin()).isEqualTo(AddressFixtures.TEST_BUILDING_NAME);
            assertThat(allocatedParty.getDestination()).isEqualTo(AddressFixtures.TEST_BUILDING_NAME);
            assertThat(allocatedParty.getOriginAddress()).isEqualTo(AddressFixtures.TEST_ROAD_ADDRESS);
            assertThat(allocatedParty.getDestinationAddress()).isEqualTo(AddressFixtures.TEST_ROAD_ADDRESS);
        }
    }

    @Nested
    class fromEntity_메서드는 {

        @Test
        void PartyEntity로부터_Party를_생성한다() {
            //given
            PartyEntity partyEntity = PartyFixtures.createPartyEntity();

            //when
            Party party = Party.fromEntity(partyEntity);

            //then
            assertThat(party.getTitle()).isEqualTo(partyEntity.getTitle());
            assertThat(party.getExplanation()).isEqualTo(partyEntity.getExplanation());
            assertThat(party.getDepartureTime()).isEqualTo(partyEntity.getDepartureTime());
            assertThat(party.getOrigin()).isEqualTo(partyEntity.getOrigin());
            assertThat(party.getOriginAddress()).isEqualTo(partyEntity.getOriginAddress());
            assertThat(party.getOriginLocation().latitude()).isEqualTo(partyEntity.getOriginLatitude());
            assertThat(party.getOriginLocation().longitude()).isEqualTo(partyEntity.getOriginLongitude());
            assertThat(party.getDestination()).isEqualTo(partyEntity.getDestination());
            assertThat(party.getDestinationAddress()).isEqualTo(partyEntity.getDestinationAddress());
            assertThat(party.getDestinationLocation().latitude()).isEqualTo(partyEntity.getDestinationLatitude());
            assertThat(party.getDestinationLocation().longitude()).isEqualTo(partyEntity.getDestinationLongitude());
            assertThat(party.getMaxParticipants()).isEqualTo(partyEntity.getMaxParticipants());
            assertThat(party.getViews()).isEqualTo(partyEntity.getViews());
            assertThat(party.getParticipationSet()).isEmpty();
        }
    }

    @Nested
    class fromEntityExcludeParticipants_메서드는 {

        @Test
        void partyEntity로부터_참여자를_제외한_Party를_생성한다() {
            //given
            PartyEntity partyEntity = PartyFixtures.createPartyEntity();

            //when
            Party party = Party.fromEntityExcludeParticipants(partyEntity);

            //then
            assertThat(party.getTitle()).isEqualTo(partyEntity.getTitle());
            assertThat(party.getExplanation()).isEqualTo(partyEntity.getExplanation());
            assertThat(party.getDepartureTime()).isEqualTo(partyEntity.getDepartureTime());
            assertThat(party.getOrigin()).isEqualTo(partyEntity.getOrigin());
            assertThat(party.getOriginAddress()).isEqualTo(partyEntity.getOriginAddress());
            assertThat(party.getOriginLocation().latitude()).isEqualTo(partyEntity.getOriginLatitude());
            assertThat(party.getOriginLocation().longitude()).isEqualTo(partyEntity.getOriginLongitude());
            assertThat(party.getDestination()).isEqualTo(partyEntity.getDestination());
            assertThat(party.getDestinationAddress()).isEqualTo(partyEntity.getDestinationAddress());
            assertThat(party.getDestinationLocation().latitude()).isEqualTo(partyEntity.getDestinationLatitude());
            assertThat(party.getDestinationLocation().longitude()).isEqualTo(partyEntity.getDestinationLongitude());
            assertThat(party.getMaxParticipants()).isEqualTo(partyEntity.getMaxParticipants());
            assertThat(party.getViews()).isEqualTo(partyEntity.getViews());
            assertThat(party.getParticipationSet()).isNull();
        }
    }
}
