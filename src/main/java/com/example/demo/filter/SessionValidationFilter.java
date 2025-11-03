package com.example.demo.filter;

import com.example.demo.singleton.SessionManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SessionValidationFilter implements Filter {

    private static final String SESSION_HEADER = "X-Session-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        // ✅ Allow public endpoints (HTML, JS, CSS, and login)
        if (uri.equals("/login") ||
                uri.startsWith("/index") ||
                uri.startsWith("/css") ||
                uri.startsWith("/js") ||
                uri.startsWith("/images") ||
                uri.endsWith(".html") ||
                uri.endsWith(".js") ||
                uri.endsWith(".css")) {

            chain.doFilter(request, response);
            return;
        }

        // ✅ Validate session for protected endpoints
        String sessionId = req.getHeader(SESSION_HEADER);

        if (sessionId == null || !SessionManager.getInstance().isValid(sessionId)) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\": \"Unauthorized - invalid or missing session\"}");
            return;
        }

        chain.doFilter(request, response);
    }
}
