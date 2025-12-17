package org.graphrag.index.workflows;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.graphrag.index.input.InputDocument;
import org.graphrag.index.operations.CreateGraphArtifactsOperation;
import org.graphrag.index.operations.CreateTextUnitsOperation;
import org.graphrag.index.operations.ChunkDocumentsOperation;
import org.graphrag.index.typing.GraphArtifacts;
import org.graphrag.index.text_splitting.TextSplitter;
import org.graphrag.index.typing.DocumentChunk;
import org.graphrag.index.typing.TextUnit;
import org.graphrag.model.GraphDocument;

/**
 * Coordinates the index pipeline steps similarly to the Python
 * {@code index.workflows}. Chunking, graph artifact creation, and persistence
 * are orchestrated externally by callers.
 */
public class IndexWorkflow {
    private final ChunkDocumentsOperation chunkDocumentsOperation;
    private final CreateTextUnitsOperation createTextUnitsOperation;
    private final CreateGraphArtifactsOperation createGraphArtifactsOperation;

    public IndexWorkflow(TextSplitter textSplitter) {
        Objects.requireNonNull(textSplitter, "textSplitter");
        this.chunkDocumentsOperation = new ChunkDocumentsOperation(textSplitter);
        this.createTextUnitsOperation = new CreateTextUnitsOperation();
        this.createGraphArtifactsOperation = new CreateGraphArtifactsOperation();
    }

    public GraphArtifacts run(List<InputDocument> documents) {
        Objects.requireNonNull(documents, "documents");
        List<GraphDocument> graphDocuments = documents.stream()
                .map(doc -> new GraphDocument(doc.id(), doc.title(), doc.content(), Instant.now()))
                .toList();

        List<DocumentChunk> chunks = chunkDocumentsOperation.apply(documents);
        List<TextUnit> textUnits = createTextUnitsOperation.apply(chunks);

        return createGraphArtifactsOperation.apply(graphDocuments, chunks, textUnits);
    }
}
