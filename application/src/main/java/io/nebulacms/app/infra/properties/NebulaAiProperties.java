package io.nebulacms.app.infra.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * AI integration properties for NebulaCMS.
 */
@Data
@ConfigurationProperties(prefix = "nebula.ai")
public class NebulaAiProperties {

    private final Ollama ollama = new Ollama();

    @Data
    public static class Ollama {

        /**
         * Enable Ollama-backed AI calls.
         */
        private boolean enabled;

        /**
         * Ollama HTTP endpoint.
         */
        private String baseUrl = "http://127.0.0.1:11434";

        /**
         * Default model name.
         */
        private String model = "llama3.1";

        /**
         * Request timeout text value, e.g. 30s.
         */
        private String timeout = "30s";
    }
}
