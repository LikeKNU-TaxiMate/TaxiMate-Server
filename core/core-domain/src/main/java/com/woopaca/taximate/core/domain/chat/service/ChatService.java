package com.woopaca.taximate.core.domain.chat.service;

import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.MessageNotifier;
import com.woopaca.taximate.core.domain.event.ChatEventProducer;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.PartyFinder;
import com.woopaca.taximate.core.domain.user.User;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final PartyFinder partyFinder;
    private final MessageNotifier messageNotifier;
    private final ChatEventProducer chatEventProducer;

    public ChatService(PartyFinder partyFinder, MessageNotifier messageNotifier, ChatEventProducer chatEventProducer) {
        this.partyFinder = partyFinder;
        this.messageNotifier = messageNotifier;
        this.chatEventProducer = chatEventProducer;
    }

    public void sendStandardMessage(Long partyId, User sender, String message) {
        Party party = partyFinder.findParty(partyId);
        Chat chat = Chat.standardMessage(party, sender, message);
        chatEventProducer.publishChatEvent(chat);
        messageNotifier.notify(chat);
    }
}
