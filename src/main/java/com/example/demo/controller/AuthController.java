package com.example.demo.controller;


import com.example.demo.singleton.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public static class LoginRequest {
        public String username;
        public String password;
        public LoginRequest() {}
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        logger.info("Login attempt for user={}", req.username);
// Hardcoded credentials
        if ("admin".equals(req.username) && "admin123".equals(req.password)) {
            String sessionId = UUID.randomUUID().toString();
            SessionManager.getInstance().addSession(sessionId);
            Map<String, String> resp = new HashMap<>();
            resp.put("sessionId", sessionId);
            return ResponseEntity.ok(resp);
        }
        Map<String, String> err = new HashMap<>();
        err.put("error", "Invalid credentials");
        return ResponseEntity.status(401).body(err);
    }
    @GetMapping("/login")
    public String showLoginPage() {
        return "Please use POST method to login with username and password";
    }

}