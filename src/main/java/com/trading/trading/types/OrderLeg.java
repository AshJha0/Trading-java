package com.trading.trading.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A single leg of a multileg order.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderLeg {

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

    @JsonProperty("avg_fill_price")
    private double avgFillPrice;

    @JsonProperty("exec_quantity")
    private double execQuantity;

    @JsonProperty("last_fill_price")
    private Double lastFillPrice;

    @JsonProperty("last_fill_quantity")
    private Double lastFillQuantity;

    @JsonProperty("remaining_quantity")
    private Double remainingQuantity;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("option_symbol")
    private String optionSymbol;

    @JsonProperty("position_effect")
    private String positionEffect;

    public OrderLeg() {}

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

    public double getAvgFillPrice() { return avgFillPrice; }
    public void setAvgFillPrice(double avgFillPrice) { this.avgFillPrice = avgFillPrice; }

    public double getExecQuantity() { return execQuantity; }
    public void setExecQuantity(double execQuantity) { this.execQuantity = execQuantity; }

    public Double getLastFillPrice() { return lastFillPrice; }
    public void setLastFillPrice(Double lastFillPrice) { this.lastFillPrice = lastFillPrice; }

    public Double getLastFillQuantity() { return lastFillQuantity; }
    public void setLastFillQuantity(Double lastFillQuantity) { this.lastFillQuantity = lastFillQuantity; }

    public Double getRemainingQuantity() { return remainingQuantity; }
    public void setRemainingQuantity(Double remainingQuantity) { this.remainingQuantity = remainingQuantity; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getOptionSymbol() { return optionSymbol; }
    public void setOptionSymbol(String optionSymbol) { this.optionSymbol = optionSymbol; }

    public String getPositionEffect() { return positionEffect; }
    public void setPositionEffect(String positionEffect) { this.positionEffect = positionEffect; }
}
