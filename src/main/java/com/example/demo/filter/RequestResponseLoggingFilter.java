package com.example.demo.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;


import java.io.IOException;


@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);


        try {
            logger.info("Incoming Request: method={} uri={}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            int status = wrappedResponse.getStatus();
            logger.info("Outgoing Response: method={} uri={} status={}", request.getMethod(), request.getRequestURI(), status);
// important: copy body to original response
            wrappedResponse.copyBodyToResponse();
        }
    }
}