package org.graphrag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.graphrag.index.input.InputDocument;
import org.graphrag.index.run.IndexRunner;
import org.graphrag.index.validate_config.ValidateConfig;
import org.graphrag.model.GraphDocument;

/**
 * Entry point for a Java reimplementation of Microsoft's GraphRAG 2.7.0.
 * <p>
 * The original reference implementation is written in Python. This class
 * provides the top-level orchestration surface in Java and will be expanded to
 * mirror the Python behavior over time. The current version focuses on
 * defining the core lifecycle hooks so that additional modules (graph storage,
 * embedding, retrieval) can be implemented incrementally.
 */
public class GraphRag {
    private final GraphRagConfig config;
    private final GraphStore graphStore;
    private final QueryEngine queryEngine;
    private final IndexRunner indexRunner;

    public static GraphRag fromConfigFile(Path configPath, GraphStore store, QueryEngine queryEngine) {
        Objects.requireNonNull(configPath, "configPath");
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            GraphRagConfig config = mapper.readValue(Files.readAllBytes(configPath), GraphRagConfig.class);
            return new GraphRag(config, store, queryEngine);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load GraphRAG configuration from " + configPath, e);
        }
    }

    public GraphRag(GraphRagConfig config, GraphStore graphStore, QueryEngine queryEngine) {
        this.config = Objects.requireNonNull(config, "config");
        this.graphStore = Objects.requireNonNull(graphStore, "graphStore");
        this.queryEngine = Objects.requireNonNull(queryEngine, "queryEngine");
        ValidateConfig.validate(config);
        this.indexRunner = new IndexRunner(config, graphStore);
    }

    /**
     * In the Python implementation this method would run the end-to-end GraphRAG
     * workflow: load data, build a heterogeneous graph, embed nodes, and serve
     * retrieval-augmented answers. Here we expose a simplified entry point to be
     * fleshed out in parity with the Python pipeline.
     */
    public QueryResult query(String question) {
        GraphContext context = graphStore.ensureReady();
        return queryEngine.answer(question, context);
    }

    /**
     * Convenience helper for seeding the store with a batch of documents prior to
     * querying. Mirrors the ingestion hooks in the Python package.
     */
    public void ingest(List<GraphDocument> documents) {
        graphStore.ingest(config, documents);
    }

    /**
     * Java-friendly entry point for the Python {@code index.run} module. Accepts
     * raw input documents, runs the configured indexing pipeline, and persists
     * the resulting graph artifacts.
     */
    public void index(List<InputDocument> documents) {
        indexRunner.run(documents);
    }
}
