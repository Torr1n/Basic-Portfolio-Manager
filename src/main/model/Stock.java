package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a Stock having a ticker, amount of shares owned, purchase history of the stock
// and list of historical prices (in dollars)
public class Stock implements Writable {

    private final String ticker;
    private ArrayList<Double> priceHistory;
    private ArrayList<Purchase> purchaseHistory;

    //REQUIRES: ticker has a non-zero length, price >= 0.0, shares >=0
    //EFFECTS: creates a Stock with the given ticker, a list of purchases with the initial transaction in it
    //         and adds the given current price to an empty list to record the stock's price history
    public Stock(String ticker, Double price, int shares) {
        this.ticker = ticker;
        this.purchaseHistory = new ArrayList<>();
        purchaseHistory.add(new Purchase(shares, price));
        this.priceHistory = new ArrayList<>();
        priceHistory.add(price);
        EventLog.getInstance().logEvent(new Event("Added a new stock: " + ticker
                + " with " + shares + " shares and at a price of $" + price + " per share"));
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
        EventLog.getInstance().logEvent(new Event("Updated " + ticker + "'s price to " + currentPrice));
    }

    //REQUIRES: amount > 0
    //MODIFIES: this
    //EFFECTS: creates a new purchase with the given amount and current price. Then adds it to the purchase history
    public void buyMoreShares(int amount) {
        purchaseHistory.add(new Purchase(amount, getMostRecentPrice()));
        EventLog.getInstance().logEvent(new Event("Bought " + amount + " shares of " + ticker));
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
        EventLog.getInstance().logEvent(new Event("Sold " + amount + " shares of " + ticker));
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

    //EFFECTS: changes the priceHistory to the given arraylist
    public void setPriceHistory(ArrayList<Double> a) {
        this.priceHistory = a;
    }

    //EFFECTS: changes the purchaseHistory to the given arraylist
    public void setPurchaseHistory(ArrayList<Purchase> a) {
        this.purchaseHistory = a;
    }

    //EFFECTS: turns the given stock into a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("ticker", ticker);
        json.put("Price History", priceHistoryToJson());
        json.put("Purchase History", purchaseHistoryToJson());
        return json;
    }

    // EFFECTS: returns historical prices of this stock as a JSON array
    private JSONArray priceHistoryToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Double d : priceHistory) {
            JSONObject json = new JSONObject();
            json.put("price", d);
            jsonArray.put(json);
        }

        return jsonArray;
    }

    // EFFECTS: returns past purchases of this stock as a JSON array
    private JSONArray purchaseHistoryToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Purchase p : purchaseHistory) {
            jsonArray.put(p.toJson());
        }

        return jsonArray;
    }
}
