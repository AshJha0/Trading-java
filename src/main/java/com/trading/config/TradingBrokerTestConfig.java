package com.trading.config;

/**
 * Configuration record for the TradingBrokerTest API client.
 *
 * <p>Use the {@link Builder} to construct an instance:
 * <pre>{@code
 * TradingBrokerTestConfig cfg = TradingBrokerTestConfig.builder()
 *     .apiKey("your-access-token")
 *     .build();
 * }</pre>
 *
 * <p>All fields are read from environment variables when the no-arg
 * {@link #fromEnvironment()} factory is used, matching the TradingBrokerTest convention:
 * <ul>
 *   <li>{@code TRADINGBROKERTEST_ACCESS_TOKEN} → apiKey</li>
 *   <li>{@code TRADINGBROKERTEST_REST_BASE_URL} → baseUrl (default {@value #DEFAULT_BASE_URL})</li>
 *   <li>{@code TRADINGBROKERTEST_REST_TIMEOUT_MS} → timeoutMs (default {@value #DEFAULT_TIMEOUT_MS})</li>
 * </ul>
 */
public record TradingBrokerTestConfig(
        String apiKey,
        String baseUrl,
        String sandboxUrl,
        Environment environment,
        long timeoutMs
) {

    /** Live production endpoint. */
    public static final String DEFAULT_BASE_URL    = "https://api.tradingbrokertest.com/v1";
    /** Paper-trading / sandbox endpoint. */
    public static final String DEFAULT_SANDBOX_URL = "https://sandbox.tradingbrokertest.com/v1";
    /** Default HTTP timeout in milliseconds. */
    public static final long   DEFAULT_TIMEOUT_MS  = 30_000L;

    /** Selects which TradingBrokerTest environment to target. */
    public enum Environment {
        PRODUCTION,
        SANDBOX
    }

    // -------------------------------------------------------------------------
    // Compact canonical constructor — validation
    // -------------------------------------------------------------------------

    public TradingBrokerTestConfig {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("apiKey must not be null or blank");
        }
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("baseUrl must not be null or blank");
        }
        if (environment == null) {
            throw new IllegalArgumentException("environment must not be null");
        }
        if (timeoutMs <= 0) {
            throw new IllegalArgumentException("timeoutMs must be positive");
        }
    }

    // -------------------------------------------------------------------------
    // Convenience factories
    // -------------------------------------------------------------------------

    /**
     * Reads configuration from environment variables, falling back to defaults.
     */
    public static TradingBrokerTestConfig fromEnvironment() {
        String key     = System.getenv("TRADINGBROKERTEST_ACCESS_TOKEN");
        String baseUrl = System.getenv().getOrDefault("TRADINGBROKERTEST_REST_BASE_URL", DEFAULT_BASE_URL);
        long   timeout;
        try {
            String raw = System.getenv("TRADINGBROKERTEST_REST_TIMEOUT_MS");
            timeout = (raw != null) ? Long.parseLong(raw) : DEFAULT_TIMEOUT_MS;
        } catch (NumberFormatException e) {
            timeout = DEFAULT_TIMEOUT_MS;
        }
        return new TradingBrokerTestConfig(
                (key != null) ? key : "default-key",
                baseUrl,
                DEFAULT_SANDBOX_URL,
                Environment.PRODUCTION,
                timeout
        );
    }

    /** Returns a new {@link Builder} pre-populated with sensible defaults. */
    public static Builder builder() {
        return new Builder();
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    public static final class Builder {
        private String      apiKey      = null;
        private String      baseUrl     = DEFAULT_BASE_URL;
        private String      sandboxUrl  = DEFAULT_SANDBOX_URL;
        private Environment environment = Environment.PRODUCTION;
        private long        timeoutMs   = DEFAULT_TIMEOUT_MS;

        private Builder() {}

        public Builder apiKey(String apiKey)           { this.apiKey = apiKey; return this; }
        public Builder baseUrl(String baseUrl)         { this.baseUrl = baseUrl; return this; }
        public Builder sandboxUrl(String sandboxUrl)   { this.sandboxUrl = sandboxUrl; return this; }
        public Builder environment(Environment env)    { this.environment = env; return this; }
        public Builder timeoutMs(long timeoutMs)       { this.timeoutMs = timeoutMs; return this; }

        public TradingBrokerTestConfig build() {
            return new TradingBrokerTestConfig(apiKey, baseUrl, sandboxUrl, environment, timeoutMs);
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Returns the effective REST base URL, choosing the sandbox URL when
     * {@link Environment#SANDBOX} is configured.
     */
    public String effectiveBaseUrl() {
        return (environment == Environment.SANDBOX) ? sandboxUrl : baseUrl;
    }
}
