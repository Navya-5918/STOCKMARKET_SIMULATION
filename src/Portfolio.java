import java.util.*;

public class Portfolio {
    public Map<String, Integer> holdings = new HashMap<>();
    public Map<String, Double> buyPrices = new HashMap<>();
    private List<String> history = new ArrayList<>();
    private double balance;

    public Portfolio(double initialBalance) {
        this.balance = initialBalance;
    }

    // Buy stocks
    public boolean buyStock(Stock stock, int quantity) {
        double cost = stock.getPrice() * quantity;
        if (cost > balance) return false;

        balance -= cost;
        holdings.put(stock.getName(), holdings.getOrDefault(stock.getName(), 0) + quantity);
        buyPrices.put(stock.getName(), stock.getPrice());
        history.add("Bought " + quantity + " of " + stock.getName() + " at $" + stock.getPrice());
        return true;
    }

    // Sell stocks
    public boolean sellStock(Stock stock, int quantity) {
        int owned = holdings.getOrDefault(stock.getName(), 0);
        if (quantity > owned) return false;

        balance += stock.getPrice() * quantity;
        if (quantity == owned) holdings.remove(stock.getName());
        else holdings.put(stock.getName(), owned - quantity);

        history.add("Sold " + quantity + " of " + stock.getName() + " at $" + stock.getPrice());
        return true;
    }

    // Show portfolio with P/L
    public void showPortfolio() {
        System.out.println("\n--- Portfolio ---");
        if (holdings.isEmpty()) System.out.println("No stocks owned.");
        else {
            for (String name : holdings.keySet()) {
                int qty = holdings.get(name);
                double buyPrice = buyPrices.getOrDefault(name, 0.0);
                double currentPrice = buyPrice; // simplified
                double profitLoss = (currentPrice - buyPrice) * qty;
                System.out.printf("%s: %d shares | Buy: $%.2f | Profit/Loss: $%.2f%n",
                        name, qty, buyPrice, profitLoss);
            }
        }
        System.out.println("Balance: $" + balance);
    }

    // Show transaction history
    public void showHistory() {
        System.out.println("\n--- Transaction History ---");
        if (history.isEmpty()) System.out.println("No transactions yet.");
        else history.forEach(System.out::println);
    }

    public double getBalance() {
        return balance;
    }
}

