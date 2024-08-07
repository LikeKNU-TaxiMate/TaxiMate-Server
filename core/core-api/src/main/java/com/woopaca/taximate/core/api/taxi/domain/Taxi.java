package com.woopaca.taximate.core.api.taxi.domain;

import com.woopaca.taximate.core.api.party.model.Coordinate;

import java.util.List;

public record Taxi(List<Coordinate> route, int fare, int duration) {
}
