package org.graphrag.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a source document ingested into the graph. The Python GraphRAG
 * implementation treats documents as the source of both text snippets and
 * derived entities; this mirrors that contract in a Java-friendly shape.
 */
public final class GraphDocument {
    private final String id;
    private final String title;
    private final String content;
    private final Instant ingestedAt;

    public GraphDocument(String title, String content) {
        this(UUID.randomUUID().toString(), title, content, Instant.now());
    }

    public GraphDocument(String id, String title, String content, Instant ingestedAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.title = Objects.requireNonNull(title, "title");
        this.content = Objects.requireNonNull(content, "content");
        this.ingestedAt = Objects.requireNonNull(ingestedAt, "ingestedAt");
    }

    public String id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String content() {
        return content;
    }

    public Instant ingestedAt() {
        return ingestedAt;
    }
}
