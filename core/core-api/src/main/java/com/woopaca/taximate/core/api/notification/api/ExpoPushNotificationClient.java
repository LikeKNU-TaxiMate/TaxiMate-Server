package com.woopaca.taximate.core.api.notification.api;

import com.woopaca.taximate.core.domain.notification.PushNotification;
import com.woopaca.taximate.core.domain.notification.PushNotificationClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class ExpoPushNotificationClient implements PushNotificationClient {

    private final RestClient restClient;

    public ExpoPushNotificationClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public void sendNotifications(PushNotification pushNotification, List<String> tokens) {
        ExpoPushNotification expoPushNotification = ExpoPushNotification.of(pushNotification, tokens);
        restClient.post()
                .uri("https://exp.host/--/api/v2/push/send")
                .header(HttpHeaders.HOST, "exp.host")
                .header(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate")
                .body(expoPushNotification)
                .retrieve();
    }
}
