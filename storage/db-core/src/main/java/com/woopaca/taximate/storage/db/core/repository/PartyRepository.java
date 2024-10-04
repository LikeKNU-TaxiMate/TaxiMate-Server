package com.woopaca.taximate.storage.db.core.repository;

import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<PartyEntity, Long>, PartyQueryDslRepository {

    @EntityGraph(attributePaths = {"participationSet"})
    @Query("""
            SELECT p
            FROM com.woopaca.taximate.storage.db.core.entity.PartyEntity p
            WHERE p.id = :id
            """)
    Optional<PartyEntity> findByIdWithParticipation(@Param("id") Long id);
}
