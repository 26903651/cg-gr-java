package org.graphrag.index.text_splitting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.graphrag.index.input.InputDocument;
import org.graphrag.index.typing.DocumentChunk;
import org.graphrag.index.utils.IdGenerator;

/**
 * A trivial text splitter roughly analogous to the Python reference default
 * splitter. It splits on sentence boundaries and falls back to whitespace when
 * punctuation is missing, emitting chunk metadata that preserves ordering.
 */
public class SimpleTextSplitter implements TextSplitter {
    private static final Pattern SENTENCE_BOUNDARY = Pattern.compile("(?<=[.!?])\\s+");
    private final int maxSentencesPerChunk;
    private final IdGenerator idGenerator = new IdGenerator("chunk_");

    public SimpleTextSplitter() {
        this(3);
    }

    public SimpleTextSplitter(int maxSentencesPerChunk) {
        this.maxSentencesPerChunk = Math.max(1, maxSentencesPerChunk);
    }

    @Override
    public List<DocumentChunk> split(InputDocument document) {
        String[] sentences = SENTENCE_BOUNDARY.split(document.content().trim());
        if (sentences.length == 0) {
            sentences = new String[] {document.content()};
        }

        List<DocumentChunk> chunks = new ArrayList<>();
        List<String> buffer = new ArrayList<>();
        int chunkIndex = 0;
        for (String sentence : sentences) {
            buffer.add(sentence.trim());
            if (buffer.size() >= maxSentencesPerChunk) {
                chunks.add(createChunk(document, ++chunkIndex, buffer));
                buffer.clear();
            }
        }
        if (!buffer.isEmpty()) {
            chunks.add(createChunk(document, ++chunkIndex, buffer));
        }
        return chunks;
    }

    private DocumentChunk createChunk(InputDocument document, int chunkIndex, List<String> sentences) {
        String text = String.join(" ", sentences).trim();
        Map<String, String> metadata = new HashMap<>(document.metadata());
        metadata.put("chunk_index", Integer.toString(chunkIndex));
        metadata.put("source_title", document.title());
        return new DocumentChunk(idGenerator.nextId(), document.id(), chunkIndex, text, metadata);
    }
}
