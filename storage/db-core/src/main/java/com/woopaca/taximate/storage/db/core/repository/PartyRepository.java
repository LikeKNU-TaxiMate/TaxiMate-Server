package com.woopaca.taximate.storage.db.core.repository;

import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<PartyEntity, Long>, PartyQueryDslRepository {

    @EntityGraph(attributePaths = {"participationSet", "participationSet.user"})
    @Query("""
            SELECT p
            FROM party p
            WHERE p.id = :id
            """)
    Optional<PartyEntity> findByIdWithParticipation(@Param("id") Long id);

    @EntityGraph(attributePaths = {"participationSet", "participationSet.user"})
    @Query("""
            SELECT p
            FROM party p
            WHERE p.id IN (
                    SELECT pt.party.id
                    FROM participation pt
                    WHERE pt.user.id = :userId AND pt.status = 'PARTICIPATING'
            )
            """)
    List<PartyEntity> findByParticipationUserId(@Param("userId") Long userId);

    @Modifying
    @Query("""
            UPDATE party p
            SET p.views = p.views + 1
            WHERE p.id = :id
            """)
    void increaseViews(Long id);
}
