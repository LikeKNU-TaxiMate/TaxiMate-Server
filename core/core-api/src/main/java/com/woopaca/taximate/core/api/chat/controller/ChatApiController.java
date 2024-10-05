package com.woopaca.taximate.core.api.chat.controller;

import com.woopaca.taximate.core.api.chat.dto.ChatRoomResponse;
import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.api.common.model.ApiResults.ApiResponse;
import com.woopaca.taximate.core.domain.chat.service.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/chat")
@RestController
public class ChatApiController {

    private final ChatService chatService;

    public ChatApiController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ApiResponse<List<ChatRoomResponse>> chatRoomList() {
        List<ChatRoomResponse> response = chatService.getChatRoomList()
                .stream()
                .map(ChatRoomResponse::from)
                .toList();
        return ApiResults.success(response);
    }
}
