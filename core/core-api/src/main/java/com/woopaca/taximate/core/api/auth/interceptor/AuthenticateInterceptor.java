package com.woopaca.taximate.core.api.auth.interceptor;

import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.error.exception.NonexistentUserException;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.core.domain.user.UserFinder;
import com.woopaca.taximate.core.security.context.AuthenticationProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
public class AuthenticateInterceptor implements HandlerInterceptor {

    private final UserFinder userFinder;
    private final AuthenticationProvider authenticationProvider;

    public AuthenticateInterceptor(UserFinder userFinder, AuthenticationProvider authenticationProvider) {
        this.userFinder = userFinder;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String subject = authenticationProvider.getSubject();
        if (Objects.equals(subject, "anonymousUser")) {
            AuthenticatedUserHolder.setAuthenticatedUser(User.GUEST);
            return true;
        }

        User authenticatedUser = userFinder.findUserByEmail(subject)
                .orElseThrow(NonexistentUserException::new);
        AuthenticatedUserHolder.setAuthenticatedUser(authenticatedUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AuthenticatedUserHolder.clear();
    }
}
