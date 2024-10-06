package com.woopaca.taximate.core.domain.notification;

public record PushNotification(String title, String subtitle, String body, int badge) {

    public PushNotification {
        title = String.join(" ", "🚖", title);
    }
}
