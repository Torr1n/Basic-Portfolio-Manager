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
        assertEquals(10,testStock.getShares());
        assertEquals(100.0,testStock.getMostRecentPrice());
        assertEquals("TEST",testStock.getTicker());
    }

    @Test
    void testUpdatePrice() {
        testStock.updatePrice(111.1);
        assertEquals(2, testStock.getPriceHistory().size());
        assertEquals(111.1, testStock.getPriceHistory().get(1));
    }
}