package org.graphrag.index.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.graphrag.index.typing.DocumentChunk;
import org.graphrag.index.typing.GraphArtifacts;
import org.graphrag.model.GraphDocument;
import org.graphrag.model.Relationship;
import org.graphrag.model.Entity;

/**
 * Builds graph artifacts from document chunks using trivial entity extraction and linkage.
 */
public class BuildGraphArtifactsOperation {

    public GraphArtifacts build(List<DocumentChunk> chunks) {
        List<GraphDocument> documents = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();
        List<Relationship> relationships = new ArrayList<>();

        for (DocumentChunk chunk : chunks) {
            GraphDocument document = new GraphDocument(chunk.id(), chunk.text());
            documents.add(document);

            List<Entity> extracted = extractEntities(chunk);
            entities.addAll(extracted);
            relationships.addAll(connectSequential(extracted));
        }

        return new GraphArtifacts(documents, entities, relationships);
    }

    private List<Entity> extractEntities(DocumentChunk chunk) {
        String[] tokens = chunk.text().split("\\s+");
        Map<String, Entity> seen = new HashMap<>();
        for (String token : tokens) {
            String normalized = token.trim().toLowerCase();
            if (normalized.isBlank()) {
                continue;
            }
            if (normalized.length() <= 3) {
                continue; // ignore short words
            }
            seen.computeIfAbsent(normalized, label -> new Entity(label, "Extracted from chunk " + chunk.id()));
        }
        return new ArrayList<>(seen.values());
    }

    private List<Relationship> connectSequential(List<Entity> extracted) {
        List<Relationship> relationships = new ArrayList<>();
        for (int i = 0; i < extracted.size() - 1; i++) {
            Entity source = extracted.get(i);
            Entity target = extracted.get(i + 1);
            relationships.add(new Relationship(source.id(), target.id(), "adjacent_in_chunk"));
        }
        return relationships;
    }
}
