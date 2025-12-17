package org.graphrag;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.graphrag.model.GraphDocument;
import org.graphrag.model.GraphEdge;
import org.graphrag.model.GraphNode;

/**
 * Represents runtime context for a graph query. The Python implementation
 * surfaces a similar object that bundles together the active graph, embedding
 * state, and any derived caches for answer synthesis.
 */
public class GraphContext {
    private final List<GraphNode> nodes;
    private final List<GraphEdge> edges;
    private final List<GraphDocument> documents;
    private final Map<String, Double> nodeRelevancy;
    private final Instant preparedAt;

    public GraphContext(
            List<GraphNode> nodes,
            List<GraphEdge> edges,
            List<GraphDocument> documents,
            Map<String, Double> nodeRelevancy,
            Instant preparedAt) {
        this.nodes = List.copyOf(nodes);
        this.edges = List.copyOf(edges);
        this.documents = List.copyOf(documents);
        this.nodeRelevancy = Map.copyOf(nodeRelevancy);
        this.preparedAt = Objects.requireNonNull(preparedAt, "preparedAt");
    }

    public List<GraphNode> nodes() {
        return nodes;
    }

    public List<GraphEdge> edges() {
        return edges;
    }

    public List<GraphDocument> documents() {
        return documents;
    }

    public Map<String, Double> nodeRelevancy() {
        return nodeRelevancy;
    }

    public Instant preparedAt() {
        return preparedAt;
    }
}
