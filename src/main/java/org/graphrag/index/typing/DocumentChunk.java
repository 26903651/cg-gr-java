package org.graphrag.index.typing;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Equivalent to the Python {@code index.typing.DocumentChunk}. Represents a
 * slice of an {@link org.graphrag.index.input.InputDocument} after text
 * splitting.
 */
public final class DocumentChunk {
    private final String id;
    private final String documentId;
    private final int index;
    private final String text;
    private final Map<String, String> metadata;

    public DocumentChunk(String id, String documentId, int index, String text, Map<String, String> metadata) {
        this.id = Objects.requireNonNull(id, "id");
        this.documentId = Objects.requireNonNull(documentId, "documentId");
        this.index = index;
        this.text = Objects.requireNonNull(text, "text");
        this.metadata = Map.copyOf(metadata == null ? Collections.emptyMap() : metadata);
    }

    public String id() {
        return id;
    }

    public String documentId() {
        return documentId;
    }

    public int index() {
        return index;
    }

    public String text() {
        return text;
    }

    public Map<String, String> metadata() {
        return metadata;
    }
}
