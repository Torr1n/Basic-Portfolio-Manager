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

    public Double totalInvested() {
        return (numShares * pricePurchased);
    }
}
