package com.woopaca.taximate.core.domain.chat;

import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ChatDataBase {

    private final Map<Party, List<Chat>> map;
    private final Map<UserPartyKey, Long> readHistory;
    private final AtomicLong sequence;

    public ChatDataBase() {
        this.map = new ConcurrentHashMap<>();
        this.readHistory = new ConcurrentHashMap<>();
        this.sequence = new AtomicLong(0L);
    }

    public void saveChat(Chat chat) {
        Party party = chat.getParty();
        List<Chat> chats = map.computeIfAbsent(party, key -> Collections.synchronizedList(new ArrayList<>()));
        chat.updateId(sequence.incrementAndGet());
        chats.add(chat);
    }

    public List<Chat> readChat(User user, Party party) {
        List<Chat> chats = map.get(party);
        UserPartyKey userPartyKey = new UserPartyKey(user.getId(), party.getId());
        if (chats != null && !chats.isEmpty()) {
            readHistory.put(userPartyKey, chats.get(chats.size() - 1).getId());
        }
        return chats;
    }

    public Optional<Chat> readRecentChat(Party party) {
        List<Chat> chats = map.get(party);
        if (chats == null || chats.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(chats.get(chats.size() - 1));
    }

    public int countOfUnread(User user, Party party) {
        UserPartyKey userPartyKey = new UserPartyKey(user.getId(), party.getId());
        Long lastReadChat = readHistory.getOrDefault(userPartyKey, 0L);
        return (int) readChat(user, party).stream()
                .filter(chat -> chat.getId() > lastReadChat)
                .count();
    }

    record UserPartyKey(Long userId, Long partyId) {
    }
}