package com.woopaca.taximate.core.domain.chat.id;

import java.util.concurrent.atomic.AtomicLong;

public final class IdGenerator {

    private final AtomicLong sequence = new AtomicLong(0);

    private IdGenerator() {
    }

    private static class Nested {

        static final IdGenerator INSTANCE = new IdGenerator();
    }

    static void setInitialId(long initialId) {
        IdGenerator instance = Nested.INSTANCE;
        instance.sequence.set(initialId);
    }

    public static long generateId() {
        IdGenerator instance = Nested.INSTANCE;
        return instance.sequence.incrementAndGet();
    }
}
