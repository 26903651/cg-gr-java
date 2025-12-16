package org.graphrag.query;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.graphrag.GraphContext;
import org.graphrag.QueryEngine;
import org.graphrag.QueryResult;
import org.graphrag.model.GraphDocument;
import org.graphrag.model.Entity;
import org.graphrag.model.QueryTrace;

/**
 * A minimal query implementation that mirrors the high-level steps of the
 * Python pipeline: (1) retrieve relevant documents, (2) traverse related
 * entities, and (3) synthesize a response from the matches. This is intentionally
 * lightweight and deterministic so the Java port has runnable behavior without
 * requiring LLM dependencies.
 */
public class SimpleQueryEngine implements QueryEngine {
    private final int maxDocuments;

    public SimpleQueryEngine() {
        this(5);
    }

    public SimpleQueryEngine(int maxDocuments) {
        this.maxDocuments = maxDocuments;
    }

    @Override
    public QueryResult answer(String question, GraphContext context) {
        String normalized = question.toLowerCase(Locale.ROOT);
        List<GraphDocument> matchingDocuments = context.documents().stream()
                .filter(doc -> doc.content().toLowerCase(Locale.ROOT).contains(normalized))
                .sorted(Comparator.comparing(GraphDocument::ingestedAt).reversed())
                .limit(maxDocuments)
                .toList();

        Map<String, Double> relevancy = context.entityRelevancy();
        List<Entity> traversedEntities = context.entities().stream()
                .sorted((a, b) -> Double.compare(relevancy.getOrDefault(b.id(), 0d), relevancy.getOrDefault(a.id(), 0d)))
                .limit(maxDocuments)
                .toList();

        List<String> snippets = new ArrayList<>();
        for (GraphDocument doc : matchingDocuments) {
            snippets.add(doc.title() + ": " + summarize(doc.content()));
        }

        String synthesized = snippets.isEmpty()
                ? "No supporting documents matched the query."
                : String.join("\n", snippets);

        QueryTrace trace = new QueryTrace(
                question,
                matchingDocuments.stream().map(GraphDocument::id).collect(Collectors.toList()),
                traversedEntities.stream().map(Entity::id).collect(Collectors.toList()),
                Instant.now());

        return new QueryResult(synthesized, trace, Instant.now());
    }

    private String summarize(String content) {
        String[] tokens = content.split("\\s+");
        int limit = Math.min(tokens.length, 40);
        return String.join(" ", java.util.Arrays.copyOf(tokens, limit)) + (tokens.length > limit ? " …" : "");
    }
}
