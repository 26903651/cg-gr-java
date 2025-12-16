package org.graphrag;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration placeholder for the Java GraphRAG port. This mirrors the
 * structured configuration of the Python package in a minimal form so the rest
 * of the system can evolve without breaking the public surface.
 */
public class GraphRagConfig {
    private final Path workingDirectory;
    private final List<String> includePatterns;
    private final List<String> excludePatterns;

    // Needed for YAML deserialization
    private GraphRagConfig() {
        this.workingDirectory = Path.of(".");
        this.includePatterns = List.of("**/*.txt", "**/*.md");
        this.excludePatterns = List.of();
    }

    @JsonCreator
    public GraphRagConfig(
            @JsonProperty(value = "working_directory", required = true) Path workingDirectory,
            @JsonProperty(value = "include", defaultValue = "**/*.txt,**/*.md") List<String> includePatterns,
            @JsonProperty(value = "exclude", defaultValue = "") List<String> excludePatterns) {
        this.workingDirectory = Objects.requireNonNull(workingDirectory, "workingDirectory");
        this.includePatterns = List.copyOf(includePatterns);
        this.excludePatterns = List.copyOf(excludePatterns);
    }

    public Path getWorkingDirectory() {
        return workingDirectory;
    }

    public List<String> getIncludePatterns() {
        return includePatterns;
    }

    public List<String> getExcludePatterns() {
        return excludePatterns;
    }
}
