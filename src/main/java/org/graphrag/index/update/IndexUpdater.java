package org.graphrag.index.update;

import java.util.Objects;

import org.graphrag.GraphRagConfig;
import org.graphrag.GraphStore;
import org.graphrag.index.typing.GraphArtifacts;

/**
 * Applies computed index artifacts to the underlying {@link GraphStore}. This
 * mirrors the persistence phase in the Python {@code index.update} module.
 */
public class IndexUpdater {
    private final GraphStore graphStore;
    private final GraphRagConfig config;

    public IndexUpdater(GraphStore graphStore, GraphRagConfig config) {
        this.graphStore = Objects.requireNonNull(graphStore, "graphStore");
        this.config = Objects.requireNonNull(config, "config");
    }

    public void apply(GraphArtifacts artifacts) {
        Objects.requireNonNull(artifacts, "artifacts");
        graphStore.ingestArtifacts(config, artifacts.documents(), artifacts.entities(), artifacts.relationships());
    }
}
