package org.graphrag.index.typing;

import java.util.List;
import java.util.Objects;

import org.graphrag.model.GraphDocument;
import org.graphrag.model.GraphEdge;
import org.graphrag.model.GraphNode;

/**
 * Equivalent to the Python {@code index.typing.GraphArtifacts}. Bundles the
 * persisted components of the indexing pipeline (documents, nodes, and edges)
 * so downstream storage layers can ingest them consistently.
 */
public record GraphArtifacts(List<GraphDocument> documents, List<GraphNode> nodes, List<GraphEdge> edges) {
    public GraphArtifacts {
        Objects.requireNonNull(documents, "documents");
        Objects.requireNonNull(nodes, "nodes");
        Objects.requireNonNull(edges, "edges");
    }
}
