package com.trading.error;

/**
 * Sealed exception hierarchy for all errors produced by the TradingBrokerTest client library.
 *
 * <p>Use a {@code switch} expression over the sealed hierarchy to handle every variant:
 * <pre>{@code
 * try { ... }
 * catch (TradingBrokerTestError e) {
 *     switch (e) {
 *         case TradingBrokerTestError.ApiError ae   -> System.err.println("HTTP " + ae.statusCode());
 *         case TradingBrokerTestError.AuthError ae  -> System.err.println("Auth: " + ae.getMessage());
 *         case TradingBrokerTestError.ParseError pe -> System.err.println("Parse: " + pe.getMessage());
 *         case TradingBrokerTestError.NetworkError ne -> System.err.println("Network: " + ne.getMessage());
 *         case TradingBrokerTestError.RateLimitError re -> System.err.println("Rate limited");
 *     }
 * }
 * }</pre>
 */
public sealed class TradingBrokerTestError extends Exception
        permits TradingBrokerTestError.ApiError,
                TradingBrokerTestError.ParseError,
                TradingBrokerTestError.NetworkError,
                TradingBrokerTestError.AuthError,
                TradingBrokerTestError.RateLimitError {

    protected TradingBrokerTestError(String message) {
        super(message);
    }

    protected TradingBrokerTestError(String message, Throwable cause) {
        super(message, cause);
    }

    // -------------------------------------------------------------------------
    // Variants
    // -------------------------------------------------------------------------

    /** The TradingBrokerTest API returned a non-success HTTP status code. */
    public static final class ApiError extends TradingBrokerTestError {
        private final int statusCode;

        public ApiError(int statusCode, String message) {
            super("API error " + statusCode + ": " + message);
            this.statusCode = statusCode;
        }

        /** HTTP status code returned by the API (e.g. 400, 404, 500). */
        public int statusCode() {
            return statusCode;
        }
    }

    /** A JSON response body could not be parsed into the expected type. */
    public static final class ParseError extends TradingBrokerTestError {
        public ParseError(String msg) {
            super("Parse error: " + msg);
        }

        public ParseError(String msg, Throwable cause) {
            super("Parse error: " + msg, cause);
        }
    }

    /** A network-level failure occurred (timeout, connection refused, etc.). */
    public static final class NetworkError extends TradingBrokerTestError {
        public NetworkError(String msg) {
            super("Network error: " + msg);
        }

        public NetworkError(String msg, Throwable cause) {
            super("Network error: " + msg, cause);
        }
    }

    /** Authentication failed — invalid or missing API key / access token. */
    public static final class AuthError extends TradingBrokerTestError {
        public AuthError(String msg) {
            super("Auth error: " + msg);
        }
    }

    /** The API rate limit was exceeded. Callers should back off and retry. */
    public static final class RateLimitError extends TradingBrokerTestError {
        public RateLimitError() {
            super("Rate limit exceeded");
        }

        public RateLimitError(String msg) {
            super("Rate limit exceeded: " + msg);
        }
    }
}
