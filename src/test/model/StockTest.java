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
        assertEquals(10,testStock.getSharesOwned());
        assertEquals(100.0,testStock.getPriceHistory().get(0));
        assertEquals("TEST",testStock.getTicker());
        assertEquals(1000.0, testStock.getPurchaseHistory());
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
        testStock.buyMoreShares(10);
        stockHasExpectedSharesAndInvested(20,2000.0);
        testStock.updatePrice(5.0);
        testStock.buyMoreShares(5);
        stockHasExpectedSharesAndInvested(25,2025.00);
    }

    @Test
    void testSellShares() {
        testStock.sellShares(2);
        stockHasExpectedSharesAndInvested(8,2000.0);

    }

    private void stockHasExpectedSharesAndInvested(int expectedShares, Double expectedInvested) {
        assertEquals(expectedShares, testStock.getSharesOwned());
        assertEquals(expectedInvested, testStock.getPurchaseHistory());
    }
}