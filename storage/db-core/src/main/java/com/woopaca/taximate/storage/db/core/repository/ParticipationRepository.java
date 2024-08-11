package com.woopaca.taximate.storage.db.core.repository;

import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<ParticipationEntity, Long> {

    @EntityGraph(attributePaths = {"party"})
    List<ParticipationEntity> findByUserIdAndRole(Long userId, String role);
}
