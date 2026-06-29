package com.trading.market;

import com.fasterxml.jackson.databind.JsonNode;
import com.trading.client.TradingBrokerTestClient;
import com.trading.error.TradingBrokerTestError;
import com.trading.market.types.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Wraps TradingBrokerTestClient with market-data specific methods.
 */
public class MarketDataClient {

    private final TradingBrokerTestClient client;

    public MarketDataClient(TradingBrokerTestClient client) {
        this.client = client;
    }

    public List<Quote> getQuotes(List<String> symbols) throws TradingBrokerTestError {
        String symbolsParam = String.join(",", symbols);
        String json = client.get("/markets/quotes?symbols=" + symbolsParam + "&greeks=false");
        try {
            JsonNode root = client.getMapper().readTree(json);
            JsonNode quotes = root.path("quotes").path("quote");
            List<Quote> result = new ArrayList<>();
            if (quotes.isArray()) {
                for (JsonNode node : quotes) {
                    result.add(client.getMapper().treeToValue(node, Quote.class));
                }
            } else if (!quotes.isMissingNode() && !quotes.isNull()) {
                result.add(client.getMapper().treeToValue(quotes, Quote.class));
            }
            return result;
        } catch (Exception e) {
            throw new TradingBrokerTestError.ParseError("Failed to parse quotes: " + e.getMessage(), e);
        }
    }

    public List<HistoricalQuote> getHistory(String symbol, Interval interval,
                                             LocalDate start, LocalDate end) throws TradingBrokerTestError {
        String path = "/markets/history?symbol=" + symbol
                + "&interval=" + interval.toWire()
                + "&start=" + start
                + "&end=" + end;
        String json = client.get(path);
        try {
            JsonNode root = client.getMapper().readTree(json);
            JsonNode days = root.path("history").path("day");
            List<HistoricalQuote> result = new ArrayList<>();
            if (days.isArray()) {
                for (JsonNode node : days) {
                    result.add(client.getMapper().treeToValue(node, HistoricalQuote.class));
                }
            } else if (!days.isMissingNode() && !days.isNull()) {
                result.add(client.getMapper().treeToValue(days, HistoricalQuote.class));
            }
            return result;
        } catch (Exception e) {
            throw new TradingBrokerTestError.ParseError("Failed to parse history: " + e.getMessage(), e);
        }
    }

    public List<OptionExpiration> getOptionExpirations(String symbol) throws TradingBrokerTestError {
        String json = client.get("/markets/options/expirations?symbol=" + symbol);
        try {
            JsonNode root = client.getMapper().readTree(json);
            JsonNode expirations = root.path("expirations").path("date");
            List<OptionExpiration> result = new ArrayList<>();
            if (expirations.isArray()) {
                for (JsonNode node : expirations) {
                    result.add(client.getMapper().treeToValue(node, OptionExpiration.class));
                }
            } else if (!expirations.isMissingNode() && !expirations.isNull()) {
                result.add(client.getMapper().treeToValue(expirations, OptionExpiration.class));
            }
            return result;
        } catch (Exception e) {
            throw new TradingBrokerTestError.ParseError("Failed to parse expirations: " + e.getMessage(), e);
        }
    }

    public MarketClock getMarketClock() throws TradingBrokerTestError {
        String json = client.get("/markets/clock");
        try {
            JsonNode root = client.getMapper().readTree(json);
            return client.getMapper().treeToValue(root.path("clock"), MarketClock.class);
        } catch (Exception e) {
            throw new TradingBrokerTestError.ParseError("Failed to parse clock: " + e.getMessage(), e);
        }
    }
}
