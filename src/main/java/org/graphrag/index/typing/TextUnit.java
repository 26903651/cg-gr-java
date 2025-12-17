package org.graphrag.index.typing;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Mirrors the Python {@code index.typing.TextUnit} structure that captures a
 * semantically meaningful slice of a document. Text units are the atomic
 * inputs for entity extraction and relationship creation during indexing.
 */
public final class TextUnit {
    private final String id;
    private final String documentId;
    private final String chunkId;
    private final int position;
    private final String text;
    private final Map<String, String> metadata;

    public TextUnit(String id, String documentId, String chunkId, int position, String text, Map<String, String> metadata) {
        this.id = Objects.requireNonNull(id, "id");
        this.documentId = Objects.requireNonNull(documentId, "documentId");
        this.chunkId = Objects.requireNonNull(chunkId, "chunkId");
        this.position = position;
        this.text = Objects.requireNonNull(text, "text");
        this.metadata = Map.copyOf(metadata == null ? Collections.emptyMap() : metadata);
    }

    public String id() {
        return id;
    }

    public String documentId() {
        return documentId;
    }

    public String chunkId() {
        return chunkId;
    }

    public int position() {
        return position;
    }

    public String text() {
        return text;
    }

    public Map<String, String> metadata() {
        return metadata;
    }
}
