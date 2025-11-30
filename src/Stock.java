import java.util.*;

public class Stock {
    private String name;
    private double price;
    private List<Double> priceHistory = new ArrayList<>();
    private double volatility; // how much price can fluctuate
    private Random random = new Random();

    public Stock(String name, double price, double volatility) {
        this.name = name;
        this.price = price;
        this.volatility = volatility;
        priceHistory.add(price); // initialize history
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public List<Double> getPriceHistory() {
        return priceHistory;
    }

    // Update price dynamically, optionally affected by market event
    public void updatePrice(double eventImpact) {
        double changePercent = (random.nextDouble() * volatility * 2) - volatility + eventImpact; // -volatility to +volatility + event
        price += price * (changePercent / 100);
        price = Math.round(price * 100.0) / 100.0; // round to 2 decimals
        priceHistory.add(price);
    }

    // Display simple ASCII trend graph
    public void showTrend() {
        System.out.print(name + " trend: ");
        for (double p : priceHistory) {
            int bars = (int) (p / 50); // scale for console
            for (int i = 0; i < bars; i++) System.out.print("*");
            System.out.print(" ");
        }
        System.out.println();
    }
}
