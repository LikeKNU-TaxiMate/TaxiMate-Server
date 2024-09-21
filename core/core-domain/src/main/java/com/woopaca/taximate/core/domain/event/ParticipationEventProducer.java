package com.woopaca.taximate.core.domain.event;

import com.woopaca.taximate.core.domain.user.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ParticipationEventProducer {

    private final ApplicationEventPublisher eventPublisher;

    public ParticipationEventProducer(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishParticipateEvent(Long partyId, User user, LocalDateTime participatedAt) {
        ParticipateEvent event = new ParticipateEvent(partyId, user, participatedAt);
        eventPublisher.publishEvent(event);
    }

    public void publishLeaveEvent(Long partyId, User user, LocalDateTime leftAt) {
        LeaveEvent event = new LeaveEvent(partyId, user.getId(), leftAt);
        eventPublisher.publishEvent(event);
    }
}
