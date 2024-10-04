package com.woopaca.taximate.storage.db.core.repository;

import com.woopaca.taximate.storage.db.core.entity.ChatReadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatReadRepository extends JpaRepository<ChatReadEntity, Long> {

    Optional<ChatReadEntity> findByUserIdAndPartyId(Long userId, Long partyId);
}
