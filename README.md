# Trading — Java

**Trading-java** is a Java 24 client library for the [TradingBrokerTest](https://tradingbrokertest.com) ( This is just for Testing purpose and locally build and not for general purpose) tradingbrokertestage REST API. It provides typed, fully deserialized access to market data (real-time quotes, options chains, historical OHLCV prices, market clock), order management (equity and option orders — placement, inspection, modification, and cancellation), account information (balances, positions, gain/loss reporting), user profile, and watchlists. The library is built on OkHttp3 for HTTP transport and Jackson for JSON deserialization, and supports both the live production and paper-trading sandbox environments through a sealed `TradingBrokerTestError` exception hierarchy.

---

## Features

- **Typed REST API client** — each domain area has its own high-level client class (`MarketDataClient`, `TradingClient`, `AccountsClient`)
- **Market data** — real-time multi-symbol quotes, OHLCV history, option expiration dates, market clock
- **Order placement and management** — equity and option orders; place, list, get, modify, cancel
- **Account balances and positions** — full `AccountBalance` record including margin details
- **Gain/loss reporting** — closed-position P&L history
- **Watchlists** — named watchlists with per-item symbol tracking
- **Sandbox and production environments** — switch with a single `Environment` enum value
- **Sealed `TradingBrokerTestError` hierarchy** — `ApiError`, `ParseError`, `NetworkError`, `AuthError`, `RateLimitError`
- **Full Jackson JSON deserialization** — all types are annotated with `@JsonProperty` and `@JsonIgnoreProperties(ignoreUnknown = true)`
- **Environment-variable configuration** — `TradingBrokerTestConfig.fromEnvironment()` reads `TRADINGBROKERTEST_ACCESS_TOKEN` and optional overrides

---

## Requirements

| Requirement | Version |
|---|---|
| Java | 24+ (preview features enabled) |
| Maven | 3.9+ |
| TradingBrokerTest API key | Sandbox or production key from the TradingBrokerTest developer portal |

---

## Build & Test

```bash
mvn compile
mvn test        # 30 tests — unit tests only; no live API calls required
mvn package
```

The test suite runs with `--enable-preview` and ZGC (`-XX:+UseZGC -XX:+ZGenerational`).

---

## Authentication

Obtain an access token from the [TradingBrokerTest developer portal](https://developer.tradingbrokertest.com). Sandbox tokens are free and suitable for development; production tokens require a funded tradingbrokertestage account.

```java
// Minimal production configuration
TradingBrokerTestConfig config = TradingBrokerTestConfig.builder()
    .apiKey("your-access-token")
    .build();

// Sandbox configuration
TradingBrokerTestConfig sandbox = TradingBrokerTestConfig.builder()
    .apiKey("your-sandbox-token")
    .environment(TradingBrokerTestConfig.Environment.SANDBOX)
    .build();

// Load from environment variables
// Reads TRADINGBROKERTEST_ACCESS_TOKEN, TRADINGBROKERTEST_REST_BASE_URL, TRADINGBROKERTEST_REST_TIMEOUT_MS
TradingBrokerTestConfig envConfig = TradingBrokerTestConfig.fromEnvironment();
```

The effective base URL is resolved automatically: `https://api.tradingbrokertest.com/v1` for production and `https://sandbox.tradingbrokertest.com/v1` for sandbox.

---

## Project Structure

```
com.trading
├── client
│   └── TradingBrokerTestClient.java          # Low-level OkHttp3 wrapper (GET/POST/PUT/DELETE)
├── config
│   └── TradingBrokerTestConfig.java          # Configuration record + Builder + Environment enum
├── error
│   └── TradingBrokerTestError.java           # Sealed exception hierarchy
├── market
│   ├── MarketDataClient.java        # High-level market data operations
│   └── types
│       ├── GreeksData.java          # Option Greeks (delta, gamma, theta, vega, rho)
│       ├── HistoricalQuote.java     # OHLCV bar (date, open, high, low, close, volume)
│       ├── Interval.java            # DAILY / WEEKLY / MONTHLY history intervals
│       ├── MarketClock.java         # Market status, next state, and times
│       ├── OptionExpiration.java    # Single option expiry date
│       ├── Quote.java               # Real-time quote (bid/ask/last + option fields)
│       ├── TimeSale.java            # Intraday time-and-sales tick
│       └── TimeSalesInterval.java   # Intraday interval enum (tick, 1min, 5min, …)
├── trading
│   ├── TradingClient.java           # High-level order management operations
│   └── types
│       ├── Order.java               # Full order record (id, status, legs, fills, …)
│       ├── OrderClass.java          # EQUITY / OPTION / COMBO / MULTILEG
│       ├── OrderDuration.java       # DAY / GTC / PRE / POST
│       ├── OrderLeg.java            # Single leg within a multi-leg order
│       ├── OrderRequest.java        # Builder for constructing order requests
│       ├── OrderResponse.java       # Response envelope (id, status, partner_id)
│       ├── OrderSide.java           # BUY / SELL / BUY_TO_OPEN / … (8 values)
│       ├── OrderStatus.java         # PENDING / OPEN / FILLED / CANCELLED / … (8+ values)
│       └── OrderType.java           # MARKET / LIMIT / STOP / STOP_LIMIT / … (7 values)
├── accounts
│   ├── AccountsClient.java          # High-level account management operations
│   └── types
│       ├── Account.java             # Account summary (number, classification, status)
│       ├── AccountBalance.java      # Full balance record including margin sub-record
│       ├── GainLoss.java            # Closed-position gain/loss entry
│       ├── Margin.java              # Margin details (buying power, calls, sweep)
│       └── Position.java            # Open position (symbol, quantity, cost basis)
├── user
│   └── types
│       ├── UserAccount.java         # Account stub in the user profile
│       └── UserProfile.java         # User profile (id, name, linked accounts)
├── watchlists
│   └── types
│       ├── Watchlist.java           # Named watchlist with items list
│       └── WatchlistItem.java       # Single symbol in a watchlist
├── streaming
│   └── StreamingSession.java        # Stub interface for WebSocket streaming (TODO)
└── common
    ├── AccountType.java             # Account-type enum (MARGIN, CASH, PDT, …)
    └── SortOrder.java               # Sort direction enum (ASC / DESC)
```

---

## Quick Start

### Market Data

```java
TradingBrokerTestConfig config = TradingBrokerTestConfig.builder()
    .apiKey("your-access-token")
    .build();

TradingBrokerTestClient client = new TradingBrokerTestClient(config);
MarketDataClient market = new MarketDataClient(client);

// Real-time quotes for multiple symbols
List<Quote> quotes = market.getQuotes(List.of("AAPL", "TSLA", "SPY"));
quotes.forEach(q -> System.out.printf("%s: bid=%.2f ask=%.2f last=%.2f%n",
    q.symbol(), q.bid(), q.ask(), q.last()));

// Historical OHLCV prices
List<HistoricalQuote> history = market.getHistory(
    "AAPL",
    Interval.DAILY,
    LocalDate.of(2025, 1, 1),
    LocalDate.of(2025, 3, 31)
);
history.forEach(h -> System.out.printf("%s: open=%.2f close=%.2f vol=%d%n",
    h.date(), h.open(), h.close(), h.volume()));

// Market clock
MarketClock clock = market.getMarketClock();
System.out.println("Market status: " + clock.state());
```

### Option Chains

```java
// List available expiration dates for a symbol
List<OptionExpiration> expirations = market.getOptionExpirations("AAPL");
expirations.forEach(e -> System.out.println(e.date()));
```

### Placing Orders

```java
TradingClient trading = new TradingClient(client);

// Equity limit order
OrderRequest req = OrderRequest.equityOrder(
        "AAPL", OrderSide.BUY, 100, OrderType.LIMIT, OrderDuration.DAY)
    .price(150.00)
    .build();
OrderResponse response = trading.placeOrder("12345678", req);
System.out.println("Order ID: " + response.id());

// Option order
OrderRequest optReq = OrderRequest.optionOrder(
        "AAPL", "AAPL240119C00150000", OrderSide.BUY_TO_OPEN,
        1, OrderType.LIMIT, OrderDuration.DAY)
    .price(3.50)
    .build();
trading.placeOrder("12345678", optReq);

// Inspect an order
Order order = trading.getOrder("12345678", response.id());
System.out.println("Status: " + order.getStatus());

// Modify an open order
trading.modifyOrder("12345678", response.id(), null, null, 148.00, null);

// Cancel an open order
trading.cancelOrder("12345678", response.id());
```

### Account Information

```java
AccountsClient accounts = new AccountsClient(client);

// List all accounts linked to the token
List<Account> accts = accounts.getAccounts();

// Account balance
AccountBalance balance = accounts.getBalance("12345678");
System.out.println("Total equity:  " + balance.totalEquity());
System.out.println("Total cash:    " + balance.totalCash());
System.out.println("Open P&L:      " + balance.openPl());
if (balance.margin() != null) {
    System.out.println("Stock BP:      " + balance.margin().stockBuyingPower());
}

// Open positions
List<Position> positions = accounts.getPositions("12345678");
positions.forEach(p -> System.out.printf("%s: %.0f shares @ %.2f cost%n",
    p.symbol(), p.quantity(), p.costBasis()));

// Closed-position gain/loss history
List<GainLoss> gl = accounts.getGainLoss("12345678");
gl.forEach(g -> System.out.printf("%s: P&L=%.2f (%.2f%%)%n",
    g.symbol(), g.gainLoss(), g.gainLossPercent()));
```

---

## API Reference

### MarketDataClient

| Method | Endpoint | Description |
|---|---|---|
| `getQuotes(List<String> symbols)` | `GET /markets/quotes` | Real-time quotes for one or more symbols |
| `getHistory(symbol, interval, start, end)` | `GET /markets/history` | Historical OHLCV data for a date range |
| `getOptionExpirations(String symbol)` | `GET /markets/options/expirations` | Available option expiry dates for a symbol |
| `getMarketClock()` | `GET /markets/clock` | Current market state, next state, and timestamps |

### TradingClient

| Method | Endpoint | Description |
|---|---|---|
| `placeOrder(accountId, OrderRequest)` | `POST /accounts/{id}/orders` | Place an equity or option order |
| `getOrders(String accountId)` | `GET /accounts/{id}/orders` | List all orders for an account |
| `getOrder(accountId, orderId)` | `GET /accounts/{id}/orders/{orderId}` | Retrieve a specific order by ID |
| `cancelOrder(accountId, orderId)` | `DELETE /accounts/{id}/orders/{orderId}` | Cancel an open order |
| `modifyOrder(accountId, orderId, type, duration, price, stop)` | `PUT /accounts/{id}/orders/{orderId}` | Modify price, stop, type, or duration of an open order |

### AccountsClient

| Method | Endpoint | Description |
|---|---|---|
| `getAccounts()` | `GET /user/accounts` | List all accounts linked to the API token |
| `getBalance(String accountId)` | `GET /accounts/{id}/balances` | Full balance record including margin details |
| `getPositions(String accountId)` | `GET /accounts/{id}/positions` | Open positions (symbol, quantity, cost basis) |
| `getOrders(String accountId)` | `GET /accounts/{id}/orders` | Order list (same endpoint as `TradingClient.getOrders`) |
| `getGainLoss(String accountId)` | `GET /accounts/{id}/gainloss` | Closed-position gain/loss history |

---

## Order Types

| Value | Wire value | Description |
|---|---|---|
| `MARKET` | `market` | Execute immediately at the best available price |
| `LIMIT` | `limit` | Execute only at the specified price or better; `price` required |
| `STOP` | `stop` | Triggers a market order when the stop price is reached; `stop` required |
| `STOP_LIMIT` | `stop_limit` | Triggers a limit order when the stop price is reached; both `price` and `stop` required |
| `DEBIT` | `debit` | Multi-leg spread entered for a net debit; `price` required |
| `CREDIT` | `credit` | Multi-leg spread entered for a net credit; `price` required |
| `EVEN` | `even` | Multi-leg spread entered at zero net cost |

---

## Order Sides

| Value | Wire value | Applicable to |
|---|---|---|
| `BUY` | `buy` | Equity — buy to establish a long position |
| `SELL` | `sell` | Equity — sell an existing long position |
| `BUY_TO_COVER` | `buy_to_cover` | Equity — buy to close a short position |
| `SELL_SHORT` | `sell_short` | Equity — sell short to establish a short position |
| `BUY_TO_OPEN` | `buy_to_open` | Options — open a long option position |
| `BUY_TO_CLOSE` | `buy_to_close` | Options — close a short option position |
| `SELL_TO_OPEN` | `sell_to_open` | Options — open a short option position |
| `SELL_TO_CLOSE` | `sell_to_close` | Options — close a long option position |

---

## Order Duration

| Value | Wire value | Description |
|---|---|---|
| `DAY` | `day` | Cancel any unfilled quantity at end of the regular session |
| `GTC` | `gtc` | Good-till-cancelled — order persists across sessions until filled or cancelled |
| `PRE` | `pre` | Pre-market session only |
| `POST` | `post` | Post-market session only |

---

## Error Handling

All client methods declare `throws TradingBrokerTestError`. The sealed hierarchy lets you handle every case exhaustively with a `switch` expression or with individual `catch` blocks:

```java
try {
    List<Quote> quotes = market.getQuotes(List.of("AAPL"));
} catch (TradingBrokerTestError.AuthError e) {
    System.err.println("Check your API key: " + e.getMessage());
} catch (TradingBrokerTestError.RateLimitError e) {
    System.err.println("Rate limited — back off and retry");
} catch (TradingBrokerTestError.ApiError e) {
    System.err.printf("HTTP %d: %s%n", e.statusCode(), e.getMessage());
} catch (TradingBrokerTestError.NetworkError e) {
    System.err.println("Network problem: " + e.getMessage());
} catch (TradingBrokerTestError.ParseError e) {
    System.err.println("Unexpected response shape: " + e.getMessage());
}
```

Or using a pattern `switch` (Java 21+):

```java
try {
    // ...
} catch (TradingBrokerTestError e) {
    switch (e) {
        case TradingBrokerTestError.ApiError     ae -> System.err.println("HTTP " + ae.statusCode());
        case TradingBrokerTestError.AuthError    ae -> System.err.println("Auth failure");
        case TradingBrokerTestError.RateLimitError r -> System.err.println("Rate limited");
        case TradingBrokerTestError.NetworkError ne -> System.err.println("Network: " + ne.getMessage());
        case TradingBrokerTestError.ParseError   pe -> System.err.println("Parse: " + pe.getMessage());
    }
}
```

### TradingBrokerTestError Hierarchy

| Subclass | When thrown | Key members |
|---|---|---|
| `TradingBrokerTestError.ApiError` | Non-2xx HTTP response from the API | `statusCode()` — the HTTP status code |
| `TradingBrokerTestError.AuthError` | HTTP 401 or 403 — invalid or expired token | `getMessage()` |
| `TradingBrokerTestError.RateLimitError` | HTTP 429 — too many requests | `getMessage()` |
| `TradingBrokerTestError.NetworkError` | `IOException` — timeout, connection refused, DNS failure | `getMessage()`, `getCause()` |
| `TradingBrokerTestError.ParseError` | Jackson cannot deserialize the response body | `getMessage()`, `getCause()` |

---

## Configuration Reference

`TradingBrokerTestConfig` is a Java record. All fields are set via the `Builder`.

| Field | Type | Default | Description |
|---|---|---|---|
| `apiKey` | `String` | — (required) | TradingBrokerTest access token; must not be blank |
| `baseUrl` | `String` | `https://api.tradingbrokertest.com/v1` | Live production endpoint |
| `sandboxUrl` | `String` | `https://sandbox.tradingbrokertest.com/v1` | Paper-trading sandbox endpoint |
| `environment` | `Environment` | `PRODUCTION` | `PRODUCTION` or `SANDBOX`; controls which URL is used |
| `timeoutMs` | `long` | `30000` | OkHttp connect and read timeout in milliseconds |

The `effectiveBaseUrl()` method returns `sandboxUrl` when `environment == SANDBOX`, and `baseUrl` otherwise.

**Environment-variable bootstrap:**

| Variable | Maps to | Default |
|---|---|---|
| `TRADINGBROKERTEST_ACCESS_TOKEN` | `apiKey` | — |
| `TRADINGBROKERTEST_REST_BASE_URL` | `baseUrl` | `https://api.tradingbrokertest.com/v1` |
| `TRADINGBROKERTEST_REST_TIMEOUT_MS` | `timeoutMs` | `30000` |

---

## Dependencies

| Artifact | Version | Scope |
|---|---|---|
| `com.squareup.okhttp3:okhttp` | 4.12.0 | compile |
| `com.fasterxml.jackson.core:jackson-databind` | 2.17.1 | compile |
| `com.fasterxml.jackson.core:jackson-annotations` | 2.17.1 | compile |
| `com.fasterxml.jackson.datatype:jackson-datatype-jsr310` | 2.17.1 | compile |
| `org.slf4j:slf4j-api` | 2.0.13 | compile |
| `ch.qos.logback:logback-classic` | 1.5.6 | compile |
| `org.junit.jupiter:junit-jupiter` | 5.10.3 | test |

---
