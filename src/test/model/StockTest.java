package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {
    private Stock testStock;

    @BeforeEach
    void runBefore() {
        testStock = new Stock("TEST", 100.0, 10);
    }

    @Test
    void testConstructor() {
        assertEquals("TEST",testStock.getTicker());
        assertEquals(10,testStock.getPurchaseHistory().get(0).getNumShares());
        assertEquals(100.0,testStock.getPurchaseHistory().get(0).getPricePurchased());
        assertEquals(100.0,testStock.getPriceHistory().get(0));
    }

    @Test
    void testGetSharesOwned() {
        assertEquals(10, testStock.getSharesOwned());
        testStock.buyMoreShares(15);
        assertEquals(25, testStock.getSharesOwned());
        testStock.sellShares(5);
        assertEquals(20, testStock.getSharesOwned());
    }

    @Test
    void testGetAmountInvested() {
        assertEquals(1000.0, testStock.getAmountInvested());
        testStock.buyMoreShares(15);
        assertEquals(2500.0, testStock.getAmountInvested());
        testStock.updatePrice(50.0);
        testStock.buyMoreShares(10);
        assertEquals(3000.0, testStock.getAmountInvested());
        testStock.sellShares(5);
        assertEquals(2500.0, testStock.getAmountInvested());
    }

    @Test
    void testUpdatePrice() {
        testStock.updatePrice(111.1);
        assertEquals(2, testStock.getPriceHistory().size());
        assertEquals(111.1, testStock.getPriceHistory().get(1));
        assertEquals(100.0, testStock.getPriceHistory().get(0));
        testStock.updatePrice((0.0));
        assertEquals(3,testStock.getPriceHistory().size());
        assertEquals(0.0, testStock.getPriceHistory().get(2));
    }

    @Test
    void testBuyMoreShares() {
        testStock.buyMoreShares(20);
        Purchase p = new Purchase(20, 100.0);
        isSizeAndPlacementOfPurchaseCorrect(2,p,1);
        testStock.updatePrice(200.0);
        testStock.buyMoreShares(10);
        p = new Purchase(10, 200.0);
        isSizeAndPlacementOfPurchaseCorrect(3,p,2);
    }

    @Test
    void testSellShares() {
        Purchase p0 = updatePriceBuySharesAndMakePurchase(200.0, 20);
        Purchase p1 = updatePriceBuySharesAndMakePurchase(150.0, 15);
        testStock.sellShares(10);
        isSizeAndPlacementOfPurchaseCorrect(2,p0,0);
        isSizeAndPlacementOfPurchaseCorrect(2,p1,1);
        testStock.sellShares(15);
        Purchase p2 = new Purchase(5,200.0);
        isSizeAndPlacementOfPurchaseCorrect(2,p2,0);
        isSizeAndPlacementOfPurchaseCorrect(2,p1,1);
        testStock.sellShares(10);
        Purchase p3 = new Purchase(10,150.0);
        isSizeAndPlacementOfPurchaseCorrect(1,p3,0);
    }

    @Test
    void testCurrentValueOfShares() {
        assertEquals(1000.0, testStock.currentValueOfShares());
        testStock.updatePrice(150.0);
        assertEquals(1500.0, testStock.currentValueOfShares());
        testStock.buyMoreShares(20);
        assertEquals(4500.0, testStock.currentValueOfShares());
    }

    @Test
    void testProfit() {
        testStock.updatePrice(150.0);
        assertEquals(500.0, testStock.profit());
        testStock.updatePrice(50.0);
        assertEquals(-500, testStock.profit());
    }

    @Test
    void testGetMostRecentPrice() {
        assertEquals(100.0, testStock.getMostRecentPrice());
        testStock.updatePrice(150.0);
        assertEquals(150, testStock.getMostRecentPrice());
    }

    //EFFECTS: changes the price of the stock and buys shares, then outputs a new purchase with those fields
    private Purchase updatePriceBuySharesAndMakePurchase(Double updatedPrice, int sharesBought) {
        testStock.updatePrice(updatedPrice);
        testStock.buyMoreShares(sharesBought);
        return new Purchase(sharesBought, updatedPrice);
    }

    //EFFECTS: determines if the purchase history has given size, and the given purchase is at the correct placement
    private void isSizeAndPlacementOfPurchaseCorrect(int size, Purchase purchase, int placementInListOfPurchase) {
        assertEquals(size, testStock.getPurchaseHistory().size());
        assertEquals(purchase.getPricePurchased(),
                testStock.getPurchaseHistory().get(placementInListOfPurchase).getPricePurchased());
        assertEquals(purchase.getNumShares(),
                testStock.getPurchaseHistory().get(placementInListOfPurchase).getNumShares());
    }
}