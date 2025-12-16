package org.graphrag.index.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Lightweight ID generator used across index operations to mirror the
 * auto-incrementing identifiers in the Python implementation.
 */
public final class IdGenerator {
    private final AtomicLong counter = new AtomicLong();
    private final String prefix;

    public IdGenerator(String prefix) {
        this.prefix = prefix == null ? "id" : prefix;
    }

    public String nextId() {
        return prefix + counter.incrementAndGet();
    }
}
