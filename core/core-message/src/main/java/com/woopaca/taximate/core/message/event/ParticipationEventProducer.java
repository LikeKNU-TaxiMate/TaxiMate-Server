package com.woopaca.taximate.core.message.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ParticipationEventProducer {

    private final ApplicationEventPublisher eventPublisher;

    public ParticipationEventProducer(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishParticipateEvent(Long partyId, Long userId) {
        // TODO 팟 참여 이벤트 발행
        ParticipateEvent participateEvent = new ParticipateEvent(partyId, userId);
        eventPublisher.publishEvent(participateEvent);
    }
}
