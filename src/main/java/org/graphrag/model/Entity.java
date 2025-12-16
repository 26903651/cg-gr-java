package org.graphrag.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Minimal representation of an entity node used by the GraphRAG retrieval flow.
 */
public final class Entity {
    private final String id;
    private final String label;
    private final String description;

    public Entity(String label, String description) {
        this(UUID.randomUUID().toString(), label, description);
    }

    public Entity(String id, String label, String description) {
        this.id = Objects.requireNonNull(id, "id");
        this.label = Objects.requireNonNull(label, "label");
        this.description = Objects.requireNonNull(description, "description");
    }

    public String id() {
        return id;
    }

    public String label() {
        return label;
    }

    public String description() {
        return description;
    }
}
