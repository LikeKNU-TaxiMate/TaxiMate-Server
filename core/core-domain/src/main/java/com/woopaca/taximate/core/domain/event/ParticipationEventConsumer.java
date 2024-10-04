package com.woopaca.taximate.core.domain.event;

import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.MessageNotifier;
import com.woopaca.taximate.core.domain.event.dto.LeaveEvent;
import com.woopaca.taximate.core.domain.event.dto.ParticipateEvent;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class ParticipationEventConsumer {

    private static final String PARTICIPATE_MESSAGE = "%s님이 팟에 참여했습니다.";
    private static final String LEAVE_MESSAGE = "%s님이 팟을 나갔습니다.";

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
        String message = String.format(PARTICIPATE_MESSAGE, participant.getNickname());
        Party party = participateEvent.party();
        Chat systemMessage = Chat.systemMessage(party, message, participateEvent.participatedAt());
        chatEventProducer.publishChatEvent(systemMessage);
        messageNotifier.notify(systemMessage);
    }

    @Async
    @TransactionalEventListener
    public void handleLeaveEvent(LeaveEvent leaveEvent) {
        User leaver = leaveEvent.leaver();
        String message = String.format(LEAVE_MESSAGE, leaver.getNickname());
        Party party = leaveEvent.party();
        Chat systemMessage = Chat.systemMessage(party, message, leaveEvent.leftAt());
        chatEventProducer.publishChatEvent(systemMessage);
        messageNotifier.notify(systemMessage);
    }
}
