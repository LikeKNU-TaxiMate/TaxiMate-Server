package com.woopaca.taximate.core.api.notification.api;

import com.woopaca.taximate.core.domain.notification.PushNotification;
import lombok.Builder;

import java.util.List;

@Builder
public record ExpoPushNotification(List<String> to, String title, String subtitle, String body, String sound,
                                   int badge) {

    public ExpoPushNotification {
        sound = "default";
    }

    public static ExpoPushNotification of(PushNotification pushNotification, List<String> tokens) {
        return ExpoPushNotification.builder()
                .to(tokens)
                .title(pushNotification.title())
                .subtitle(pushNotification.subtitle())
                .body(pushNotification.body())
                .badge(pushNotification.badge())
                .build();
    }
}
