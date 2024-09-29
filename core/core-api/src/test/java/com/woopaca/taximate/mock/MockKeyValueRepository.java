package com.woopaca.taximate.mock;

import com.woopaca.taximate.storage.db.nosql.repository.KeyValueRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Profile("test")
@Repository
public class MockKeyValueRepository implements KeyValueRepository {

    private final Map<String, String> keyValueMap = new ConcurrentHashMap<>();

    @Override
    public void set(String key, String value) {
        keyValueMap.put(key, value);
    }

    @Override
    public void set(String key, String value, Duration duration) {
        keyValueMap.put(key, value);
    }

    @Override
    public String get(String key) {
        return keyValueMap.get(key);
    }

    @Override
    public void remove(String key) {
        keyValueMap.remove(key);
    }

    @Override
    public Map<String, String> getAll() {
        return keyValueMap;
    }
}
