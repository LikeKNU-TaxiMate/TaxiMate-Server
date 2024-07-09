package com.woopaca.taximate.core.api.party.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Parties {

    private final List<Party> parties;

    public Parties(List<Party> parties) {
        if (parties == null) {
            this.parties = Collections.emptyList();
            return;
        }
        this.parties = parties;
    }

    public Stream<Party> stream() {
        return this.parties.stream();
    }
}
