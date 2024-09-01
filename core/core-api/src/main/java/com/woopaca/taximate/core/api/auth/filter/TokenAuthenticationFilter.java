package com.woopaca.taximate.core.api.auth.filter;

import com.woopaca.taximate.core.api.auth.token.JwtProvider;
import com.woopaca.taximate.core.api.user.domain.User;
import com.woopaca.taximate.core.api.user.domain.UserFinder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";

    private final JwtProvider jwtProvider;
    private final UserFinder userFinder;

    public TokenAuthenticationFilter(JwtProvider jwtProvider, UserFinder userFinder) {
        this.jwtProvider = jwtProvider;
        this.userFinder = userFinder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = extractToken(request);
        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        String subject = jwtProvider.verify(accessToken);
        User user = userFinder.findUserByEmail(subject)
                .orElseThrow();
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = UsernamePasswordAuthenticationToken
                .authenticated(user, null, Collections.emptyList());
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER)) {
            return authorizationHeader.substring(BEARER.length());
        }
        return null;
    }
}
