
public class FIXOrderEvent extends TradeEvent {
    private String clOrdId;

    public FIXOrderEvent(String senderId, long timestamp,
                         double basePrice, String clOrdId) {
        super(senderId, timestamp, basePrice);
        this.clOrdId = clOrdId;
    }

    @Override
    public void dispatch() {
        double fee = basePrice * 0.0015;
        double total = basePrice + fee;

        System.out.println("FIX Stock Purchase Request");
        System.out.println("Order ID: " + clOrdId);
        System.out.println("Sender ID: " + senderId);
        System.out.printf("Stock Price: %.2f%n", basePrice);
        System.out.printf("Transaction Fee: %.2f%n", fee);
        System.out.printf("Total Amount: %.2f%n", total);
    }
}
