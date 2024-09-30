package com.woopaca.taximate.core.message.event;

import com.woopaca.taximate.core.domain.chat.WebSocketSessions;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.core.message.HeaderAccessorManipulator;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
public class WebSocketEventListener {

    private final WebSocketSessions webSocketSessions;

    public WebSocketEventListener(WebSocketSessions webSocketSessions) {
        this.webSocketSessions = webSocketSessions;
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent subscribeEvent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(subscribeEvent.getMessage());
        HeaderAccessorManipulator accessorManipulator = HeaderAccessorManipulator.wrap(accessor);
        String identifier = accessorManipulator.getIdentifier();
        User user = accessorManipulator.getUser();
        webSocketSessions.addSession(identifier, user.getId());
    }

    @EventListener
    public void handleSessionUnsubscribeEvent(SessionUnsubscribeEvent unsubscribeEvent) {
        removeSession(unsubscribeEvent);
    }

    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent disconnectEvent) {
        removeSession(disconnectEvent);
    }

    private void removeSession(AbstractSubProtocolEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        HeaderAccessorManipulator accessorManipulator = HeaderAccessorManipulator.wrap(accessor);
        User user = accessorManipulator.getUser();
        if (user == null) {
            return;
        }
        webSocketSessions.removeSession(user.getId());
    }
}
