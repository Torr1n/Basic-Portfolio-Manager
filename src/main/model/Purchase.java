package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;

//Represents a history of stock purchases with the number of shares purchased and price purchased at
public class Purchase implements Writable {

    private final int numShares;
    private final Double pricePurchased;

    public Purchase(int numShares, Double pricePurchased) {
        this.numShares = numShares;
        this.pricePurchased = pricePurchased;
    }

    public Double getPricePurchased() {
        return pricePurchased;
    }

    public int getNumShares() {
        return numShares;
    }

    //EFFECTS: returns the total amount (in dollars) purchased in this purchase
    public Double totalInvested() {
        return (numShares * pricePurchased);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Purchase purchase = (Purchase) o;
        return numShares == purchase.numShares && pricePurchased.equals(purchase.pricePurchased);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numShares, pricePurchased);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Number of Shares", numShares);
        json.put("Purchase Price", pricePurchased);
        return json;
    }
}
