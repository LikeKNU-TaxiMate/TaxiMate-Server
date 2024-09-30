package com.woopaca.taximate.core.api.chat.controller;

import com.woopaca.taximate.core.api.chat.dto.ChatResponse;
import com.woopaca.taximate.core.api.chat.dto.ChatResponse.ChatListResponse;
import com.woopaca.taximate.core.api.chat.dto.ChatRoomResponse;
import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.api.common.model.ApiResults.ApiResponse;
import com.woopaca.taximate.core.api.party.dto.response.PartiesResponse;
import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.ChatDataBase;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.PartyFinder;
import com.woopaca.taximate.core.domain.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/chat")
@RestController
public class ChatApiController {

    private final PartyFinder partyFinder;
    private final ChatDataBase chatDataBase;

    public ChatApiController(PartyFinder partyFinder, ChatDataBase chatDataBase) {
        this.partyFinder = partyFinder;
        this.chatDataBase = chatDataBase;
    }

    @GetMapping
    public ApiResponse<List<ChatRoomResponse>> chatRoomList() {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        List<ChatRoomResponse> response = partyFinder.findParticipatedAllParties(authenticatedUser)
                .stream()
                .map(party -> {
                    int unreadCount = chatDataBase.countOfUnread(authenticatedUser, party);
                    Chat recentChat = chatDataBase.readRecentChat(party)
                            .orElse(Chat.empty());
                    return new ChatRoomResponse(party.getId(), party.getTitle(), party.getMaxParticipants(),
                            party.currentParticipantsCount(), party.isProgress(), recentChat.getMessage(),
                            recentChat.getSentAt(), unreadCount);
                })
                .toList();
        return ApiResults.success(response);
    }

    @GetMapping("/{partyId}")
    public ApiResponse<ChatResponse> chatList(@PathVariable("partyId") Long partyId) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        Party party = partyFinder.findParty(partyId);
        List<ChatListResponse> chatListResponses = chatDataBase.readChat(authenticatedUser, party)
                .stream()
                .map(ChatListResponse::from)
                .toList();
        PartiesResponse partiesResponse = PartiesResponse.from(party);
        ChatResponse response = new ChatResponse(partiesResponse, chatListResponses);
        return ApiResults.success(response);
    }
}
