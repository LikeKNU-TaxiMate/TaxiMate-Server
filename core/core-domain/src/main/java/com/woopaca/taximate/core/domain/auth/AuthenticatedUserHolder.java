package com.woopaca.taximate.core.domain.auth;

import com.woopaca.taximate.core.domain.user.User;

public final class AuthenticatedUserHolder {

    private static final ThreadLocal<User> contextHolder = new ThreadLocal<>();

    private AuthenticatedUserHolder() {
    }

    public static void setAuthenticatedUser(User user) {
        contextHolder.set(user);
    }

    public static User getAuthenticatedUser() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}
