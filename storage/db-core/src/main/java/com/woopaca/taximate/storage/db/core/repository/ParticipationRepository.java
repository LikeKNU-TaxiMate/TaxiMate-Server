package com.woopaca.taximate.storage.db.core.repository;

import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<ParticipationEntity, Long> {

    @EntityGraph(attributePaths = {"party"})
    List<ParticipationEntity> findByUserId(Long userId);

    List<ParticipationEntity> findByPartyId(Long partyId);

    Optional<ParticipationEntity> findByPartyIdAndUserId(Long partyId, Long userId);

    @Modifying
    @Query("""
            UPDATE participation
            SET role = :role
            WHERE user.id = :userId AND party.id = :partyId
            """)
    void updateRole(@Param("userId") Long userId, @Param("partyId") Long partyId, @Param("role") String role);
}
