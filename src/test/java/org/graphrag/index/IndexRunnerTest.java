package org.graphrag.index;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.graphrag.GraphRag;
import org.graphrag.GraphRagConfig;
import org.graphrag.GraphStore;
import org.graphrag.QueryEngine;
import org.graphrag.QueryResult;
import org.graphrag.index.input.InputDocument;
import org.graphrag.query.SimpleQueryEngine;
import org.graphrag.store.InMemoryGraphStore;
import org.junit.jupiter.api.Test;

class IndexRunnerTest {

    @Test
    void indexPipelineCreatesGraphArtifactsAndSupportsQueries() {
        GraphRagConfig config = new GraphRagConfig(Path.of("."), List.of("**/*"), List.of());
        GraphStore store = new InMemoryGraphStore();
        QueryEngine queryEngine = new SimpleQueryEngine();
        GraphRag graphRag = new GraphRag(config, store, queryEngine);

        InputDocument document = new InputDocument("Story", "Alice meets Bob. Bob travels to Paris.");
        graphRag.index(List.of(document));

        var context = store.ensureReady();
        assertEquals(1, context.documents().size(), "Index should persist source documents");
        assertEquals(1, context.chunks().size(), "Chunks should mirror the Python text splitter output");
        assertEquals(1, context.textUnits().size(), "Text units should be created from ordered chunks");
        assertFalse(context.entities().isEmpty(), "Entities should be extracted from text units");
        assertFalse(context.relationships().isEmpty(), "Relationships should be constructed between related entities");

        QueryResult result = graphRag.query("Paris");
        assertTrue(result.answer().contains("Story"), "Query should surface the original source title");
    }
}
