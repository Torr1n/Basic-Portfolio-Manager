package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;

//Represents a history of stock purchases with the number of shares purchased and price purchased at
public class Purchase implements Writable {

    private final int numShares;
    private final Double pricePurchased;

    //REQUIRES: numShares >= 0, pricePurchased >= 0
    //EFFECTS: creates a new purchase with the given number of shares and at the given price
    public Purchase(int numShares, Double pricePurchased) {
        this.numShares = numShares;
        this.pricePurchased = pricePurchased;
    }

    //EFFECTS: returns the price the stock was purchased at
    public Double getPricePurchased() {
        return pricePurchased;
    }

    //EFFECTS: returns the number of shares purchased
    public int getNumShares() {
        return numShares;
    }

    //EFFECTS: returns the total amount (in dollars) purchased in this purchase
    public Double totalInvested() {
        return (numShares * pricePurchased);
    }

    //EFFECTS: determines if two purchases are equal.
    @Override
    public boolean equals(Object o) {
        Purchase purchase = (Purchase) o;
        return numShares == purchase.numShares && pricePurchased.equals(purchase.pricePurchased);
    }

    //EFFECTS: turns the given purchase into a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Number of Shares", numShares);
        json.put("Purchase Price", pricePurchased);
        return json;
    }
}
