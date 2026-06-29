package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.trading.accounts.types.*;
import com.trading.config.TradingBrokerTestConfig;
import com.trading.error.TradingBrokerTestError;
import com.trading.market.types.*;
import com.trading.trading.types.*;
import com.trading.user.types.*;
import com.trading.watchlists.types.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Trading-java — no real HTTP calls, all JSON parsed in-process.
 */
class TradingTest {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    // -------------------------------------------------------------------------
    // 1. TradingBrokerTestConfig: default production URL set
    // -------------------------------------------------------------------------
    @Test
    void test01_defaultProductionUrl() {
        TradingBrokerTestConfig cfg = TradingBrokerTestConfig.builder().apiKey("test-key").build();
        assertEquals("https://api.tradingbrokertest.com/v1", cfg.baseUrl());
        assertEquals(TradingBrokerTestConfig.Environment.PRODUCTION, cfg.environment());
    }

    // -------------------------------------------------------------------------
    // 2. TradingBrokerTestConfig: sandbox URL is different from production URL
    // -------------------------------------------------------------------------
    @Test
    void test02_sandboxUrlDifferentFromProduction() {
        assertNotEquals(TradingBrokerTestConfig.DEFAULT_BASE_URL, TradingBrokerTestConfig.DEFAULT_SANDBOX_URL);
        assertTrue(TradingBrokerTestConfig.DEFAULT_SANDBOX_URL.contains("sandbox"));
    }

    // -------------------------------------------------------------------------
    // 3. TradingBrokerTestConfig: apiKey stored correctly
    // -------------------------------------------------------------------------
    @Test
    void test03_apiKeyStoredCorrectly() {
        TradingBrokerTestConfig cfg = TradingBrokerTestConfig.builder().apiKey("my-secret-key").build();
        assertEquals("my-secret-key", cfg.apiKey());
    }

    // -------------------------------------------------------------------------
    // 4. TradingBrokerTestError.ApiError: statusCode and message accessible
    // -------------------------------------------------------------------------
    @Test
    void test04_apiErrorStatusCodeAndMessage() {
        TradingBrokerTestError.ApiError err = new TradingBrokerTestError.ApiError(404, "Not Found");
        assertEquals(404, err.statusCode());
        assertTrue(err.getMessage().contains("404"));
        assertTrue(err.getMessage().contains("Not Found"));
    }

    // -------------------------------------------------------------------------
    // 5. TradingBrokerTestError.ParseError: message accessible
    // -------------------------------------------------------------------------
    @Test
    void test05_parseErrorMessage() {
        TradingBrokerTestError.ParseError err = new TradingBrokerTestError.ParseError("unexpected token");
        assertTrue(err.getMessage().contains("unexpected token"));
    }

    // -------------------------------------------------------------------------
    // 6. TradingBrokerTestError.NetworkError caught as TradingBrokerTestError supertype
    // -------------------------------------------------------------------------
    @Test
    void test06_networkErrorCaughtAsSupertype() {
        TradingBrokerTestError err = new TradingBrokerTestError.NetworkError("connection refused");
        assertInstanceOf(TradingBrokerTestError.class, err);
        assertTrue(err.getMessage().contains("connection refused"));
    }

    // -------------------------------------------------------------------------
    // 7. OrderSide: all values present and @JsonProperty present
    // -------------------------------------------------------------------------
    @Test
    void test07_orderSideAllValues() {
        OrderSide[] values = OrderSide.values();
        assertTrue(values.length >= 4);
        assertNotNull(OrderSide.BUY);
        assertNotNull(OrderSide.SELL);
        assertNotNull(OrderSide.BUY_TO_COVER);
        assertNotNull(OrderSide.SELL_SHORT);
    }

    @Test
    void test07b_orderSideJsonPropertyAnnotation() throws Exception {
        Field f = OrderSide.class.getField("BUY");
        com.fasterxml.jackson.annotation.JsonProperty jp =
                f.getAnnotation(com.fasterxml.jackson.annotation.JsonProperty.class);
        assertNotNull(jp, "@JsonProperty must be present on BUY");
        assertEquals("buy", jp.value());
    }

    // -------------------------------------------------------------------------
    // 8. OrderType: all 7 values
    // -------------------------------------------------------------------------
    @Test
    void test08_orderTypeAllValues() {
        assertEquals(7, OrderType.values().length);
        assertNotNull(OrderType.MARKET);
        assertNotNull(OrderType.LIMIT);
        assertNotNull(OrderType.STOP);
        assertNotNull(OrderType.STOP_LIMIT);
        assertNotNull(OrderType.DEBIT);
        assertNotNull(OrderType.CREDIT);
        assertNotNull(OrderType.EVEN);
    }

    // -------------------------------------------------------------------------
    // 9. OrderDuration: DAY, GTC, PRE, POST
    // -------------------------------------------------------------------------
    @Test
    void test09_orderDurationValues() {
        assertNotNull(OrderDuration.DAY);
        assertNotNull(OrderDuration.GTC);
        assertNotNull(OrderDuration.PRE);
        assertNotNull(OrderDuration.POST);
        assertEquals(4, OrderDuration.values().length);
    }

    // -------------------------------------------------------------------------
    // 10. OrderClass: EQUITY, OPTION, COMBO, MULTILEG
    // -------------------------------------------------------------------------
    @Test
    void test10_orderClassValues() {
        assertNotNull(OrderClass.EQUITY);
        assertNotNull(OrderClass.OPTION);
        assertNotNull(OrderClass.COMBO);
        assertNotNull(OrderClass.MULTILEG);
        assertEquals(4, OrderClass.values().length);
    }

    // -------------------------------------------------------------------------
    // 11. OrderStatus: key statuses present
    // -------------------------------------------------------------------------
    @Test
    void test11_orderStatusValues() {
        assertNotNull(OrderStatus.PENDING);
        assertNotNull(OrderStatus.OPEN);
        assertNotNull(OrderStatus.PARTIALLY_FILLED);
        assertNotNull(OrderStatus.FILLED);
        assertNotNull(OrderStatus.CANCELLED);
        assertNotNull(OrderStatus.REJECTED);
        assertNotNull(OrderStatus.EXPIRED);
        assertNotNull(OrderStatus.ERROR);
        assertTrue(OrderStatus.values().length >= 8);
    }

    // -------------------------------------------------------------------------
    // 12. OrderRequest: equity builder creates valid request
    // -------------------------------------------------------------------------
    @Test
    void test12_orderRequestEquityBuilder() {
        OrderRequest req = OrderRequest.equityOrder("AAPL", OrderSide.BUY, 100,
                        OrderType.LIMIT, OrderDuration.DAY)
                .price(150.00)
                .accountId("ACC123")
                .build();

        assertEquals("AAPL", req.getSymbol());
        assertEquals(OrderSide.BUY, req.getSide());
        assertEquals(100, req.getQty(), 0.0001);
        assertEquals(OrderType.LIMIT, req.getType());
        assertEquals(OrderDuration.DAY, req.getDuration());
        assertEquals(150.00, req.getPrice(), 0.0001);
        assertEquals(OrderClass.EQUITY, req.getClazz());
        assertEquals("ACC123", req.getAccountId());
    }

    // -------------------------------------------------------------------------
    // 13. OrderRequest: missing required field throws
    // -------------------------------------------------------------------------
    @Test
    void test13_orderRequestValidationThrows() {
        assertThrows(IllegalStateException.class, () ->
                OrderRequest.equityOrder(null, OrderSide.BUY, 10, OrderType.MARKET, OrderDuration.DAY)
                        .build());
    }

    // -------------------------------------------------------------------------
    // 14. Quote: deserialize from JSON
    // -------------------------------------------------------------------------
    @Test
    void test14_quoteDeserialize() throws Exception {
        String json = """
                {"symbol":"AAPL","last":150.25,"bid":150.20,"ask":150.30,
                 "open":149.00,"high":151.00,"low":148.50,"close":149.75,"volume":5000000}
                """;
        Quote q = mapper.readValue(json, Quote.class);
        assertEquals("AAPL", q.symbol());
        assertEquals(150.25, q.last(), 0.001);
        assertEquals(150.20, q.bid(), 0.001);
        assertEquals(150.30, q.ask(), 0.001);
        assertEquals(5_000_000L, q.volume());
    }

    // -------------------------------------------------------------------------
    // 15. HistoricalQuote: deserialize from JSON
    // -------------------------------------------------------------------------
    @Test
    void test15_historicalQuoteDeserialize() throws Exception {
        String json = """
                {"date":"2024-01-15","open":148.50,"high":151.00,"low":148.00,"close":150.25,"volume":3200000}
                """;
        HistoricalQuote hq = mapper.readValue(json, HistoricalQuote.class);
        assertEquals(148.50, hq.open(), 0.001);
        assertEquals(150.25, hq.close(), 0.001);
        assertEquals(3_200_000L, hq.volume());
    }

    // -------------------------------------------------------------------------
    // 16. Order: deserialize from JSON with key fields
    // -------------------------------------------------------------------------
    @Test
    void test16_orderDeserialize() throws Exception {
        String json = """
                {"id":12345,"type":"limit","symbol":"AAPL","side":"buy","quantity":100.0,
                 "status":"open","duration":"day","price":150.00,"avg_fill_price":0.0,
                 "exec_quantity":0.0,"last_fill_price":0.0,"last_fill_quantity":0.0,"remain_quantity":100.0}
                """;
        Order order = mapper.readValue(json, Order.class);
        assertEquals(12345L, order.getId());
        assertEquals(OrderType.LIMIT, order.getType());
        assertEquals("AAPL", order.getSymbol());
        assertEquals(OrderSide.BUY, order.getSide());
        assertEquals(100.0, order.getQuantity(), 0.001);
        assertEquals(OrderStatus.OPEN, order.getStatus());
        assertEquals(OrderDuration.DAY, order.getDuration());
        assertEquals(150.00, order.getPrice(), 0.001);
        assertEquals(100.0, order.getRemainQuantity(), 0.001);
    }

    // -------------------------------------------------------------------------
    // 17. Account: record construction
    // -------------------------------------------------------------------------
    @Test
    void test17_accountRecordConstruction() {
        Account acc = new Account("ACC123", "individual", "2020-01-01",
                false, 2, "active", "margin", "2024-01-01");
        assertEquals("ACC123", acc.accountNumber());
        assertEquals("individual", acc.classification());
        assertFalse(acc.dayTrader());
        assertEquals(2, acc.optionLevel());
    }

    // -------------------------------------------------------------------------
    // 18. AccountBalance: fields accessible
    // -------------------------------------------------------------------------
    @Test
    void test18_accountBalanceFields() throws Exception {
        String json = """
                {"option_short_value":0.0,"total_equity":50000.0,"account_number":"ACC123",
                 "account_type":"margin","close_pl":100.0,"current_requirement":0.0,"equity":50000.0,
                 "equity_percent":100.0,"long_market_value":50000.0,"market_value":50000.0,
                 "open_pl":200.0,"option_long_value":0.0,"option_requirement":0.0,
                 "pending_orders_count":0,"short_market_value":0.0,"stock_long_value":50000.0,
                 "total_cash":10000.0,"uncleared_funds":0.0,"pending_cash":0.0,
                 "margin":{"fed_call":0.0,"maintenance_call":0.0,"option_buying_power":25000.0,
                           "stock_buying_power":100000.0,"stock_short_value":0.0,"sweep":0.0}}
                """;
        AccountBalance bal = mapper.readValue(json, AccountBalance.class);
        assertEquals("ACC123", bal.accountNumber());
        assertEquals(50000.0, bal.totalEquity(), 0.001);
        assertEquals(200.0, bal.openPl(), 0.001);
        assertNotNull(bal.margin());
        assertEquals(100000.0, bal.margin().stockBuyingPower(), 0.001);
    }

    // -------------------------------------------------------------------------
    // 19. Position: costBasis, quantity, symbol
    // -------------------------------------------------------------------------
    @Test
    void test19_positionFields() {
        Position pos = new Position(15000.0, "2023-06-01T00:00:00Z", 1L, 100.0, "AAPL");
        assertEquals(15000.0, pos.costBasis(), 0.001);
        assertEquals(100.0, pos.quantity(), 0.001);
        assertEquals("AAPL", pos.symbol());
    }

    // -------------------------------------------------------------------------
    // 20. GainLoss: gainLossPercent accessible
    // -------------------------------------------------------------------------
    @Test
    void test20_gainLossFields() {
        GainLoss gl = new GainLoss("2024-01-15", 15000.0, 500.0, 3.33,
                "2023-06-01", 15500.0, 100.0, "AAPL", 1);
        assertEquals(3.33, gl.gainLossPercent(), 0.001);
        assertEquals("AAPL", gl.symbol());
        assertEquals(500.0, gl.gainLoss(), 0.001);
    }

    // -------------------------------------------------------------------------
    // 21. Watchlist: items list construction
    // -------------------------------------------------------------------------
    @Test
    void test21_watchlistConstruction() {
        WatchlistItem item = new WatchlistItem(1L, "wl-123", "AAPL", "2024-01-01");
        Watchlist wl = new Watchlist("wl-123", "Tech Stocks", "pub-456", List.of(item));
        assertEquals("wl-123", wl.id());
        assertEquals("Tech Stocks", wl.name());
        assertEquals(1, wl.items().size());
        assertEquals("AAPL", wl.items().get(0).symbol());
    }

    // -------------------------------------------------------------------------
    // 22. Interval: DAILY, WEEKLY, MONTHLY enum values
    // -------------------------------------------------------------------------
    @Test
    void test22_intervalValues() {
        assertNotNull(Interval.DAILY);
        assertNotNull(Interval.WEEKLY);
        assertNotNull(Interval.MONTHLY);
        assertEquals("daily",   Interval.DAILY.toWire());
        assertEquals("weekly",  Interval.WEEKLY.toWire());
        assertEquals("monthly", Interval.MONTHLY.toWire());
    }

    // -------------------------------------------------------------------------
    // 23. TradingBrokerTestConfig: effectiveBaseUrl returns sandboxUrl in SANDBOX mode
    // -------------------------------------------------------------------------
    @Test
    void test23_effectiveBaseUrlSandbox() {
        TradingBrokerTestConfig cfg = TradingBrokerTestConfig.builder()
                .apiKey("test-key")
                .environment(TradingBrokerTestConfig.Environment.SANDBOX)
                .build();
        assertEquals(TradingBrokerTestConfig.DEFAULT_SANDBOX_URL, cfg.effectiveBaseUrl());
    }

    // -------------------------------------------------------------------------
    // 24. TradingBrokerTestConfig: effectiveBaseUrl returns baseUrl in PRODUCTION mode
    // -------------------------------------------------------------------------
    @Test
    void test24_effectiveBaseUrlProduction() {
        TradingBrokerTestConfig cfg = TradingBrokerTestConfig.builder().apiKey("test-key").build();
        assertEquals(TradingBrokerTestConfig.DEFAULT_BASE_URL, cfg.effectiveBaseUrl());
    }

    // -------------------------------------------------------------------------
    // 25. OrderResponse: record fields
    // -------------------------------------------------------------------------
    @Test
    void test25_orderResponseRecord() throws Exception {
        String json = """
                {"order":{"id":99887,"status":"ok","partner_id":""}}
                """;
        com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(json);
        OrderResponse resp = mapper.treeToValue(root.path("order"), OrderResponse.class);
        assertEquals(99887L, resp.id());
        assertEquals("ok", resp.status());
    }

    // -------------------------------------------------------------------------
    // 26. UserProfile: record construction
    // -------------------------------------------------------------------------
    @Test
    void test26_userProfileConstruction() {
        UserAccount ua = new UserAccount("ACC999", "individual", false, 2, "active", "margin");
        UserProfile up = new UserProfile("user-1", "John Doe", List.of(ua));
        assertEquals("user-1", up.id());
        assertEquals("John Doe", up.name());
        assertEquals(1, up.accounts().size());
        assertEquals("ACC999", up.accounts().get(0).accountNumber());
    }

    // -------------------------------------------------------------------------
    // 27. TradingBrokerTestConfig: blank apiKey throws
    // -------------------------------------------------------------------------
    @Test
    void test27_blankApiKeyThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                TradingBrokerTestConfig.builder().apiKey("  ").build());
    }

    // -------------------------------------------------------------------------
    // 28. OrderRequest: LIMIT order requires price
    // -------------------------------------------------------------------------
    @Test
    void test28_limitOrderRequiresPrice() {
        assertThrows(IllegalStateException.class, () ->
                OrderRequest.equityOrder("AAPL", OrderSide.BUY, 10, OrderType.LIMIT, OrderDuration.DAY)
                        .build());
    }

    // -------------------------------------------------------------------------
    // 29. OrderType deserialize from JSON snake_case
    // -------------------------------------------------------------------------
    @Test
    void test29_orderTypeDeserializeStopLimit() throws Exception {
        String json = """
                {"id":1,"type":"stop_limit","symbol":"GOOG","side":"sell","quantity":5.0,
                 "status":"pending","duration":"gtc","price":200.0,"stop":195.0,
                 "avg_fill_price":0.0,"exec_quantity":0.0,"last_fill_price":0.0,
                 "last_fill_quantity":0.0,"remain_quantity":5.0}
                """;
        Order order = mapper.readValue(json, Order.class);
        assertEquals(OrderType.STOP_LIMIT, order.getType());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(OrderDuration.GTC, order.getDuration());
    }

    // -------------------------------------------------------------------------
    // 30. TradingBrokerTestError.RateLimitError is a TradingBrokerTestError
    // -------------------------------------------------------------------------
    @Test
    void test30_rateLimitErrorIsTradingBrokerTestError() {
        TradingBrokerTestError err = new TradingBrokerTestError.RateLimitError();
        assertInstanceOf(TradingBrokerTestError.class, err);
        assertTrue(err.getMessage().contains("Rate limit"));
    }
}
