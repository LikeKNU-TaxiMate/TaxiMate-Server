package com.woopaca.taximate.storage.db.nosql.repository;

import java.time.Duration;

public interface KeyValueRepository {

    void set(String key, String value);

    void set(String key, String value, Duration duration);

    String get(String key);

    void remove(String key);
}
