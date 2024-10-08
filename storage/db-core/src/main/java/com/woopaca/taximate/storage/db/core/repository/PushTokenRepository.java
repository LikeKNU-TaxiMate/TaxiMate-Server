package com.woopaca.taximate.storage.db.core.repository;

import com.woopaca.taximate.storage.db.core.entity.PushTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PushTokenRepository extends JpaRepository<PushTokenEntity, Long> {

    List<PushTokenEntity> findByUserIdIn(Collection<Long> ids);

    boolean existsByToken(String token);
}
