package com.trading.accounts;

import com.fasterxml.jackson.databind.JsonNode;
import com.trading.accounts.types.*;
import com.trading.client.TradingBrokerTestClient;
import com.trading.error.TradingBrokerTestError;
import com.trading.trading.types.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Client for TradingBrokerTest account-management endpoints.
 */
public class AccountsClient {

    private final TradingBrokerTestClient client;

    public AccountsClient(TradingBrokerTestClient client) {
        this.client = client;
    }

    public List<Account> getAccounts() throws TradingBrokerTestError {
        String json = client.get("/user/accounts");
        try {
            JsonNode root = client.getMapper().readTree(json);
            JsonNode accounts = root.path("accounts").path("account");
            List<Account> result = new ArrayList<>();
            if (accounts.isArray()) {
                for (JsonNode node : accounts) {
                    result.add(client.getMapper().treeToValue(node, Account.class));
                }
            } else if (!accounts.isMissingNode() && !accounts.isNull()) {
                result.add(client.getMapper().treeToValue(accounts, Account.class));
            }
            return result;
        } catch (Exception e) {
            throw new TradingBrokerTestError.ParseError("Failed to parse accounts: " + e.getMessage(), e);
        }
    }

    public AccountBalance getBalance(String accountId) throws TradingBrokerTestError {
        String json = client.get("/accounts/" + accountId + "/balances");
        try {
            JsonNode root = client.getMapper().readTree(json);
            return client.getMapper().treeToValue(root.path("balances"), AccountBalance.class);
        } catch (Exception e) {
            throw new TradingBrokerTestError.ParseError("Failed to parse balance: " + e.getMessage(), e);
        }
    }

    public List<Position> getPositions(String accountId) throws TradingBrokerTestError {
        String json = client.get("/accounts/" + accountId + "/positions");
        try {
            JsonNode root = client.getMapper().readTree(json);
            JsonNode positions = root.path("positions").path("position");
            List<Position> result = new ArrayList<>();
            if (positions.isArray()) {
                for (JsonNode node : positions) {
                    result.add(client.getMapper().treeToValue(node, Position.class));
                }
            } else if (!positions.isMissingNode() && !positions.isNull()) {
                result.add(client.getMapper().treeToValue(positions, Position.class));
            }
            return result;
        } catch (Exception e) {
            throw new TradingBrokerTestError.ParseError("Failed to parse positions: " + e.getMessage(), e);
        }
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

    public List<GainLoss> getGainLoss(String accountId) throws TradingBrokerTestError {
        String json = client.get("/accounts/" + accountId + "/gainloss");
        try {
            JsonNode root = client.getMapper().readTree(json);
            JsonNode positions = root.path("gainloss").path("closed_position");
            List<GainLoss> result = new ArrayList<>();
            if (positions.isArray()) {
                for (JsonNode node : positions) {
                    result.add(client.getMapper().treeToValue(node, GainLoss.class));
                }
            } else if (!positions.isMissingNode() && !positions.isNull()) {
                result.add(client.getMapper().treeToValue(positions, GainLoss.class));
            }
            return result;
        } catch (Exception e) {
            throw new TradingBrokerTestError.ParseError("Failed to parse gain/loss: " + e.getMessage(), e);
        }
    }
}
