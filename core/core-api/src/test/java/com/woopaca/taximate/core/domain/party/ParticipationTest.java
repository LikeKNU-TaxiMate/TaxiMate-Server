package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.core.domain.fixture.ParticipationFixtures;
import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParticipationTest {

    @Nested
    class isHost_메서드는 {

        @Test
        void 참여자_역할이_호스트인_경우_true를_반환한다() {
            //given
            Participation participation = Participation.builder()
                    .role(Participation.ParticipationRole.HOST)
                    .build();

            //when
            boolean isHost = participation.isHost();

            //then
            assertThat(isHost).isTrue();
        }

        @Test
        void 참여자_역할이_호스트가_아닌_경우_false를_반환한다() {
            //given
            Participation participation = Participation.builder()
                    .role(Participation.ParticipationRole.PARTICIPANT)
                    .build();

            //when
            boolean isHost = participation.isHost();

            //then
            assertThat(isHost).isFalse();
        }
    }

    @Nested
    class isParticipating_메서드는 {

        @Test
        void 참여자_상태가_참여중인_경우_true를_반환한다() {
            //given
            Participation participation = Participation.builder()
                    .status(Participation.ParticipationStatus.PARTICIPATING)
                    .build();

            //when
            boolean isParticipating = participation.isParticipating();

            //then
            assertThat(isParticipating).isTrue();
        }

        @Test
        void 참여자_상태가_참여중이_아닌_경우_false를_반환한다() {
            //given
            Participation participation = Participation.builder()
                    .status(Participation.ParticipationStatus.NONE)
                    .build();

            //when
            boolean isParticipating = participation.isParticipating();

            //then
            assertThat(isParticipating).isFalse();
        }
    }

    @Nested
    class fromEntity_메서드는 {

        @Test
        void ParticipationEntity로부터_Participation을_생성한다() {
            //given
            ParticipationEntity entity = ParticipationFixtures.createParticipationEntity();

            //when
            Participation participation = Participation.fromEntity(entity);

            //then
            assertThat(participation).isNotNull();
            assertThat(participation.getId()).isEqualTo(entity.getId());
            assertThat(participation.getRole()).isEqualTo(Participation.ParticipationRole.valueOf(entity.getRole()));
            assertThat(participation.getStatus()).isEqualTo(Participation.ParticipationStatus.valueOf(entity.getStatus()));
            assertThat(participation.getUserId()).isEqualTo(entity.getUserId());
        }
    }
}