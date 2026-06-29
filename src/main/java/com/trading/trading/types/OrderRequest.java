package com.trading.trading.types;

/**
 * Builder for placing orders via the TradingBrokerTest API.
 */
public class OrderRequest {

    private final String accountId;
    private final OrderClass clazz;
    private final String symbol;
    private final OrderSide side;
    private final double qty;
    private final OrderType type;
    private final OrderDuration duration;
    private final Double price;
    private final Double stop;
    private final String optionSymbol;

    private OrderRequest(Builder b) {
        this.accountId    = b.accountId;
        this.clazz        = b.clazz;
        this.symbol       = b.symbol;
        this.side         = b.side;
        this.qty          = b.qty;
        this.type         = b.type;
        this.duration     = b.duration;
        this.price        = b.price;
        this.stop         = b.stop;
        this.optionSymbol = b.optionSymbol;
    }

    // --- Static factories ---

    public static Builder equityOrder(String symbol, OrderSide side, double qty,
                                      OrderType type, OrderDuration duration) {
        return new Builder()
                .clazz(OrderClass.EQUITY)
                .symbol(symbol)
                .side(side)
                .qty(qty)
                .type(type)
                .duration(duration);
    }

    public static Builder optionOrder(String symbol, String optionSymbol, OrderSide side,
                                      double qty, OrderType type, OrderDuration duration) {
        return new Builder()
                .clazz(OrderClass.OPTION)
                .symbol(symbol)
                .optionSymbol(optionSymbol)
                .side(side)
                .qty(qty)
                .type(type)
                .duration(duration);
    }

    // --- Getters ---

    public String getAccountId()       { return accountId; }
    public OrderClass getClazz()       { return clazz; }
    public String getSymbol()          { return symbol; }
    public OrderSide getSide()         { return side; }
    public double getQty()             { return qty; }
    public OrderType getType()         { return type; }
    public OrderDuration getDuration() { return duration; }
    public Double getPrice()           { return price; }
    public Double getStop()            { return stop; }
    public String getOptionSymbol()    { return optionSymbol; }

    // --- Builder ---

    public static final class Builder {
        private String accountId;
        private OrderClass clazz = OrderClass.EQUITY;
        private String symbol;
        private OrderSide side;
        private double qty;
        private OrderType type = OrderType.MARKET;
        private OrderDuration duration = OrderDuration.DAY;
        private Double price;
        private Double stop;
        private String optionSymbol;

        public Builder accountId(String accountId)       { this.accountId = accountId;       return this; }
        public Builder clazz(OrderClass clazz)           { this.clazz = clazz;               return this; }
        public Builder symbol(String symbol)             { this.symbol = symbol;             return this; }
        public Builder optionSymbol(String optionSymbol) { this.optionSymbol = optionSymbol; return this; }
        public Builder side(OrderSide side)              { this.side = side;                 return this; }
        public Builder qty(double qty)                   { this.qty = qty;                   return this; }
        public Builder type(OrderType type)              { this.type = type;                 return this; }
        public Builder duration(OrderDuration duration)  { this.duration = duration;         return this; }
        public Builder price(Double price)               { this.price = price;               return this; }
        public Builder stop(Double stop)                 { this.stop = stop;                 return this; }

        public OrderRequest build() {
            if (symbol == null || symbol.isBlank()) {
                throw new IllegalStateException("symbol is required");
            }
            if (side == null) {
                throw new IllegalStateException("side is required");
            }
            if (qty <= 0) {
                throw new IllegalStateException("qty must be positive");
            }
            if ((type == OrderType.LIMIT || type == OrderType.STOP_LIMIT) && price == null) {
                throw new IllegalStateException("price required for LIMIT / STOP_LIMIT orders");
            }
            if ((type == OrderType.STOP || type == OrderType.STOP_LIMIT) && stop == null) {
                throw new IllegalStateException("stop required for STOP / STOP_LIMIT orders");
            }
            return new OrderRequest(this);
        }
    }
}
