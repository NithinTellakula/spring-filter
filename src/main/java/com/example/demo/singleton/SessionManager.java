package com.example.demo.singleton;


import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class SessionManager {
    private static final SessionManager INSTANCE = new SessionManager();
    private final Set<String> sessions = ConcurrentHashMap.newKeySet();


    private SessionManager() {}


    public static SessionManager getInstance() {
        return INSTANCE;
    }


    public void addSession(String id) {
        sessions.add(id);
    }


    public boolean isValid(String id) {
        if (id == null) return false;
        return sessions.contains(id);
    }


    public void removeSession(String id) {
        sessions.remove(id);
    }
}