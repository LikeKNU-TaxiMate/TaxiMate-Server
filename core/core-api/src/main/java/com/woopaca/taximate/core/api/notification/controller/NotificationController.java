package com.woopaca.taximate.core.api.notification.controller;

import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.api.common.model.ApiResults.ApiResponse;
import com.woopaca.taximate.core.domain.notification.PushTokenAppender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/notifications")
@RestController
public class NotificationController {

    private final PushTokenAppender pushTokenAppender;

    public NotificationController(PushTokenAppender pushTokenAppender) {
        this.pushTokenAppender = pushTokenAppender;
    }

    @PostMapping("/tokens")
    public ApiResponse<String> registerToken(@RequestBody String pushToken) {
        pushTokenAppender.appendToken(pushToken);
        return ApiResults.success(pushToken);
    }
}
