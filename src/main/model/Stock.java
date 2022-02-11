package model;

import java.util.ArrayList;

public class Stock {
    // Represents a Stock having a ticker, amount of shares owned, purchase history of the stock
    // and list of historical prices (in dollars)
    private final String ticker;
    private final ArrayList<Double> priceHistory;
    private final ArrayList<Purchase> purchaseHistory;

    //REQUIRES: ticker has a non-zero length, price >= 0.0, shares >=0
    //EFFECTS: creates a Stock with the given ticker, a list of purchases with the initial transaction in it
    //         and adds the given current price to an empty list to record the stock's price history
    public Stock(String ticker, Double price, int shares) {
        this.ticker = ticker;
        this.purchaseHistory = new ArrayList<>();
        purchaseHistory.add(new Purchase(shares, price));
        this.priceHistory = new ArrayList<>();
        priceHistory.add(price);
    }

    public String getTicker() {
        return ticker;
    }

    public ArrayList<Double> getPriceHistory() {
        return priceHistory;
    }

    public ArrayList<Purchase> getPurchaseHistory() {
        return purchaseHistory;
    }

    //EFFECTS: sums the shares bought at each price in the purchase history
    public Integer getSharesOwned() {
        int sharesOwned = 0;
        for (Purchase p : purchaseHistory) {
            sharesOwned += p.getNumShares();
        }
        return sharesOwned;
    }

    //EFFECTS: sums the value of all shares bought at each price in the purchase history
    public Double getAmountInvested() {
        Double amountInvested = 0.0;
        for (Purchase p : purchaseHistory) {
            amountInvested += p.totalInvested();
        }
        return amountInvested;
    }

    //REQUIRES: currentPrice >= 0.0
    //MODIFIES: this
    //EFFECTS: adds the given current stock price to the price history of the stock
    public void updatePrice(Double currentPrice) {
        priceHistory.add(currentPrice);
    }

    //REQUIRES: amount > 0
    //MODIFIES: this
    //EFFECTS: creates a new purchase with the given amount and current price. Then adds it to the purchase history
    public void buyMoreShares(int amount) {
        purchaseHistory.add(new Purchase(amount, getMostRecentPrice()));
    }

    //REQUIRES: amount > 0 and amount <= getSharesOwned()
    //MODIFIES: this
    //EFFECTS: sells the given amount of stock by removing shares from purchaseHistory,
    //         while prioritizing older purchased shares
    public void sellShares(int amount) {
        if (amount > purchaseHistory.get(0).getNumShares()) {
            amount -= purchaseHistory.get(0).getNumShares();
            purchaseHistory.remove(0);
            sellShares(amount);
        } else {
            if (amount == purchaseHistory.get(0).getNumShares()) {
                purchaseHistory.remove(0);
            } else {
                Purchase sold = new Purchase((purchaseHistory.get(0).getNumShares() - amount),
                        purchaseHistory.get(0).getPricePurchased());
                purchaseHistory.set(0, sold);
            }
        }
    }

    //EFFECTS: returns the current market value of all the owned shares of the stock
    public Double currentValueOfShares() {
        return (getSharesOwned() * getMostRecentPrice());
    }

    //EFFECTS: returns the current profit made on the stock (in dollars)
    public Double profit() {
        return (currentValueOfShares() - getAmountInvested());
    }

    //EFFECTS: returns the most recent price of the stock
    public Double getMostRecentPrice() {
        return priceHistory.get(priceHistory.size() - 1);
    }
}
