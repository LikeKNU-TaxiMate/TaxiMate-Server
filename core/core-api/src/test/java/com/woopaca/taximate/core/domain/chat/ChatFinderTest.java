package com.woopaca.taximate.core.domain.chat;

import com.woopaca.taximate.core.domain.fixture.UserFixtures;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.ChatEntity;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;
import com.woopaca.taximate.storage.db.core.repository.ChatRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ChatFinderTest {

    @InjectMocks
    private ChatFinder chatFinder;

    @Mock
    private ChatRepository chatRepository;

    @Nested
    class findRecentMessage_메서드는 {

        @Test
        void 최근_메시지를_찾아서_반환한다() {
            // given
            UserEntity userEntity = UserFixtures.createUserEntity();
            ChatEntity chatEntity = ChatEntity.builder()
                    .message("message")
                    .type(MessageType.MESSAGE.name())
                    .user(userEntity)
                    .build();

            long partyId = 1L;
            Party party = Party.builder()
                    .id(partyId)
                    .build();
            when(chatRepository.findTopByPartyId(eq(partyId), any(Sort.class))).thenReturn(Optional.of(chatEntity));

            // when
            Chat chat = chatFinder.findRecentMessage(party);

            // then
            assertThat(chat).isNotNull();
            assertThat(chat.getMessage()).isEqualTo(chatEntity.getMessage());
        }

        @Test
        void 최근_메시지가_없을_경우_빈_메시지를_반환한다() {
            // given
            long partyId = 1L;
            Party party = Party.builder()
                    .id(partyId)
                    .build();
            when(chatRepository.findTopByPartyId(eq(partyId), any(Sort.class))).thenReturn(Optional.empty());

            // when
            Chat chat = chatFinder.findRecentMessage(party);

            // then
            assertThat(chat).isNotNull();
            assertThat(chat.getMessage()).isEmpty();
            assertThat(chat.getSentAt()).isEqualTo(LocalDateTime.MIN);
        }
    }

    @Nested
    class calculateUnreadCount_메서드는 {

        @Test
        void 읽지_않은_메시지_개수를_계산한다() {
            // given
            long partyId = 1L;
            long userId = 1L;
            Party party = Party.builder()
                    .id(partyId)
                    .build();
            User user = UserFixtures.createUser(userId);
            when(chatRepository.countByLastChatId(eq(partyId), eq(userId))).thenReturn(3);

            // when
            int unreadCount = chatFinder.calculateUnreadCount(party, user);

            // then
            assertThat(unreadCount).isEqualTo(3);
        }

        @Test
        void 읽지_않은_메시지가_없을_경우_0을_반환한다() {
            // given
            long partyId = 1L;
            long userId = 1L;
            Party party = Party.builder()
                    .id(partyId)
                    .build();
            User user = UserFixtures.createUser(userId);
            when(chatRepository.countByLastChatId(eq(partyId), eq(userId))).thenReturn(0);

            // when
            int unreadCount = chatFinder.calculateUnreadCount(party, user);

            // then
            assertThat(unreadCount).isZero();
        }
    }

    @Nested
    class findChats_메서드는 {

        @Test
        void 팟의_메시지_목록을_반환한다() {
            // given
            UserEntity userEntity = UserFixtures.createUserEntity();
            ChatEntity chatEntity1 = ChatEntity.builder()
                    .message("message1")
                    .type(MessageType.MESSAGE.name())
                    .user(userEntity)
                    .build();
            ChatEntity chatEntity2 = ChatEntity.builder()
                    .message("message2")
                    .type(MessageType.MESSAGE.name())
                    .user(userEntity)
                    .build();

            long partyId = 1L;
            Party party = Party.builder()
                    .id(partyId)
                    .build();
            when(chatRepository.findByPartyId(eq(partyId), any(Sort.class))).thenReturn(List.of(chatEntity1, chatEntity2));

            // when
            List<Chat> chats = chatFinder.findChats(party);

            // then
            assertThat(chats).hasSize(2);
            assertThat(chats.get(0).getMessage()).isEqualTo(chatEntity1.getMessage());
            assertThat(chats.get(1).getMessage()).isEqualTo(chatEntity2.getMessage());
        }

        @Test
        void 메시지가_없을_경우_빈_목록을_반환한다() {
            // given
            long partyId = 1L;
            Party party = Party.builder()
                    .id(partyId)
                    .build();
            when(chatRepository.findByPartyId(eq(partyId), any(Sort.class))).thenReturn(List.of());

            // when
            List<Chat> chats = chatFinder.findChats(party);

            // then
            assertThat(chats).isEmpty();
        }
    }
}
