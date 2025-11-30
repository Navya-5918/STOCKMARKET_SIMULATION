import java.util.*;

public class Main {
    private static boolean running = true;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        // Initialize stocks with volatility
        Stock apple = new Stock("Apple", 150, 5);
        Stock google = new Stock("Google", 2800, 3);
        Stock amazon = new Stock("Amazon", 3400, 4);
        Stock[] stocks = {apple, google, amazon};

        Portfolio portfolio = new Portfolio(10000);

        // Start background thread for real-time updates
        Thread marketThread = new Thread(() -> {
            while (running) {
                double eventImpact = 0;
                if (rand.nextInt(100) < 10) { // 10% chance
                    eventImpact = rand.nextDouble() * 10 - 5; // -5% to +5%
                    System.out.println("\n\u001B[35m--- Market Event! Prices affected by "
                            + String.format("%.2f", eventImpact) + "% ---\u001B[0m");
                }

                for (Stock s : stocks) s.updatePrice(eventImpact);

                try {
                    Thread.sleep(3000); // Update every 3 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        marketThread.start();

        // Menu loop
        while (running) {
            System.out.println("\n--- Stock Market Simulator ---");
            System.out.println("1. Show Stocks");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. Show Portfolio");
            System.out.println("5. Show Transaction History");
            System.out.println("6. Show Stock Trends");
            System.out.println("7. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\n--- Available Stocks ---");
                    for (Stock s : stocks) {
                        System.out.printf("%s - $%.2f%n", s.getName(), s.getPrice());
                    }
                    break;

                case 2:
                    System.out.print("Enter stock name to buy: ");
                    String buyName = sc.next();
                    Stock buyStock = findStock(stocks, buyName);
                    if (buyStock != null) {
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt();
                        if (portfolio.buyStock(buyStock, qty))
                            System.out.println("\u001B[32mBought " + qty + " shares of " + buyName + "\u001B[0m");
                        else System.out.println("\u001B[31mInsufficient balance.\u001B[0m");
                    } else System.out.println("\u001B[31mStock not found.\u001B[0m");
                    break;

                case 3:
                    System.out.print("Enter stock name to sell: ");
                    String sellName = sc.next();
                    Stock sellStock = findStock(stocks, sellName);
                    if (sellStock != null) {
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt();
                        if (portfolio.sellStock(sellStock, qty))
                            System.out.println("\u001B[32mSold " + qty + " shares of " + sellName + "\u001B[0m");
                        else System.out.println("\u001B[31mNot enough shares.\u001B[0m");
                    } else System.out.println("\u001B[31mStock not found.\u001B[0m");
                    break;

                case 4:
                    showPortfolioRealTime(portfolio, stocks);
                    break;

                case 5:
                    portfolio.showHistory();
                    break;

                case 6:
                    for (Stock s : stocks) s.showTrend();
                    break;

                case 7:
                    running = false;
                    System.out.println("Exiting Stock Market Simulator...");
                    break;

                default:
                    System.out.println("\u001B[31mInvalid option.\u001B[0m");
            }
        }

        sc.close();
    }

    // Find stock by name
    private static Stock findStock(Stock[] stocks, String name) {
        for (Stock s : stocks)
            if (s.getName().equalsIgnoreCase(name))
                return s;
        return null;
    }

    // Show portfolio with real-time profit/loss using current prices
    private static void showPortfolioRealTime(Portfolio portfolio, Stock[] stocks) {
        System.out.println("\n--- Portfolio ---");
        Map<String, Integer> holdings = portfolio.holdings;
        Map<String, Double> buyPrices = portfolio.buyPrices;

        if (holdings.isEmpty()) {
            System.out.println("No stocks owned.");
        } else {
            for (String name : holdings.keySet()) {
                int qty = holdings.get(name);
                double buyPrice = buyPrices.get(name);
                double currentPrice = 0;
                for (Stock s : stocks) {
                    if (s.getName().equalsIgnoreCase(name)) {
                        currentPrice = s.getPrice();
                        break;
                    }
                }
                double profitLoss = (currentPrice - buyPrice) * qty;
                String color = profitLoss >= 0 ? "\u001B[32m" : "\u001B[31m"; // green for profit, red for loss
                System.out.printf("%s%s: %d shares | Buy: $%.2f | Current: $%.2f | P/L: $%.2f\u001B[0m%n",
                        color, name, qty, buyPrice, currentPrice, profitLoss);
            }
        }
        System.out.println("Balance: $" + portfolio.getBalance());
    }
}
