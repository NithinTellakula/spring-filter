package com.example.demo.config;

import com.example.demo.singleton.SessionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final SessionManager sessionManager;
    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    public OAuth2SuccessHandler(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        // generate a random token (or map to JWT if you later add JWT)
        String token = UUID.randomUUID().toString();
        // use SessionManager to create a session id
        String sessionId = sessionManager.createSession(email);

        logger.info("OAuth2 login success for email={} | sessionId={}", email, sessionId);

        String redirectUrl = String.format(
                "http://localhost:8080/index.html?login=success&email=%s&token=%s&sessionId=%s",
                URLEncoder.encode(email, StandardCharsets.UTF_8),
                URLEncoder.encode(token, StandardCharsets.UTF_8),
                URLEncoder.encode(sessionId, StandardCharsets.UTF_8)
        );

        response.sendRedirect(redirectUrl);
    }
}
