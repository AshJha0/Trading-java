package com.trading.trading;

import com.fasterxml.jackson.databind.JsonNode;
import com.trading.client.TradingBrokerTestClient;
import com.trading.error.TradingBrokerTestError;
import com.trading.trading.types.*;

import java.util.*;

/**
 * Client for TradingBrokerTest order-management endpoints.
 */
public class TradingClient {

    private final TradingBrokerTestClient client;

    public TradingClient(TradingBrokerTestClient client) {
        this.client = client;
    }

    public OrderResponse placeOrder(String accountId, OrderRequest request) throws TradingBrokerTestError {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("class",    request.getClazz().name().toLowerCase());
        form.put("symbol",   request.getSymbol());
        form.put("side",     sideWire(request.getSide()));
        form.put("quantity", String.valueOf(request.getQty()));
        form.put("type",     typeWire(request.getType()));
        form.put("duration", durationWire(request.getDuration()));
        if (request.getPrice() != null)        form.put("price", String.valueOf(request.getPrice()));
        if (request.getStop() != null)         form.put("stop",  String.valueOf(request.getStop()));
        if (request.getOptionSymbol() != null) form.put("option_symbol", request.getOptionSymbol());

        String json = client.post("/accounts/" + accountId + "/orders", form);
        return parseOrderResponse(json);
    }

    public List<Order> getOrders(String accountId) throws TradingBrokerTestError {
        String json = client.get("/accounts/" + accountId + "/orders");
        try {
            JsonNode root = client.getMapper().readTree(json);
            JsonNode orders = root.path("orders").path("order");
            List<Order> result = new ArrayList<>();
            if (orders.isArray()) {
                for (JsonNode node : orders) {
                    result.add(client.getMapper().treeToValue(node, Order.class));
                }
            } else if (!orders.isMissingNode() && !orders.isNull()) {
                result.add(client.getMapper().treeToValue(orders, Order.class));
            }
            return result;
        } catch (Exception e) {
            throw new TradingBrokerTestError.ParseError("Failed to parse orders: " + e.getMessage(), e);
        }
    }

    public Order getOrder(String accountId, long orderId) throws TradingBrokerTestError {
        String json = client.get("/accounts/" + accountId + "/orders/" + orderId);
        try {
            JsonNode root = client.getMapper().readTree(json);
            return client.getMapper().treeToValue(root.path("order"), Order.class);
        } catch (Exception e) {
            throw new TradingBrokerTestError.ParseError("Failed to parse order: " + e.getMessage(), e);
        }
    }

    public OrderResponse cancelOrder(String accountId, long orderId) throws TradingBrokerTestError {
        String json = client.delete("/accounts/" + accountId + "/orders/" + orderId);
        return parseOrderResponse(json);
    }

    public OrderResponse modifyOrder(String accountId, long orderId, OrderType type,
                                      OrderDuration dur, Double price, Double stop) throws TradingBrokerTestError {
        Map<String, String> form = new LinkedHashMap<>();
        if (type  != null)  form.put("type",     typeWire(type));
        if (dur   != null)  form.put("duration", durationWire(dur));
        if (price != null)  form.put("price",    String.valueOf(price));
        if (stop  != null)  form.put("stop",     String.valueOf(stop));
        String json = client.put("/accounts/" + accountId + "/orders/" + orderId, form);
        return parseOrderResponse(json);
    }

    private OrderResponse parseOrderResponse(String json) throws TradingBrokerTestError {
        try {
            JsonNode root = client.getMapper().readTree(json);
            JsonNode order = root.path("order");
            return client.getMapper().treeToValue(order, OrderResponse.class);
        } catch (Exception e) {
            throw new TradingBrokerTestError.ParseError("Failed to parse order response: " + e.getMessage(), e);
        }
    }

    private String sideWire(OrderSide side) {
        return switch (side) {
            case BUY           -> "buy";
            case SELL          -> "sell";
            case BUY_TO_COVER  -> "buy_to_cover";
            case SELL_SHORT    -> "sell_short";
            case BUY_TO_OPEN   -> "buy_to_open";
            case BUY_TO_CLOSE  -> "buy_to_close";
            case SELL_TO_OPEN  -> "sell_to_open";
            case SELL_TO_CLOSE -> "sell_to_close";
        };
    }

    private String typeWire(OrderType type) {
        return switch (type) {
            case MARKET     -> "market";
            case LIMIT      -> "limit";
            case STOP       -> "stop";
            case STOP_LIMIT -> "stop_limit";
            case DEBIT      -> "debit";
            case CREDIT     -> "credit";
            case EVEN       -> "even";
        };
    }

    private String durationWire(OrderDuration dur) {
        return switch (dur) {
            case DAY  -> "day";
            case GTC  -> "gtc";
            case PRE  -> "pre";
            case POST -> "post";
        };
    }
}
