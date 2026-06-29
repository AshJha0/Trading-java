package com.trading.trading.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a single order returned by the TradingBrokerTest API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    @JsonProperty("id")
    private long id;

    @JsonProperty("type")
    private OrderType type;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("side")
    private OrderSide side;

    @JsonProperty("quantity")
    private double quantity;

    @JsonProperty("status")
    private OrderStatus status;

    @JsonProperty("duration")
    private OrderDuration duration;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("avg_fill_price")
    private double avgFillPrice;

    @JsonProperty("exec_quantity")
    private double execQuantity;

    @JsonProperty("last_fill_price")
    private double lastFillPrice;

    @JsonProperty("last_fill_quantity")
    private double lastFillQuantity;

    @JsonProperty("remain_quantity")
    private double remainQuantity;

    @JsonProperty("create_date")
    private String createDate;

    @JsonProperty("transaction_date")
    private String transactionDate;

    @JsonProperty("class")
    private OrderClass orderClass;

    @JsonProperty("option_symbol")
    private String optionSymbol;

    @JsonProperty("num_legs")
    private Integer numLegs;

    @JsonProperty("strategy")
    private String strategy;

    @JsonProperty("leg")
    private List<OrderLeg> legs;

    // Default constructor for Jackson
    public Order() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public OrderType getType() { return type; }
    public void setType(OrderType type) { this.type = type; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public OrderSide getSide() { return side; }
    public void setSide(OrderSide side) { this.side = side; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public OrderDuration getDuration() { return duration; }
    public void setDuration(OrderDuration duration) { this.duration = duration; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public double getAvgFillPrice() { return avgFillPrice; }
    public void setAvgFillPrice(double avgFillPrice) { this.avgFillPrice = avgFillPrice; }

    public double getExecQuantity() { return execQuantity; }
    public void setExecQuantity(double execQuantity) { this.execQuantity = execQuantity; }

    public double getLastFillPrice() { return lastFillPrice; }
    public void setLastFillPrice(double lastFillPrice) { this.lastFillPrice = lastFillPrice; }

    public double getLastFillQuantity() { return lastFillQuantity; }
    public void setLastFillQuantity(double lastFillQuantity) { this.lastFillQuantity = lastFillQuantity; }

    public double getRemainQuantity() { return remainQuantity; }
    public void setRemainQuantity(double remainQuantity) { this.remainQuantity = remainQuantity; }

    public String getCreateDate() { return createDate; }
    public void setCreateDate(String createDate) { this.createDate = createDate; }

    public String getTransactionDate() { return transactionDate; }
    public void setTransactionDate(String transactionDate) { this.transactionDate = transactionDate; }

    public OrderClass getOrderClass() { return orderClass; }
    public void setOrderClass(OrderClass orderClass) { this.orderClass = orderClass; }

    public String getOptionSymbol() { return optionSymbol; }
    public void setOptionSymbol(String optionSymbol) { this.optionSymbol = optionSymbol; }

    public Integer getNumLegs() { return numLegs; }
    public void setNumLegs(Integer numLegs) { this.numLegs = numLegs; }

    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }

    public List<OrderLeg> getLegs() { return legs; }
    public void setLegs(List<OrderLeg> legs) { this.legs = legs; }
}
