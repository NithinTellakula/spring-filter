package com.example.demo.controller;

import com.example.demo.singleton.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final SessionManager sessionManager;

    public AuthController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public static class LoginRequest {
        public String username;
        public String password;
        public LoginRequest() {}
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        logger.info("Login attempt for user={}", req.username);

        if ("admin".equals(req.username) && "admin123".equals(req.password)) {
            String sessionId = sessionManager.createSession(req.username);
            logger.info("Login successful for user={} | Session ID={}", req.username, sessionId);

            Map<String, String> resp = new HashMap<>();
            resp.put("message", "Login successful");
            resp.put("sessionId", sessionId);
            resp.put("user", req.username);

            return ResponseEntity.ok(resp);
        }

        Map<String, String> err = new HashMap<>();
        err.put("error", "Invalid credentials");
        return ResponseEntity.status(401).body(err);
    }

    @GetMapping("/session/validate")
    public ResponseEntity<?> validateSession(@RequestHeader(name = "X-Session-Id", required = false) String sid) {
        logger.info("Validate session request for sid={}", sid);
        boolean valid = sid != null && sessionManager.isValid(sid);
        Map<String, Object> resp = new HashMap<>();
        resp.put("valid", valid);
        if (valid) {
            resp.put("user", sessionManager.getUser(sid));
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "Please use POST method to login with username and password.";
    }
}
