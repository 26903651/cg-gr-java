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
        assertEquals(1, context.documents().size(), "Index should persist chunk documents");
        assertFalse(context.nodes().isEmpty(), "Entities should be extracted from chunks");
        assertFalse(context.edges().isEmpty(), "Edges should be constructed between related entities");

        QueryResult result = graphRag.query("Paris");
        assertTrue(result.answer().contains("Story"), "Query should surface the original source title");
    }
}
