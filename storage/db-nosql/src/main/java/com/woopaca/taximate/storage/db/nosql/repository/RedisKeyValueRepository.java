package com.woopaca.taximate.storage.db.nosql.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Slf4j
@Repository
public class RedisKeyValueRepository implements KeyValueRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisKeyValueRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Async
    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Async
    @Override
    public void set(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Async
    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
