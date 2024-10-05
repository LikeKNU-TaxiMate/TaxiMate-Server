package com.woopaca.taximate.storage.db.core.repository;

import com.woopaca.taximate.storage.db.core.entity.ChatEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    Optional<ChatEntity> findTopBy(Sort sort);

    @EntityGraph(attributePaths = {"user"})
    Optional<ChatEntity> findTopByPartyId(Long partyId, Sort sort);

    @Query("""
            SELECT COUNT(*)
            FROM chat
            WHERE id > (SELECT lastChatId
                        FROM chat_read
                        WHERE partyId = :partyId
                          AND userId = :userId)
            AND partyId = :partyId
            """)
    int countByLastChatId(@Param("partyId") Long partyId, @Param("userId") Long userId);

    @EntityGraph(attributePaths = {"user"})
    List<ChatEntity> findByPartyId(Long partyId, Sort sort);
}
