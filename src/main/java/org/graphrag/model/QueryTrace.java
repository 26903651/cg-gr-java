package org.graphrag.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Captures a minimal trace of how a query was answered, mirroring the
 * diagnostics available in the Python implementation.
 */
public final class QueryTrace {
    private final String question;
    private final List<String> matchedDocumentIds;
    private final List<String> traversedNodeIds;
    private final Instant executedAt;

    public QueryTrace(String question, List<String> matchedDocumentIds, List<String> traversedNodeIds, Instant executedAt) {
        this.question = Objects.requireNonNull(question, "question");
        this.matchedDocumentIds = List.copyOf(matchedDocumentIds);
        this.traversedNodeIds = List.copyOf(traversedNodeIds);
        this.executedAt = Objects.requireNonNull(executedAt, "executedAt");
    }

    public String question() {
        return question;
    }

    public List<String> matchedDocumentIds() {
        return matchedDocumentIds;
    }

    public List<String> traversedNodeIds() {
        return traversedNodeIds;
    }

    public Instant executedAt() {
        return executedAt;
    }
}
