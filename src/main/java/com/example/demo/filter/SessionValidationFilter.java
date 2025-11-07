package com.example.demo.filter;

import com.example.demo.singleton.SessionManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Order(2)
public class SessionValidationFilter implements Filter {

    private static final String SESSION_HEADER = "X-Session-Id";
    private final SessionManager sessionManager;
    private static final Logger logger = LoggerFactory.getLogger(SessionValidationFilter.class);

    public SessionValidationFilter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        logger.debug("SessionValidationFilter checking uri={}", uri);

        // Allow public endpoints and static resources
        if (
                uri.equals("/") ||
                        uri.startsWith("/index") ||
                        uri.startsWith("/login") ||
                        uri.startsWith("/oauth2") ||
                        uri.contains("login/oauth2") ||
                        uri.startsWith("/css") ||
                        uri.startsWith("/js") ||
                        uri.startsWith("/images") ||
                        uri.endsWith(".html") ||
                        uri.endsWith(".js") ||
                        uri.endsWith(".css")
        ) {
            chain.doFilter(request, response);
            return;
        }

        // Allow CORS preflight
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String sessionId = req.getHeader(SESSION_HEADER);
        logger.debug("Found session header {} for uri {}", sessionId, uri);

        if (sessionId == null || !sessionManager.isValid(sessionId)) {
            logger.warn("Unauthorized request to {} - missing/invalid session: {}", uri, sessionId);
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\": \"Unauthorized - invalid or missing session\"}");
            return;
        }

        logger.debug("Session valid (id={}) for uri={}", sessionId, uri);
        chain.doFilter(request, response);
    }
}
