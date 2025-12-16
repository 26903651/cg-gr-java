package org.graphrag;

/**
 * The component responsible for translating user questions into graph lookups
 * and LLM prompts. This mirrors the Python QueryEngine abstraction.
 */
public interface QueryEngine {
    QueryResult answer(String question, GraphContext context);
}
