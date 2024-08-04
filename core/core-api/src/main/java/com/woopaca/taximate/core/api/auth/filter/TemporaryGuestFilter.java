package com.woopaca.taximate.core.api.auth.filter;

import com.woopaca.taximate.core.api.user.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TemporaryGuestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContextHolder.getContext()
                .setAuthentication(UsernamePasswordAuthenticationToken.unauthenticated(User.GUEST, null/*, Collections.emptyList()*/));
        filterChain.doFilter(request, response);
    }
}
