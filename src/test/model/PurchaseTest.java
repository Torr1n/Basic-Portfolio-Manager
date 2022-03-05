package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testEquals() {
        Purchase p0 = new Purchase(101, 10.0);
        Purchase p1 = new Purchase(100, 10.1);
        Purchase p2 = new Purchase(100, 10.0);
        assertTrue(testPurchase.equals(p2));
        assertFalse(testPurchase.equals(p0));
        assertFalse(testPurchase.equals(p1));
    }
}

