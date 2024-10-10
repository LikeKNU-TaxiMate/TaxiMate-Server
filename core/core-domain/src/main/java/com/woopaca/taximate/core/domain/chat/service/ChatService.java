package com.woopaca.taximate.core.domain.chat.service;

import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.ChatFinder;
import com.woopaca.taximate.core.domain.chat.ChatReadRecorder;
import com.woopaca.taximate.core.domain.chat.ChatRoom;
import com.woopaca.taximate.core.domain.chat.MessageNotifier;
import com.woopaca.taximate.core.domain.error.exception.NotParticipatedPartyException;
import com.woopaca.taximate.core.domain.event.ChatEventPublisher;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.PartyFinder;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.repository.ChatReadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ChatService {

    private final PartyFinder partyFinder;
    private final MessageNotifier messageNotifier;
    private final ChatEventPublisher chatEventPublisher;
    private final ChatFinder chatFinder;
    private final ChatReadRecorder chatReadRecorder;
    private final ChatReadRepository chatReadRepository;

    public ChatService(PartyFinder partyFinder, MessageNotifier messageNotifier, ChatEventPublisher chatEventPublisher, ChatFinder chatFinder, ChatReadRecorder chatReadRecorder, ChatReadRepository chatReadRepository) {
        this.partyFinder = partyFinder;
        this.messageNotifier = messageNotifier;
        this.chatEventPublisher = chatEventPublisher;
        this.chatFinder = chatFinder;
        this.chatReadRecorder = chatReadRecorder;
        this.chatReadRepository = chatReadRepository;
    }

    public void sendStandardMessage(Long partyId, User sender, String message) {
        Party party = partyFinder.findParty(partyId);
        if (!party.isParticipated(sender)) {
            throw new NotParticipatedPartyException();
        }
        Chat chat = Chat.standardMessage(party, sender, message);
        chatEventPublisher.publishChatEvent(chat);
        messageNotifier.notify(chat);
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> getChatRoomList() {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        return partyFinder.findParticipatingParties(authenticatedUser)
                .stream()
                .map(party -> {
                    Chat recentMessage = chatFinder.findRecentMessage(party);
                    int unreadCount = chatFinder.calculateUnreadCount(party, authenticatedUser);
                    return new ChatRoom(party, recentMessage, unreadCount);
                })
                .sorted(Comparator.comparing(ChatRoom::isProgress)
                        .thenComparing(ChatRoom::getRecentMessageTime).reversed())
                .toList();
    }

    @Transactional
    public List<Chat> getChats(Party party) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        if (!party.isParticipated(authenticatedUser)) {
            throw new NotParticipatedPartyException();
        }

        List<Chat> chats = chatFinder.findChats(party);
        if (chats.isEmpty()) {
            return Collections.emptyList();
        }
        chatReadRecorder.recordReadHistory(chats.get(chats.size() - 1), authenticatedUser);
        return chats;
    }

    @Transactional
    public void receiveMessage(Long partyId, Long chatId, User receiver) {
        chatReadRepository.updateLastChatId(receiver.getId(), partyId, chatId);
    }
}
