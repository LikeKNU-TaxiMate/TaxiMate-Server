package com.woopaca.taximate.storage.db.core.repository;

import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<ParticipationEntity, Long> {

    @EntityGraph(attributePaths = {"party"})
    List<ParticipationEntity> findByUserId(Long userId);

    List<ParticipationEntity> findByPartyId(Long partyId);

    @Query("""
            SELECT p.party.id
            FROM com.woopaca.taximate.storage.db.core.entity.ParticipationEntity p
            WHERE p.userId = :userId
            """)
    List<Long> findPartyIdByUserId(@Param("userId") Long userId);
}
