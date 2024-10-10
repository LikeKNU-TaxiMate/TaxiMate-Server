package com.woopaca.taximate.core.domain.event;

import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.MessageNotifier;
import com.woopaca.taximate.core.domain.event.dto.LeaveEvent;
import com.woopaca.taximate.core.domain.event.dto.ParticipateEvent;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ParticipationEventConsumer {

    private final ChatEventProducer chatEventProducer;
    private final MessageNotifier messageNotifier;

    public ParticipationEventConsumer(ChatEventProducer chatEventProducer, MessageNotifier messageNotifier) {
        this.chatEventProducer = chatEventProducer;
        this.messageNotifier = messageNotifier;
    }

    @Async
    @TransactionalEventListener
    public void handleParticipateEvent(ParticipateEvent participateEvent) {
        User participant = participateEvent.participant();
        Party party = participateEvent.party();
        Chat participateMessage = Chat.participateMessage(party, participant, participateEvent.participatedAt());
        chatEventProducer.publishChatEvent(participateMessage);
        messageNotifier.notify(participateMessage);
    }

    @Async
    @TransactionalEventListener
    public void handleLeaveEvent(LeaveEvent leaveEvent) {
        User leaver = leaveEvent.leaver();
        Party party = leaveEvent.party();
        Chat leaveMessage = Chat.leaveMessage(party, leaver, leaveEvent.leftAt());
        chatEventProducer.publishChatEvent(leaveMessage);
        messageNotifier.notify(leaveMessage);
    }
}
