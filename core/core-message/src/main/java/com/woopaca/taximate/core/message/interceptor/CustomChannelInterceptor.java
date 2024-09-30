package com.woopaca.taximate.core.message.interceptor;

import com.woopaca.taximate.core.domain.error.exception.NonexistentUserException;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.core.domain.user.UserFinder;
import com.woopaca.taximate.core.message.HeaderAccessorManipulator;
import com.woopaca.taximate.core.security.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class CustomChannelInterceptor implements ChannelInterceptor {

    private static final String BEARER = "Bearer ";

    private final JwtProvider jwtProvider;
    private final UserFinder userFinder;

    public CustomChannelInterceptor(JwtProvider jwtProvider, UserFinder userFinder) {
        this.jwtProvider = jwtProvider;
        this.userFinder = userFinder;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        HeaderAccessorManipulator accessorManipulator = HeaderAccessorManipulator.wrap(accessor);
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String authorization = accessorManipulator.getAuthorization();
            String accessToken = extractToken(authorization);
            if (!StringUtils.hasText(accessToken)) {
                // TODO handle exception
                return null;
            }

            try {
                String subject = jwtProvider.verify(accessToken);
                User user = userFinder.findUserByEmail(subject)
                        .orElseThrow(NonexistentUserException::new);
                accessorManipulator.setUser(user);
            } catch (Exception e) {
                // TODO handle exception
                log.warn("JWT verify error", e);
                return null;
            }
        }
        return message;
    }

    private String extractToken(String authorization) {
        if (StringUtils.hasText(authorization) && authorization.startsWith(BEARER)) {
            return authorization.substring(BEARER.length());
        }
        return null;
    }
}
