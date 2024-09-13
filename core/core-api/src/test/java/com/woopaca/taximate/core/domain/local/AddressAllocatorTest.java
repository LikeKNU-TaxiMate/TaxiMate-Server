package com.woopaca.taximate.core.domain.local;

import com.woopaca.taximate.core.domain.fixture.AddressFixtures;
import com.woopaca.taximate.core.domain.fixture.PartyFixtures;
import com.woopaca.taximate.core.domain.local.model.Address;
import com.woopaca.taximate.core.domain.party.Party;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressAllocatorTest {

    @InjectMocks
    private AddressAllocator addressAllocator;

    @Mock
    private KakaoLocalClientProxy kakaoLocalClient;

    @Nested
    class allocateAddress_메서드는 {

        @Test
        void 출발지와_도착지의_좌표를_변환하여_주소를_할당한다() {
            //given
            Address address = AddressFixtures.createAddress();
            when(kakaoLocalClient.requestConvertCoordinate(any())).thenReturn(CompletableFuture.completedFuture(address));
            Party party = PartyFixtures.createParty();

            //when
            addressAllocator.allocateAddress(party);

            //then
            assertThat(party.getOrigin()).isEqualTo(address.buildingName());
            assertThat(party.getDestination()).isEqualTo(address.buildingName());
            assertThat(party.getOriginAddress()).isEqualTo(address.roadAddress());
            assertThat(party.getDestinationAddress()).isEqualTo(address.roadAddress());
        }
    }
}