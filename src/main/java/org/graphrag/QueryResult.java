package org.graphrag;

import java.time.Instant;
import java.util.Objects;

import org.graphrag.model.QueryTrace;

/**
 * Structured result wrapper so the Java API resembles the outputs returned by
 * the Python GraphRAG implementation.
 */
public class QueryResult {
    private final String answer;
    private final QueryTrace trace;
    private final Instant timestamp;

    public QueryResult(String answer, QueryTrace trace, Instant timestamp) {
        this.answer = Objects.requireNonNull(answer, "answer");
        this.trace = Objects.requireNonNull(trace, "trace");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp");
    }

    public String answer() {
        return answer;
    }

    public QueryTrace trace() {
        return trace;
    }

    public Instant timestamp() {
        return timestamp;
    }
}
