package com.woopaca.taximate.core.api.taxi.api;

import com.woopaca.taximate.core.domain.party.model.Coordinate;

import java.util.List;

public record KakaoDirections(List<Route> routes) {

    public List<Coordinate> route() {
        return routes.get(0)
                .sections.get(0)
                .guides.stream()
                .map(guide -> Coordinate.of(guide.y, guide.x))
                .toList();
    }

    public int fare() {
        return routes.get(0)
                .summary
                .fare
                .taxi;
    }

    public int duration() {
        return routes.get(0)
                .summary
                .duration;
    }

    record Route(Summary summary, List<Section> sections) {
    }

    record Summary(Fare fare, int duration) {
    }

    record Fare(int taxi) {
    }

    record Section(List<Guide> guides) {
    }

    record Guide(double x, double y) {
    }
}
