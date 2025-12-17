package org.graphrag.index.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.graphrag.index.typing.DocumentChunk;
import org.graphrag.index.typing.GraphArtifacts;
import org.graphrag.index.typing.TextUnit;
import org.graphrag.model.Entity;
import org.graphrag.model.GraphDocument;
import org.graphrag.model.Relationship;

/**
 * Mirrors the Python {@code index.operations.create_graph_artifacts} step.
 * Consumes text units and constructs the entity and relationship sets that will
 * be persisted alongside documents.
 */
public class CreateGraphArtifactsOperation {

    public GraphArtifacts apply(List<GraphDocument> documents, List<DocumentChunk> chunks, List<TextUnit> textUnits) {
        Objects.requireNonNull(documents, "documents");
        Objects.requireNonNull(chunks, "chunks");
        Objects.requireNonNull(textUnits, "textUnits");

        List<Entity> entities = new ArrayList<>();
        List<Relationship> relationships = new ArrayList<>();

        for (TextUnit textUnit : textUnits) {
            List<Entity> extracted = extractEntities(textUnit);
            entities.addAll(extracted);
            relationships.addAll(connectSequential(extracted, textUnit));
        }

        return new GraphArtifacts(documents, chunks, textUnits, entities, relationships);
    }

    private List<Entity> extractEntities(TextUnit textUnit) {
        Map<String, Entity> seen = new HashMap<>();
        for (String token : textUnit.text().split("\\s+")) {
            String normalized = normalize(token);
            if (normalized.isBlank()) {
                continue;
            }
            seen.computeIfAbsent(normalized, label -> new Entity(label, "Extracted from text unit " + textUnit.id()));
        }
        return new ArrayList<>(seen.values());
    }

    private List<Relationship> connectSequential(List<Entity> extracted, TextUnit textUnit) {
        List<Relationship> relationships = new ArrayList<>();
        for (int i = 0; i < extracted.size() - 1; i++) {
            Entity source = extracted.get(i);
            Entity target = extracted.get(i + 1);
            String type = "adjacent_in_unit:" + textUnit.id();
            relationships.add(new Relationship(source.id(), target.id(), type));
        }
        return relationships;
    }

    private String normalize(String token) {
        String cleaned = token.replaceAll("[^A-Za-z0-9]", "");
        return cleaned.toLowerCase(Locale.ROOT);
    }
}
