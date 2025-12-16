package org.graphrag;

import org.graphrag.model.GraphDocument;
import org.graphrag.model.Relationship;
import org.graphrag.model.Entity;

/**
 * Abstraction over the graph storage layer. The Python code supports multiple
 * backends (e.g., in-memory, SQLite, or external stores). This placeholder
 * allows incremental development of equivalent backends in Java.
 */
public interface GraphStore {
    /**
     * Ingests raw documents into the graph. The Python reference extracts
     * entities and builds relationships during ingestion; this method is the
     * Java counterpart where implementers can mirror that behavior.
     */
    void ingest(GraphRagConfig config, Iterable<GraphDocument> documents);

    /**
     * Extended ingestion hook used by the {@code index} pipeline to persist
     * precomputed graph artifacts (documents, entities, and relationships).
     * Implementers should accept the supplied entities and relationships as
     * authoritative when provided, falling back to on-the-fly extraction if
     * desired.
     */
    default void ingestArtifacts(
            GraphRagConfig config,
            Iterable<GraphDocument> documents,
            Iterable<Entity> entities,
            Iterable<Relationship> relationships) {
        ingest(config, documents);
    }

    /**
     * Prepare the store for querying, returning a context object that can carry
     * metadata, cached embeddings, or other runtime state.
     */
    GraphContext ensureReady();
}
