package model;

public class Purchase {
    //Represents a history of stock purchases with the number of shares purchased and price purchased at

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
}
