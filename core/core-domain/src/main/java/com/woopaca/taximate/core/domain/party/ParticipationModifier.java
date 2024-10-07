package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.core.domain.error.exception.NonexistentPartyException;
import com.woopaca.taximate.core.domain.error.exception.NonexistentUserException;
import com.woopaca.taximate.core.domain.error.exception.NotParticipatedPartyException;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;
import com.woopaca.taximate.storage.db.core.repository.ParticipationRepository;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import com.woopaca.taximate.storage.db.core.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Objects;

import static com.woopaca.taximate.core.domain.party.Participation.ParticipationRole;

@Component
public class ParticipationModifier {

    private final ParticipationRepository participationRepository;
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;

    public ParticipationModifier(ParticipationRepository participationRepository, PartyRepository partyRepository, UserRepository userRepository) {
        this.participationRepository = participationRepository;
        this.partyRepository = partyRepository;
        this.userRepository = userRepository;
    }

    public Participation appendHost(Party party, User user) {
        UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(NonexistentUserException::new);
        PartyEntity partyEntity = partyRepository.findById(party.getId())
                .orElseThrow(() -> new NonexistentPartyException(party.getId()));
        ParticipationEntity participationEntity = ParticipationEntity.host(partyEntity, userEntity);
        ParticipationEntity savedParticipationEntity = participationRepository.save(participationEntity);
        return Participation.fromEntity(savedParticipationEntity);
    }

    public void appendParticipant(Party party, User user) {
        participationRepository.findByPartyIdAndUserId(party.getId(), user.getId())
                .ifPresentOrElse(entity -> {
                    entity.participate();
                    participationRepository.save(entity);
                }, () -> {
                    UserEntity userEntity = userRepository.findById(user.getId())
                            .orElseThrow(NonexistentUserException::new);
                    PartyEntity partyEntity = partyRepository.findById(party.getId())
                            .orElseThrow(() -> new NonexistentPartyException(party.getId()));
                    ParticipationEntity participationEntity = ParticipationEntity.participant(partyEntity, userEntity);
                    participationRepository.save(participationEntity);
                });
    }

    public void delegateHost(Party party, User host) {
        participationRepository.updateRole(host.getId(), party.getId(), ParticipationRole.PARTICIPANT.name());
        if (party.currentParticipantsCount() == 1) {
            return;
        }
        party.getParticipationSet()
                .stream()
                .filter(participation -> !Objects.equals(participation.getUser(), host))
                .max(Comparator.comparing(Participation::getParticipatedAt))
                .ifPresent(participation -> participationRepository
                        .updateRole(participation.getUser().getId(), party.getId(), ParticipationRole.HOST.name()));
    }

    public void removeParticipant(Party party, User user) {
        ParticipationEntity participationEntity = participationRepository.findByPartyIdAndUserId(party.getId(), user.getId())
                .orElseThrow(NotParticipatedPartyException::new);
        participationEntity.leave();
        participationRepository.save(participationEntity);
    }
}
