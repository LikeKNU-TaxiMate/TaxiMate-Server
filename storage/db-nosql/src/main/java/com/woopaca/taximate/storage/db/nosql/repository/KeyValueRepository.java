package com.woopaca.taximate.storage.db.nosql.repository;

import java.time.Duration;
import java.util.Map;

public interface KeyValueRepository {

    /**
     * 키-값 저장
     * @param key {@code null}이 아닌 문자열
     * @param value {@code }이 아닌 문자열
     * @see #set(String, String, Duration)
     */
    void set(String key, String value);

    /**
     * 키-값 저장
     * @param key {@code null}이 아닌 문자열
     * @param value {@code null}이 아닌 문자열
     * @param duration {@code null}일 수 없음
     *
     * @see #set(String, String)
     * @see java.time.Duration
     */
    void set(String key, String value, Duration duration);

    /**
     * 키로 값 조회
     * @param key {@code null}이 아닌 문자열
     * @return null일 수 있음
     */
    String get(String key);

    /**
     * 키로 값 삭제
     * @param key {@code null}이 아닌 문자열
     */
    void remove(String key);

    /**
     * 모든 키-값 조회
     * @return {@code null}일 수 없음
     */
    Map<String, String> getAll();
}
