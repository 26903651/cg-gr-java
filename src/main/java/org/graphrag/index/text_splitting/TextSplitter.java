package org.graphrag.index.text_splitting;

import java.util.List;

import org.graphrag.index.input.InputDocument;
import org.graphrag.index.typing.DocumentChunk;

/**
 * Java counterpart to the Python {@code index.text_splitting} interfaces. Takes
 * raw {@link InputDocument} instances and returns ordered {@link DocumentChunk}
 * slices.
 */
public interface TextSplitter {
    List<DocumentChunk> split(InputDocument document);
}
