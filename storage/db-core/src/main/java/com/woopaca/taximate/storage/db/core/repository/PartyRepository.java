package com.woopaca.taximate.storage.db.core.repository;

import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PartyRepository extends JpaRepository<PartyEntity, Long>, PartyQueryDslRepository {

    @EntityGraph(attributePaths = {"participationSet"})
    List<PartyEntity> findByIdIn(Collection<Long> ids);
}
