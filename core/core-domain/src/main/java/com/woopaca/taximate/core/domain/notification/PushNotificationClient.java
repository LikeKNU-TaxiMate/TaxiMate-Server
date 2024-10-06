package com.woopaca.taximate.core.domain.notification;

import java.util.List;

public interface PushNotificationClient {

    void sendNotifications(PushNotification pushNotification, List<String> tokens);
}
