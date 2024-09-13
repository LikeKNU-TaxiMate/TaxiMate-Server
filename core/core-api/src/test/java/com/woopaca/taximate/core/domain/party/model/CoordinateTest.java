package com.woopaca.taximate.core.domain.party.model;

import com.woopaca.taximate.core.domain.error.exception.CoordinateOutOfRangeException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CoordinateTest {

    @Nested
    class Coordinate_객체를_생성할_때 {

        @Test
        void 좌표_범위가_유효한_경우_객체가_생성된다() {
            //given
            double latitude = 37.123456;
            double longitude = 127.123456;

            //when
            Coordinate coordinate = Coordinate.of(latitude, longitude);

            //then
            assertThat(coordinate).isNotNull();
        }

        @ParameterizedTest
        @MethodSource("provideInvalidCoordinates")
        void 좌표_범위를_벗어나는_경우_예외가_발생한다(double latitude, double longitude) {
            //when & then
            assertThatThrownBy(() -> Coordinate.of(latitude, longitude))
                    .isInstanceOf(CoordinateOutOfRangeException.class);
        }

        private static Arguments[] provideInvalidCoordinates() {
            return new Arguments[]{
                    Arguments.of(-90.1, 127.123456),
                    Arguments.of(90.1, 127.123456),
                    Arguments.of(37.123456, -180.1),
                    Arguments.of(37.123456, 180.1)
            };
        }
    }
}
