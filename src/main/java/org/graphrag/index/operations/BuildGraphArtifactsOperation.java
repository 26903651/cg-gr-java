package org.graphrag.index.operations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.graphrag.index.typing.DocumentChunk;
import org.graphrag.index.typing.GraphArtifacts;
import org.graphrag.model.GraphDocument;
import org.graphrag.model.GraphEdge;
import org.graphrag.model.GraphNode;

/**
 * Rough Java equivalent to the Python operations that derive nodes/edges from
 * pre-chunked text. This implementation keeps the heuristics simple while
 * aligning the API shape.
 */
public class BuildGraphArtifactsOperation {

    public GraphArtifacts apply(List<DocumentChunk> chunks) {
        Objects.requireNonNull(chunks, "chunks");
        List<GraphDocument> graphDocuments = new ArrayList<>();
        List<GraphNode> nodes = new ArrayList<>();
        List<GraphEdge> edges = new ArrayList<>();

        for (DocumentChunk chunk : chunks) {
            GraphDocument graphDocument = new GraphDocument(
                    chunk.id(),
                    "chunk:" + chunk.documentId() + ":" + chunk.index(),
                    chunk.text(),
                    Instant.now());
            graphDocuments.add(graphDocument);
            List<GraphNode> extracted = extractEntities(chunk);
            nodes.addAll(extracted);
            edges.addAll(connectSequential(extracted));
        }
        return new GraphArtifacts(graphDocuments, nodes, edges);
    }

    private List<GraphNode> extractEntities(DocumentChunk chunk) {
        String[] tokens = chunk.text().split("\\s+");
        Map<String, GraphNode> seen = new HashMap<>();
        for (String token : tokens) {
            if (token.length() > 1 && Character.isUpperCase(token.charAt(0))) {
                String normalized = normalize(token);
                seen.computeIfAbsent(normalized, label -> new GraphNode(label, "Extracted from chunk " + chunk.id()));
            }
        }
        return new ArrayList<>(seen.values());
    }

    private List<GraphEdge> connectSequential(List<GraphNode> extracted) {
        List<GraphEdge> edges = new ArrayList<>();
        for (int i = 0; i < extracted.size() - 1; i++) {
            GraphNode source = extracted.get(i);
            GraphNode target = extracted.get(i + 1);
            edges.add(new GraphEdge(source.id(), target.id(), "adjacent_in_chunk"));
        }
        return edges;
    }

    private String normalize(String token) {
        return token.replaceAll("[^A-Za-z0-9]", "").toLowerCase(Locale.ROOT);
    }

}
