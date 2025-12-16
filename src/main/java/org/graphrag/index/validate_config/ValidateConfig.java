package org.graphrag.index.validate_config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import org.graphrag.GraphRagConfig;

/**
 * Lightweight validator analogous to the Python {@code index.validate_config}
 * module. Ensures required paths and patterns are present before indexing.
 */
public final class ValidateConfig {
    private ValidateConfig() {
    }

    public static void validate(GraphRagConfig config) {
        Objects.requireNonNull(config, "config");
        Path workingDirectory = config.getWorkingDirectory();
        if (!Files.exists(workingDirectory)) {
            throw new IllegalArgumentException("Working directory does not exist: " + workingDirectory);
        }
        if (config.getIncludePatterns().isEmpty()) {
            throw new IllegalArgumentException("At least one include pattern must be provided.");
        }
    }
}
