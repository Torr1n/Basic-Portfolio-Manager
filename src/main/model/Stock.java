package model;

import java.util.ArrayList;

public class Stock {
    // Represents a Stock having a ticker, amount of shares owned, amount of money invested (in dollars)
    // and list of historical prices (in dollars)
    private static String ticker;
    private static ArrayList<Double> priceHistory;
    private static int shares;
    private static Double invested;

    //REQUIRES: ticker has a non-zero length, price >= 0.0, shares >0
    //EFFECTS: creates a Stock with the given ticker, shares purchased and amount invested
    //         and adds the given current price to an empty list to record the stock's price history
    public Stock(String ticker, Double price, int shares) {
        this.ticker = ticker;
        this.shares = shares;
        this.invested = (shares * price);
        this.priceHistory = new ArrayList<Double>();
        priceHistory.add(price);
    }

    public String getTicker() {
        return ticker;
    }

    public ArrayList<Double> getPriceHistory() {
        return priceHistory;
    }

    public int getShares() {
        return shares;
    }

    public static Double getInvested() {
        return invested;
    }

    //REQUIRES: currentPrice >= 0.0
    //MODIFIES: this
    //EFFECTS: adds the given current stock price to the price history of the stock
    public void updatePrice(Double currentPrice) {
        priceHistory.add(currentPrice);
    }

    //REQUIRES: amount > 0
    //MODIFIES: this
    //EFFECTS: adds the given amount of shares to the current amount of stock shares owned
    public void buyMoreShares(int amount) {
        shares += amount;
        invested += (getMostRecentPrice() * amount);
    }

    //REQUIRES: amount > 0 and amount <= getShares()
    //MODIFIES: this
    //EFFECTS: subtracts the given amount of shares from the current amount of stock shares owned
    public void sellShares(int amount) {
        shares -= amount;
        invested -= (getMostRecentPrice() * amount);
    }

    //EFFECTS: returns the current market value of all the owned shares of the stock
    public Double currentValueOfShares() {
        Double p = getMostRecentPrice();
        return (shares * p);
    }

    //EFFECTS: returns the current profit made on the stock (in dollars)
    public Double profit() {
        return (currentValueOfShares() - invested);
    }

    //EFFECTS: returns the most recent price of the stock
    public Double getMostRecentPrice() {
        int s = priceHistory.size();
        Double p = priceHistory.get(s - 1);
        return p;
    }




}
