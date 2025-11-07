package com.example.demo.singleton;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    private final Map<String, String> sessions = new ConcurrentHashMap<>();
    // optional TTL maps if you want expiry:
    private final Map<String, Long> sessionExpiry = new ConcurrentHashMap<>();
    private static final long SESSION_TTL_MS = 30 * 60 * 1000; // 30 minutes

    public String createSession(String userIdentifier) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, userIdentifier);
        sessionExpiry.put(sessionId, System.currentTimeMillis() + SESSION_TTL_MS);
        return sessionId;
    }

    public boolean isValid(String id) {
        if (id == null) return false;
        Long expiry = sessionExpiry.get(id);
        if (expiry == null) return sessions.containsKey(id);
        if (expiry < System.currentTimeMillis()) {
            removeSession(id);
            return false;
        }
        return sessions.containsKey(id);
    }

    public String getUser(String id) {
        return sessions.get(id);
    }

    public void removeSession(String id) {
        sessions.remove(id);
        sessionExpiry.remove(id);
    }
}
