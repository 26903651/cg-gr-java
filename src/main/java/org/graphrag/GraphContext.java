package org.graphrag;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.graphrag.index.typing.DocumentChunk;
import org.graphrag.index.typing.TextUnit;
import org.graphrag.model.GraphDocument;
import org.graphrag.model.Relationship;
import org.graphrag.model.Entity;

/**
 * Represents runtime context for a graph query. The Python implementation
 * surfaces a similar object that bundles together the active graph, embedding
 * state, and any derived caches for answer synthesis.
 */
public class GraphContext {
    private final List<Entity> entities;
    private final List<Relationship> relationships;
    private final List<GraphDocument> documents;
    private final List<DocumentChunk> chunks;
    private final List<TextUnit> textUnits;
    private final Map<String, Double> entityRelevancy;
    private final Instant preparedAt;

    public GraphContext(
            List<Entity> entities,
            List<Relationship> relationships,
            List<GraphDocument> documents,
            List<DocumentChunk> chunks,
            List<TextUnit> textUnits,
            Map<String, Double> entityRelevancy,
            Instant preparedAt) {
        this.entities = List.copyOf(entities);
        this.relationships = List.copyOf(relationships);
        this.documents = List.copyOf(documents);
        this.chunks = List.copyOf(chunks);
        this.textUnits = List.copyOf(textUnits);
        this.entityRelevancy = Map.copyOf(entityRelevancy);
        this.preparedAt = Objects.requireNonNull(preparedAt, "preparedAt");
    }

    public List<Entity> entities() {
        return entities;
    }

    public List<Relationship> relationships() {
        return relationships;
    }

    public List<GraphDocument> documents() {
        return documents;
    }

    public List<DocumentChunk> chunks() {
        return chunks;
    }

    public List<TextUnit> textUnits() {
        return textUnits;
    }

    public Map<String, Double> entityRelevancy() {
        return entityRelevancy;
    }

    public Instant preparedAt() {
        return preparedAt;
    }
}
