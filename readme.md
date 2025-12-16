# GraphRAG Java Scaffold

This repository tracks a Java-oriented implementation of Microsoft's GraphRAG 2.7.0. The goal is to mirror the Python release end-to-end. The current codebase now includes runnable defaults (YAML config loader, in-memory graph store, and a deterministic query pipeline) to make the port verifiable while more advanced LLM-backed behaviors are added.

## Status
- Maven project configured for Java 17 with Jackson for YAML parsing and JUnit 5 for tests.
- Core types now backed by runnable defaults:
  - `GraphRag` supports YAML config loading, ingestion, and querying.
  - `GraphRagConfig` models working directory and file patterns to parallel the Python config surface.
  - `GraphStore` ships with an `InMemoryGraphStore` that performs naive entity extraction and edge linking from documents.
  - `SimpleQueryEngine` retrieves matching documents and synthesizes text responses while emitting trace metadata.
- No embedding pipeline or LLM integration is included yet; these will be layered on in subsequent steps to reach full parity.

## Next steps
- Port the Python embedding/indexing pipeline (entity extraction, community detection, vector search) and plug it into the store.
- Introduce prompt templating and LLM adapters to synthesize answers with the same structure as the Python output schema.
- Add persistence-aware stores (SQLite/PG/vector DB) to mirror the Python backends.
