package com.woopaca.taximate.core.domain.party.model;

import com.woopaca.taximate.core.domain.error.exception.InvalidMapRangeException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MapBoundTest {

    @Nested
    class MapBound_객체를_생성할_때 {

        @Test
        void 지도_범위가_유효한_경우_객체가_생성된다() {
            //given
            double minLatitude = 37.0;
            double minLongitude = 127.0;
            double maxLatitude = 37.9;
            double maxLongitude = 127.9;

            //when
            MapBound mapBound = new MapBound(minLatitude, minLongitude, maxLatitude, maxLongitude);

            //then
            assertNotNull(mapBound);
        }

        @ParameterizedTest
        @MethodSource("provideInvalidMapBounds")
        void 지도_범위가_유효하지_않은_경우_예외가_발생한다(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
            //when & then
            assertThatThrownBy(() -> new MapBound(minLatitude, minLongitude, maxLatitude, maxLongitude))
                    .isInstanceOf(InvalidMapRangeException.class);
        }

        private static Arguments[] provideInvalidMapBounds() {
            return new Arguments[]{
                    Arguments.of(37.9, 127.0, 37.0, 127.9),
                    Arguments.of(37.0, 127.9, 37.9, 127.0)
            };
        }
    }
}