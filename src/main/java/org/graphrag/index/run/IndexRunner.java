package org.graphrag.index.run;

import java.util.List;
import java.util.Objects;

import org.graphrag.GraphRagConfig;
import org.graphrag.GraphStore;
import org.graphrag.index.input.InputDocument;
import org.graphrag.index.text_splitting.SimpleTextSplitter;
import org.graphrag.index.text_splitting.TextSplitter;
import org.graphrag.index.update.IndexUpdater;
import org.graphrag.index.workflows.IndexWorkflow;

/**
 * Entry point mirroring the Python {@code index.run} module. Wires together the
 * text splitter, workflow, and persistence components for a one-shot indexing
 * job.
 */
public class IndexRunner {
    private final IndexWorkflow workflow;
    private final IndexUpdater updater;

    public IndexRunner(GraphRagConfig config, GraphStore graphStore) {
        this(config, graphStore, new SimpleTextSplitter());
    }

    public IndexRunner(GraphRagConfig config, GraphStore graphStore, TextSplitter textSplitter) {
        Objects.requireNonNull(config, "config");
        Objects.requireNonNull(graphStore, "graphStore");
        Objects.requireNonNull(textSplitter, "textSplitter");
        this.workflow = new IndexWorkflow(textSplitter);
        this.updater = new IndexUpdater(graphStore, config);
    }

    public void run(List<InputDocument> documents) {
        var artifacts = workflow.run(documents);
        updater.apply(artifacts);
    }
}
