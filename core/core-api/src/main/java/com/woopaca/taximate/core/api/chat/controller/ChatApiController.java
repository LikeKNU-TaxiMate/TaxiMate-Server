package com.woopaca.taximate.core.api.chat.controller;

import com.woopaca.taximate.core.api.chat.dto.ChatListResponse;
import com.woopaca.taximate.core.api.chat.dto.ChatRoomResponse;
import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.api.common.model.ApiResults.ApiResponse;
import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.service.ChatService;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.PartyFinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/chats")
@RestController
public class ChatApiController {

    private final ChatService chatService;
    private final PartyFinder partyFinder;

    public ChatApiController(ChatService chatService, PartyFinder partyFinder) {
        this.chatService = chatService;
        this.partyFinder = partyFinder;
    }

    @GetMapping
    public ApiResponse<List<ChatRoomResponse>> chatRoomList() {
        List<ChatRoomResponse> response = chatService.getChatRoomList()
                .stream()
                .map(ChatRoomResponse::from)
                .toList();
        return ApiResults.success(response);
    }

    @GetMapping("/{partyId}")
    public ApiResponse<ChatListResponse> chatList(@PathVariable("partyId") Long partyId) {
        Party party = partyFinder.findParty(partyId);
        List<Chat> chats = chatService.getChats(party);
        ChatListResponse response = ChatListResponse.of(party, chats);
        return ApiResults.success(response);
    }
}
