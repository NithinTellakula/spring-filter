package com.example.demo.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
    public class UserController {

        @GetMapping("/user")
        public Map<String, Object> user(OAuth2AuthenticationToken token) {
            if (token == null) {
                return Map.of("error", "Not logged in");
            }
            var attrs = token.getPrincipal().getAttributes();
            return Map.of(
                    "name", attrs.get("name"),
                    "email", attrs.get("email")
            );
        }
    }


