package com.trading.streaming;

/**
 * Stub interface for TradingBrokerTest streaming sessions. Full implementation is TODO.
 */
public interface StreamingSession {

    /**
     * Creates a new streaming session and returns the session token.
     *
     * @return session token string
     */
    String createSession();

    /**
     * Subscribes to a list of symbols on the given session.
     *
     * @param sessionId the session token
     * @param symbols   the symbols to subscribe to
     */
    void subscribe(String sessionId, java.util.List<String> symbols);
}
