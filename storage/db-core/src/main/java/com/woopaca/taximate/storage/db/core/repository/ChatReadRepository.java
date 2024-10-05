package com.woopaca.taximate.storage.db.core.repository;

import com.woopaca.taximate.storage.db.core.entity.ChatReadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatReadRepository extends JpaRepository<ChatReadEntity, Long> {

    void deleteByUserIdAndPartyId(Long userId, Long partyId);

    @Modifying
    @Query("""
            UPDATE chat_read
            SET lastChatId = :chatId
            WHERE userId = :userId AND partyId = :partyId
            AND lastChatId = :chatId
            """)
    void updateLastChatId(@Param("userId") Long userId, @Param("partyId") Long partyId, @Param("chatId") Long chatId);
}
