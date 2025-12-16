package org.graphrag.index.typing;

import java.util.List;
import org.graphrag.model.GraphDocument;
import org.graphrag.model.Relationship;
import org.graphrag.model.Entity;

/**
 * Bundled artifacts produced by the indexing pipeline.
 */
public record GraphArtifacts(List<GraphDocument> documents, List<Entity> entities, List<Relationship> relationships) {
}
