package com.woopaca.taximate.core.message;

import com.woopaca.taximate.core.domain.user.User;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public class HeaderAccessorManipulator {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String USER_ATTRIBUTE = "user";

    private final StompHeaderAccessor accessor;

    HeaderAccessorManipulator(StompHeaderAccessor accessor) {
        this.accessor = accessor;
    }

    public static HeaderAccessorManipulator wrap(StompHeaderAccessor accessor) {
        return new HeaderAccessorManipulator(accessor);
    }

    public void setUser(User user) {
        accessor.getSessionAttributes()
                .put(USER_ATTRIBUTE, user);
    }

    public User getUser() {
        return (User) accessor.getSessionAttributes()
                .get(USER_ATTRIBUTE);
    }

    public String getIdentifier() {
        String destination = accessor.getDestination();
        return destination.split(StompMessageSender.DESTINATION_PREFIX)[1];
    }

    public String getAuthorization() {
        return accessor.getFirstNativeHeader(AUTHORIZATION_HEADER);
    }
}
