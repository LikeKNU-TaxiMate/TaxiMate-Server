package com.woopaca.taximate.core.domain.chat;

import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.ChatReadEntity;
import com.woopaca.taximate.storage.db.core.repository.ChatReadRepository;
import org.springframework.stereotype.Component;

@Component
public class ChatReadRecorder {

    private final ChatReadRepository chatReadRepository;

    public ChatReadRecorder(ChatReadRepository chatReadRepository) {
        this.chatReadRepository = chatReadRepository;
    }

    public void createReadHistory(Chat participateChat) {
        User user = participateChat.getSender();
        Party party = participateChat.getParty();
        ChatReadEntity chatReadEntity = ChatReadEntity.builder()
                .userId(user.getId())
                .partyId(party.getId())
                .lastChatId(participateChat.getId())
                .build();
        chatReadRepository.save(chatReadEntity);
    }

    public void removeReadHistory(Chat leaveChat) {
        User user = leaveChat.getSender();
        Party party = leaveChat.getParty();
        chatReadRepository.findByUserIdAndPartyId(user.getId(), party.getId())
                .ifPresent(chatReadRepository::delete);
    }

    public void recordReadHistory(Chat chat) {
        recordReadHistory(chat, chat.getSender());
    }

    public void recordReadHistory(Chat chat, User user) {
        chatReadRepository.findByUserIdAndPartyId(user.getId(), chat.getParty().getId())
                .ifPresent(entity -> {
                    entity.updateLastChatId(chat.getId());
                    chatReadRepository.save(entity);
                });
    }
}
