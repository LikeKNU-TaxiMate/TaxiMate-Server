package com.woopaca.taximate.core.domain.chat;

import com.woopaca.taximate.core.domain.error.exception.ChatMessageTooLongException;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.ChatEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Chat {

    public static final int MAX_MESSAGE_LENGTH = 500;

    @EqualsAndHashCode.Include
    private Long id;
    private String message;
    private MessageType type;
    private LocalDateTime sentAt;
    private User sender;
    private Party party;

    @Builder
    public Chat(Long id, String message, MessageType type, LocalDateTime sentAt, User sender, Party party) {
        if (message.length() > MAX_MESSAGE_LENGTH) {
            throw new ChatMessageTooLongException();
        }

        this.id = id;
        this.message = message.trim();
        this.type = type;
        this.sentAt = sentAt;
        this.sender = sender;
        this.party = party;
    }

    public static Chat standardMessage(Party party, User sender, String message) {
        return Chat.builder()
                .message(message)
                .type(MessageType.MESSAGE)
                .sentAt(LocalDateTime.now())
                .sender(sender)
                .party(party)
                .build();
    }

    public static Chat systemMessage(Party party, String message, LocalDateTime sentAt) {
        return Chat.builder()
                .message(message)
                .type(MessageType.SYSTEM)
                .sentAt(sentAt)
                .party(party)
                .build();
    }

    public ChatEntity toEntity() {
        return ChatEntity.builder()
                .message(message)
                .userId(sender.getId())
                .partyId(party.getId())
                .build();
    }
}
