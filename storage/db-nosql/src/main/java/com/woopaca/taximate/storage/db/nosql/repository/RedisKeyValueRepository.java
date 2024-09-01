package com.woopaca.taximate.storage.db.nosql.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

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
        Assert.notNull(key, "key는 null일 수 없습니다.");
        Assert.notNull(value, "value는 null일 수 없습니다.");
        redisTemplate.opsForValue().set(key, value);
    }

    @Async
    @Override
    public void set(String key, String value, Duration duration) {
        Assert.notNull(key, "key는 null일 수 없습니다.");
        Assert.notNull(value, "value는 null일 수 없습니다.");
        redisTemplate.opsForValue().set(key, value, duration);
    }

    @Override
    public String get(String key) {
        Assert.notNull(key, "key는 null일 수 없습니다.");
        return redisTemplate.opsForValue().get(key);
    }

    @Async
    @Override
    public void remove(String key) {
        Assert.notNull(key, "key는 null일 수 없습니다.");
        redisTemplate.delete(key);
    }
}
