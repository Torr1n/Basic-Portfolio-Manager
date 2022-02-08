package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PurchaseTest {
    private Purchase testPurchase;

    @BeforeEach
    void runBefore() {
        testPurchase = new Purchase(100, 10.0);
    }

    @Test
    void testConstructor() {
        assertEquals(100, testPurchase.getNumShares());
        assertEquals(10.0, testPurchase.getPricePurchased());
    }

    @Test
    void testTotalInvested() {
        assertEquals(1000, testPurchase.totalInvested());
    }
}
