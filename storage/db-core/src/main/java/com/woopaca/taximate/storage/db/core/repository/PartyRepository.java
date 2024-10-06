package com.woopaca.taximate.storage.db.core.repository;

import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<PartyEntity, Long>, PartyQueryDslRepository {

    @EntityGraph(attributePaths = {"participationSet"})
    @Query("""
            SELECT p
            FROM party p
            WHERE p.id = :id
            """)
    Optional<PartyEntity> findByIdWithParticipation(@Param("id") Long id);

    @EntityGraph(attributePaths = {"participationSet"})
    @Query("""
            SELECT p
            FROM party p
            WHERE p.id IN (
                    SELECT pt.party.id
                    FROM participation pt
                    WHERE pt.userId = :userId
            )
            """)
    List<PartyEntity> findByParticipationUserId(@Param("userId") Long userId);
}
