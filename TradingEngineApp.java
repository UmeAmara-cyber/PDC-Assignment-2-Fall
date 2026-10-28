
public class TradingEngineApp {
    public static void main(String[] args) {
        TradeEvent event1 = new FIXOrderEvent(
            "FIX-Gateway", System.currentTimeMillis(),
            1000.00, "ORD-101"
        );

        TradeEvent event2 = new ReutersQuoteEvent(
            "Reuters-Gateway", System.currentTimeMillis(),
            1000.00, 998.50, 1001.00
        );

        event1.dispatch();

        System.out.println();

        event2.dispatch();
    }
}
