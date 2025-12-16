package org.graphrag.index.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.graphrag.index.input.InputDocument;
import org.graphrag.index.text_splitting.TextSplitter;
import org.graphrag.index.typing.DocumentChunk;

/**
 * Mirrors the Python {@code index.operations.chunk_documents} step. Applies the
 * configured {@link TextSplitter} to every input document and preserves the
 * original ordering.
 */
public class ChunkDocumentsOperation {
    private final TextSplitter textSplitter;

    public ChunkDocumentsOperation(TextSplitter textSplitter) {
        this.textSplitter = Objects.requireNonNull(textSplitter, "textSplitter");
    }

    public List<DocumentChunk> apply(List<InputDocument> documents) {
        Objects.requireNonNull(documents, "documents");
        List<DocumentChunk> chunks = new ArrayList<>();
        for (InputDocument document : documents) {
            chunks.addAll(textSplitter.split(document));
        }
        return chunks;
    }
}
