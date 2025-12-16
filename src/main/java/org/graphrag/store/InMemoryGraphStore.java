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
import org.graphrag.model.Relationship;
import org.graphrag.model.Entity;

/**
 * A lightweight, in-memory graph store roughly mirroring the behavior of the
 * Python {@code InMemoryGraphStore}. It performs trivial entity extraction by
 * treating capitalized tokens as entities and connects them with synthetic
 * relationships when they appear in the same document.
 */
public class InMemoryGraphStore implements GraphStore {
    private final List<GraphDocument> documents = new ArrayList<>();
    private final List<Entity> entities = new ArrayList<>();
    private final List<Relationship> relationships = new ArrayList<>();
    private GraphContext cachedContext;

    @Override
    public synchronized void ingest(GraphRagConfig config, Iterable<GraphDocument> incomingDocuments) {
        Objects.requireNonNull(config, "config");
        Objects.requireNonNull(incomingDocuments, "incomingDocuments");
        for (GraphDocument document : incomingDocuments) {
            documents.add(document);
            List<Entity> extracted = extractEntities(document);
            entities.addAll(extracted);
            relationships.addAll(connectWithinDocument(document, extracted));
        }
        cachedContext = null; // force regeneration
    }

    @Override
    public synchronized void ingestArtifacts(
            GraphRagConfig config,
            Iterable<GraphDocument> incomingDocuments,
            Iterable<Entity> incomingEntities,
            Iterable<Relationship> incomingRelationships) {
        Objects.requireNonNull(config, "config");
        Objects.requireNonNull(incomingDocuments, "incomingDocuments");

        boolean hasEntities = incomingEntities != null && incomingEntities.iterator().hasNext();
        boolean hasRelationships = incomingRelationships != null && incomingRelationships.iterator().hasNext();

        if (hasEntities) {
            incomingEntities.forEach(entities::add);
        }
        if (hasRelationships) {
            incomingRelationships.forEach(relationships::add);
        }

        if (hasEntities || hasRelationships) {
            incomingDocuments.forEach(documents::add);
            cachedContext = null;
        } else {
            ingest(config, incomingDocuments);
        }
    }

    @Override
    public synchronized GraphContext ensureReady() {
        if (cachedContext == null) {
            Map<String, Double> entityRelevancy = entities.stream()
                    .collect(Collectors.toMap(Entity::id, entity -> 1.0d));
            cachedContext = new GraphContext(entities, relationships, documents, entityRelevancy, Instant.now());
        }
        return cachedContext;
    }

    private List<Entity> extractEntities(GraphDocument document) {
        String[] tokens = document.content().split("\\s+");
        Map<String, Entity> seen = new HashMap<>();
        for (String token : tokens) {
            if (token.length() > 1 && Character.isUpperCase(token.charAt(0))) {
                String normalized = normalize(token);
                seen.computeIfAbsent(normalized, label -> new Entity(label, "Extracted from document " + document.id()));
            }
        }
        return new ArrayList<>(seen.values());
    }

    private List<Relationship> connectWithinDocument(GraphDocument document, List<Entity> extracted) {
        List<Relationship> documentRelationships = new ArrayList<>();
        for (int i = 0; i < extracted.size(); i++) {
            for (int j = i + 1; j < extracted.size(); j++) {
                Entity source = extracted.get(i);
                Entity target = extracted.get(j);
                documentRelationships.add(new Relationship(source.id(), target.id(), "co_occurs_in:" + document.id()));
            }
        }
        return documentRelationships;
    }

    private String normalize(String token) {
        return token.replaceAll("[^A-Za-z0-9]", "").toLowerCase(Locale.ROOT);
    }
}
