package org.graphrag.index.input;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Mirrors the Python GraphRAG {@code index.input} document contract while
 * keeping Java-friendly immutability. Represents a single raw document prior to
 * chunking or entity extraction.
 */
public final class InputDocument {
    private final String id;
    private final String title;
    private final String content;
    private final Map<String, String> metadata;

    public InputDocument(String title, String content) {
        this(UUID.randomUUID().toString(), title, content, Collections.emptyMap());
    }

    public InputDocument(String id, String title, String content, Map<String, String> metadata) {
        this.id = Objects.requireNonNull(id, "id");
        this.title = Objects.requireNonNull(title, "title");
        this.content = Objects.requireNonNull(content, "content");
        this.metadata = Map.copyOf(metadata == null ? Collections.emptyMap() : metadata);
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

    public Map<String, String> metadata() {
        return metadata;
    }
}
