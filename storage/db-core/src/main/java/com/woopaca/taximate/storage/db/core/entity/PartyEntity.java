package com.woopaca.taximate.storage.db.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Entity(name = "party")
public class PartyEntity extends BaseEntity {

    private String title;

    private String explanation;

    private LocalDateTime departureTime;

    private String origin;

    private String destination;

    private String originAddress;

    private String destinationAddress;

    private Point originLocation;

    private Point destinationLocation;

    private int maxParticipants;

    private int views;

    @OneToMany(mappedBy = "party")
    private Set<ParticipationEntity> participationSet = Collections.emptySet();

    public double getOriginLatitude() {
        return originLocation.getY();
    }

    public double getOriginLongitude() {
        return originLocation.getX();
    }

    public double getDestinationLatitude() {
        return destinationLocation.getY();
    }

    public double getDestinationLongitude() {
        return destinationLocation.getX();
    }
}
