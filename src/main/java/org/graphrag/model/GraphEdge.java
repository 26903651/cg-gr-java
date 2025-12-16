package org.graphrag.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Directed edge capturing relationships between entity nodes.
 */
public final class GraphEdge {
    private final String id;
    private final String sourceId;
    private final String targetId;
    private final String relation;

    public GraphEdge(String sourceId, String targetId, String relation) {
        this(UUID.randomUUID().toString(), sourceId, targetId, relation);
    }

    public GraphEdge(String id, String sourceId, String targetId, String relation) {
        this.id = Objects.requireNonNull(id, "id");
        this.sourceId = Objects.requireNonNull(sourceId, "sourceId");
        this.targetId = Objects.requireNonNull(targetId, "targetId");
        this.relation = Objects.requireNonNull(relation, "relation");
    }

    public String id() {
        return id;
    }

    public String sourceId() {
        return sourceId;
    }

    public String targetId() {
        return targetId;
    }

    public String relation() {
        return relation;
    }
}
