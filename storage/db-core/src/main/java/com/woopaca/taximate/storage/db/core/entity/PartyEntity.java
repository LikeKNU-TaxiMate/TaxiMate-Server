package com.woopaca.taximate.storage.db.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Entity(name = "party")
public class PartyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDateTime departureTime;

    private String origin;

    private String destination;

    @Column(columnDefinition = "POINT SRID 4326")
    private Point originLocation;

    private int maxPassengers;

    @OneToMany(mappedBy = "party")
    private Set<ParticipationEntity> participationSet = Set.of();

    public double getOriginLatitude() {
        return originLocation.getY();
    }

    public double getOriginLongitude() {
        return originLocation.getX();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PartyEntity that = (PartyEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
