package org.graphrag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.graphrag.model.GraphDocument;
import org.graphrag.query.SimpleQueryEngine;
import org.graphrag.store.InMemoryGraphStore;
import org.junit.jupiter.api.Test;

class GraphRagIntegrationTest {

    @Test
    void ingestsAndQueriesDocuments() {
        GraphRagConfig config = new GraphRagConfig(Path.of("."), List.of("**/*.md"), List.of());
        InMemoryGraphStore store = new InMemoryGraphStore();
        SimpleQueryEngine engine = new SimpleQueryEngine();
        GraphRag graphRag = new GraphRag(config, store, engine);

        graphRag.ingest(List.of(new GraphDocument("Doc1", "Seattle and Redmond are cities in Washington.")));

        QueryResult result = graphRag.query("Seattle");

        assertNotNull(result);
        assertTrue(result.answer().contains("Doc1"));
        assertFalse(result.trace().matchedDocumentIds().isEmpty());
    }
}
