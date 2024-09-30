package com.woopaca.taximate.core.domain.taxi;

import com.woopaca.taximate.core.domain.party.model.Coordinate;
import lombok.Getter;

import java.util.List;

@Getter
public class Taxi {

    private final List<Coordinate> route;
    private final int fare;
    private final int duration;

    public Taxi(List<Coordinate> route, int fare, int duration) {
        this.route = route;
        this.fare = fare;
        this.duration = duration;
    }

    public static Taxi fake() {
        return new Taxi(List.of(), 0, 0);
    }
}
