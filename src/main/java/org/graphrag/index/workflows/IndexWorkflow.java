package org.graphrag.index.workflows;

import java.util.List;
import java.util.Objects;

import org.graphrag.index.input.InputDocument;
import org.graphrag.index.operations.BuildGraphArtifactsOperation;
import org.graphrag.index.operations.ChunkDocumentsOperation;
import org.graphrag.index.typing.GraphArtifacts;
import org.graphrag.index.text_splitting.TextSplitter;
import org.graphrag.index.typing.DocumentChunk;

/**
 * Coordinates the index pipeline steps similarly to the Python
 * {@code index.workflows}. Chunking, graph artifact creation, and persistence
 * are orchestrated externally by callers.
 */
public class IndexWorkflow {
    private final ChunkDocumentsOperation chunkDocumentsOperation;
    private final BuildGraphArtifactsOperation buildGraphArtifactsOperation;

    public IndexWorkflow(TextSplitter textSplitter) {
        Objects.requireNonNull(textSplitter, "textSplitter");
        this.chunkDocumentsOperation = new ChunkDocumentsOperation(textSplitter);
        this.buildGraphArtifactsOperation = new BuildGraphArtifactsOperation();
    }

    public GraphArtifacts run(List<InputDocument> documents) {
        List<DocumentChunk> chunks = chunkDocumentsOperation.apply(documents);
        return buildGraphArtifactsOperation.apply(chunks);
    }
}
