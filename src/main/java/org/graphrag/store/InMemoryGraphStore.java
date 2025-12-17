package org.graphrag.store;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.graphrag.GraphContext;
import org.graphrag.GraphRagConfig;
import org.graphrag.GraphStore;
import org.graphrag.model.GraphDocument;
import org.graphrag.model.GraphEdge;
import org.graphrag.model.GraphNode;

/**
 * A lightweight, in-memory graph store roughly mirroring the behavior of the
 * Python {@code InMemoryGraphStore}. It performs trivial entity extraction by
 * treating capitalized tokens as entities and connects them with synthetic
 * edges when they appear in the same document.
 */
public class InMemoryGraphStore implements GraphStore {
    private final List<GraphDocument> documents = new ArrayList<>();
    private final List<GraphNode> nodes = new ArrayList<>();
    private final List<GraphEdge> edges = new ArrayList<>();
    private GraphContext cachedContext;

    @Override
    public synchronized void ingest(GraphRagConfig config, Iterable<GraphDocument> incomingDocuments) {
        Objects.requireNonNull(config, "config");
        Objects.requireNonNull(incomingDocuments, "incomingDocuments");
        for (GraphDocument document : incomingDocuments) {
            documents.add(document);
            List<GraphNode> extracted = extractEntities(document);
            nodes.addAll(extracted);
            edges.addAll(connectWithinDocument(document, extracted));
        }
        cachedContext = null; // force regeneration
    }

    @Override
    public synchronized void ingestArtifacts(
            GraphRagConfig config,
            Iterable<GraphDocument> incomingDocuments,
            Iterable<GraphNode> incomingNodes,
            Iterable<GraphEdge> incomingEdges) {
        Objects.requireNonNull(config, "config");
        Objects.requireNonNull(incomingDocuments, "incomingDocuments");

        boolean hasNodes = incomingNodes != null && incomingNodes.iterator().hasNext();
        boolean hasEdges = incomingEdges != null && incomingEdges.iterator().hasNext();

        if (hasNodes) {
            incomingNodes.forEach(nodes::add);
        }
        if (hasEdges) {
            incomingEdges.forEach(edges::add);
        }

        if (hasNodes || hasEdges) {
            incomingDocuments.forEach(documents::add);
            cachedContext = null;
        } else {
            ingest(config, incomingDocuments);
        }
    }

    @Override
    public synchronized GraphContext ensureReady() {
        if (cachedContext == null) {
            Map<String, Double> nodeRelevancy = nodes.stream()
                    .collect(Collectors.toMap(GraphNode::id, node -> 1.0d));
            cachedContext = new GraphContext(nodes, edges, documents, nodeRelevancy, Instant.now());
        }
        return cachedContext;
    }

    private List<GraphNode> extractEntities(GraphDocument document) {
        String[] tokens = document.content().split("\\s+");
        Map<String, GraphNode> seen = new HashMap<>();
        for (String token : tokens) {
            if (token.length() > 1 && Character.isUpperCase(token.charAt(0))) {
                String normalized = normalize(token);
                seen.computeIfAbsent(normalized, label -> new GraphNode(label, "Extracted from document " + document.id()));
            }
        }
        return new ArrayList<>(seen.values());
    }

    private List<GraphEdge> connectWithinDocument(GraphDocument document, List<GraphNode> extracted) {
        List<GraphEdge> documentEdges = new ArrayList<>();
        for (int i = 0; i < extracted.size(); i++) {
            for (int j = i + 1; j < extracted.size(); j++) {
                GraphNode source = extracted.get(i);
                GraphNode target = extracted.get(j);
                documentEdges.add(new GraphEdge(source.id(), target.id(), "co_occurs_in:" + document.id()));
            }
        }
        return documentEdges;
    }

    private String normalize(String token) {
        return token.replaceAll("[^A-Za-z0-9]", "").toLowerCase(Locale.ROOT);
    }
}
