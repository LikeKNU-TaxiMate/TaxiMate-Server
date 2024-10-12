package com.woopaca.taximate.core.domain.chat;

import com.woopaca.taximate.storage.db.nosql.repository.KeyValueRepository;
import org.springframework.stereotype.Component;

@Component
public class WebSocketSessions {

    private static final String KEY_PREFIX = "websocket";

    private final KeyValueRepository keyValueRepository;

    public WebSocketSessions(KeyValueRepository keyValueRepository) {
        this.keyValueRepository = keyValueRepository;
    }

    public void addSession(String identifier, Long userId) {
        String key = generateKey(userId);
        keyValueRepository.set(key, identifier);
    }

    public void removeSession(Long userId) {
        String key = generateKey(userId);
        keyValueRepository.remove(key);
    }

    public String getSession(Long userId) {
        String key = generateKey(userId);
        return keyValueRepository.get(key);
    }

    private String generateKey(Long userId) {
        return String.join(":", KEY_PREFIX, String.valueOf(userId));
    }
}
