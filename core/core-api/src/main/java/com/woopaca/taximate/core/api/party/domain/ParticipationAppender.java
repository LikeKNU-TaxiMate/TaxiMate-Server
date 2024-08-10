package com.woopaca.taximate.core.api.party.domain;

import com.woopaca.taximate.core.api.user.domain.User;
import com.woopaca.taximate.storage.db.core.repository.ParticipationRepository;
import org.springframework.stereotype.Component;

@Component
public class ParticipationAppender {

    private final ParticipationRepository participationRepository;

    public ParticipationAppender(ParticipationRepository participationRepository) {
        this.participationRepository = participationRepository;
    }

    public void appendHost(Party party, User user) {
        // TODO 호스트 추가
    }
}
