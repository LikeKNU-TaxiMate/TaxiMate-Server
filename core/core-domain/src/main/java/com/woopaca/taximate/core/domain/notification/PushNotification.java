package com.woopaca.taximate.core.domain.notification;

import java.util.Map;

public record PushNotification(String title, String subtitle, String body, int badge, Map<String, Object> data) {

    public PushNotification {
        title = String.join(" ", "🚖", title);
    }
}
