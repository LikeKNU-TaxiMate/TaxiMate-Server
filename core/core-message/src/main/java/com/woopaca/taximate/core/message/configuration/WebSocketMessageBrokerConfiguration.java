package com.woopaca.taximate.core.message.configuration;

import com.woopaca.taximate.core.message.interceptor.CustomChannelInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfiguration implements WebSocketMessageBrokerConfigurer {

    private static final int HEARTBEAT_INTERVAL = 5_000;

    private final CustomChannelInterceptor customChannelInterceptor;

    private TaskScheduler messageBrokerTaskScheduler;

    public WebSocketMessageBrokerConfiguration(CustomChannelInterceptor customChannelInterceptor) {
        this.customChannelInterceptor = customChannelInterceptor;
    }

    @Autowired
    public void setMessageBrokerTaskScheduler(@Lazy TaskScheduler taskScheduler) {
        this.messageBrokerTaskScheduler = taskScheduler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app")
                .enableSimpleBroker("/topic", "/queue")
                .setHeartbeatValue(new long[]{HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL})
                .setTaskScheduler(messageBrokerTaskScheduler);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(customChannelInterceptor);
    }
}
