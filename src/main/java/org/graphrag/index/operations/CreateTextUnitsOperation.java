package org.graphrag.index.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.graphrag.index.typing.DocumentChunk;
import org.graphrag.index.typing.TextUnit;
import org.graphrag.index.utils.IdGenerator;

/**
 * Implements the Python {@code index.operations.create_text_units} behavior by
 * turning ordered document chunks into text units that preserve sequence
 * metadata for downstream graph construction.
 */
public class CreateTextUnitsOperation {
    private final IdGenerator idGenerator = new IdGenerator("tu_");

    public List<TextUnit> apply(List<DocumentChunk> chunks) {
        Objects.requireNonNull(chunks, "chunks");
        List<TextUnit> textUnits = new ArrayList<>();
        int position = 0;
        for (DocumentChunk chunk : chunks) {
            textUnits.add(new TextUnit(
                    idGenerator.nextId(), chunk.documentId(), chunk.id(), position++, chunk.text(), chunk.metadata()));
        }
        return textUnits;
    }
}
