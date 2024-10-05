package com.woopaca.taximate.core.domain.chat.service;

import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.ChatFinder;
import com.woopaca.taximate.core.domain.chat.ChatRoom;
import com.woopaca.taximate.core.domain.chat.MessageNotifier;
import com.woopaca.taximate.core.domain.error.exception.NotParticipatedPartyException;
import com.woopaca.taximate.core.domain.event.ChatEventProducer;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.PartyFinder;
import com.woopaca.taximate.core.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class ChatService {

    private final PartyFinder partyFinder;
    private final MessageNotifier messageNotifier;
    private final ChatEventProducer chatEventProducer;
    private final ChatFinder chatFinder;

    public ChatService(PartyFinder partyFinder, MessageNotifier messageNotifier, ChatEventProducer chatEventProducer, ChatFinder chatFinder) {
        this.partyFinder = partyFinder;
        this.messageNotifier = messageNotifier;
        this.chatEventProducer = chatEventProducer;
        this.chatFinder = chatFinder;
    }

    public void sendStandardMessage(Long partyId, User sender, String message) {
        Party party = partyFinder.findParty(partyId);
        if (!party.isParticipated(sender)) {
            throw new NotParticipatedPartyException();
        }
        Chat chat = Chat.standardMessage(party, sender, message);
        chatEventProducer.publishChatEvent(chat);
        messageNotifier.notify(chat);
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> getChatRoomList() {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        List<Party> participatedParties = partyFinder.findAllParticipatedParty(authenticatedUser);
        return participatedParties.stream()
                .map(party -> {
                    Chat recentMessage = chatFinder.findRecentMessage(party);
                    int unreadCount = chatFinder.calculateUnreadCount(party, authenticatedUser);
                    return new ChatRoom(party, recentMessage, unreadCount);
                })
                .sorted(Comparator.comparing(ChatRoom::isProgress)
                        .thenComparing(ChatRoom::getRecentMessageTime).reversed())
                .toList();
    }
}
