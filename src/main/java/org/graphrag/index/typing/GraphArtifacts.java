package org.graphrag.index.typing;

import java.util.List;

import org.graphrag.model.Entity;
import org.graphrag.model.GraphDocument;
import org.graphrag.model.Relationship;

/**
 * Bundled artifacts produced by the indexing pipeline.
 */
public record GraphArtifacts(
        List<GraphDocument> documents,
        List<DocumentChunk> chunks,
        List<TextUnit> textUnits,
        List<Entity> entities,
        List<Relationship> relationships) {}
