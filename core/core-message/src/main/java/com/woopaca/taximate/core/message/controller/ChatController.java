package com.woopaca.taximate.core.message.controller;

import com.woopaca.taximate.core.domain.chat.service.ChatService;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.core.message.HeaderAccessorManipulator;
import com.woopaca.taximate.core.message.dto.ReceivedChat;
import com.woopaca.taximate.core.message.dto.SendChat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/messages")
    public void sendMessage(@Validated @Payload SendChat sendChat, StompHeaderAccessor accessor) {
        HeaderAccessorManipulator accessorManipulator = HeaderAccessorManipulator.wrap(accessor);
        User sender = accessorManipulator.getUser();
        chatService.sendStandardMessage(sendChat.partyId(), sender, sendChat.message());
    }

    @MessageMapping("/received")
    public void receivedMessage(@Validated @Payload ReceivedChat receivedChat, StompHeaderAccessor accessor) {
        HeaderAccessorManipulator accessorManipulator = HeaderAccessorManipulator.wrap(accessor);
        User receiver = accessorManipulator.getUser();
        chatService.receiveMessage(receivedChat.partyId(), receivedChat.chatId(), receiver);
    }
}
