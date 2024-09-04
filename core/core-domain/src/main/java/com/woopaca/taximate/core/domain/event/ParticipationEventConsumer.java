package com.woopaca.taximate.core.domain.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class ParticipationEventConsumer {

    @Async
    @TransactionalEventListener
    public void handleParticipateEvent(ParticipateEvent participateEvent) {
        // TODO 팟 참여 이벤트 처리
    }
}
