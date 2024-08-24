package com.woopaca.taximate.core.api.taxi.domain;

import com.woopaca.taximate.core.api.party.model.Coordinate;
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
}
