package com.woopaca.taximate.core.domain.user;

import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.fixture.UserFixtures;
import com.woopaca.taximate.core.domain.user.User.AccountStatus;
import com.woopaca.taximate.core.domain.user.User.OAuth2Provider;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserTest {

    @Nested
    class isCurrentUser_메서드는 {

        @Test
        void 현재_사용자와_같은_사용자인_경우_true를_반환한다() {
            //given
            User user = UserFixtures.createUser(1L);
            AuthenticatedUserHolder.setAuthenticatedUser(user);

            //when
            boolean isCurrentUser = user.isCurrentUser();

            //then
            assertTrue(isCurrentUser);
        }

        @Test
        void 현재_사용자와_다른_사용자인_경우_false를_반환한다() {
            //given
            User user = UserFixtures.createUser(1L);
            User other = UserFixtures.createUser(2L);
            AuthenticatedUserHolder.setAuthenticatedUser(other);

            //when
            boolean isCurrentUser = user.isCurrentUser();

            //then
            assertFalse(isCurrentUser);
        }
    }

    @Nested
    class fromEntity_메서드는 {

        @Test
        void UserEntity를_User로_변환한다() {
            //given
            UserEntity userEntity = UserFixtures.createUserEntityWith("test1");

            //when
            User user = User.fromEntity(userEntity);

            //then
            assertThat(user.getId()).isEqualTo(userEntity.getId());
            assertThat(user.getEmail()).isEqualTo(userEntity.getEmail());
            assertThat(user.getNickname()).isEqualTo(userEntity.getNickname());
            assertThat(user.getProfileImage()).isEqualTo(userEntity.getProfileImage());
            assertThat(user.getProvider()).isEqualTo(OAuth2Provider.valueOf(userEntity.getProvider()));
            assertThat(user.getStatus()).isEqualTo(AccountStatus.valueOf(userEntity.getStatus()));
        }
    }
}
