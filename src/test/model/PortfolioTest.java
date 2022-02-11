package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PortfolioTest {
    private Stock apple;
    private Stock microsoft;
    private Stock google;
    private Portfolio testPortfolio;

    @BeforeEach
    void runBefore() {
        this.apple = new Stock("AAPL", 200.0, 10);
        this.microsoft = new Stock("MSFT", 50.0, 5);
        this.google = new Stock("GOOGL", 100.0, 15);
        this.testPortfolio = new Portfolio();
        testPortfolio.addStock(apple);
    }

    @Test
    void testConstructor() {
        assertEquals(1, testPortfolio.getOwned().size());
        assertEquals(apple, testPortfolio.getOwned().get(0));
    }

    @Test
    void testAddStock() {
        testPortfolio.addStock(microsoft);
        assertEquals(2, testPortfolio.getOwned().size());
        assertEquals(microsoft, testPortfolio.getOwned().get(1));
    }

    @Test
    void testPortfolioValue() {
        assertEquals(2000.0, testPortfolio.portfolioValue());
        testPortfolio.addStock(microsoft);
        assertEquals(2250.0, testPortfolio.portfolioValue());
        testPortfolio.addStock(google);
        assertEquals(3750.0, testPortfolio.portfolioValue());
        microsoft.updatePrice(100.0);
        assertEquals(4000.0, testPortfolio.portfolioValue());
        google.buyMoreShares(5);
        assertEquals(4500.0, testPortfolio.portfolioValue());
    }

    @Test
    void testPortfolioProfit() {
        assertEquals(0, testPortfolio.portfolioProfit());
        addGoogleAndMicrosoft();
        assertEquals(0, testPortfolio.portfolioProfit());
        microsoft.updatePrice(100.0);
        assertEquals(250.0, testPortfolio.portfolioProfit());
        google.updatePrice(110.0);
        assertEquals(400.0, testPortfolio.portfolioProfit());
        apple.updatePrice(190.0);
        assertEquals(300.0, testPortfolio.portfolioProfit());
        microsoft.updatePrice(10.0);
        assertEquals(-150.0, testPortfolio.portfolioProfit());
    }

    @Test
    void testPortfolioInvested() {
        addGoogleAndMicrosoft();
        assertEquals(3750.0, testPortfolio.portfolioInvested());
        apple.updatePrice(1.0);
        assertEquals(3750.0, testPortfolio.portfolioInvested());
        apple.buyMoreShares(5);
        assertEquals(3755.0, testPortfolio.portfolioInvested());
    }

    @Test
    void testGetAllTickers() {
        ArrayList<String> testList = new ArrayList<>();
        testList.add("AAPL");
        assertEquals(testList, testPortfolio.getAllTickers());
        addGoogleAndMicrosoft();
        testList.add("MSFT");
        testList.add("GOOGL");
        assertEquals(testList, testPortfolio.getAllTickers());
    }

    @Test
    void testFindStockFromTicker() {
        assertEquals(apple, testPortfolio.findStockFromTicker("AAPL"));
        addGoogleAndMicrosoft();
        assertEquals(microsoft, testPortfolio.findStockFromTicker("MSFT"));
        assertEquals(google, testPortfolio.findStockFromTicker("GOOGL"));
    }

    @Test
    void testContainsStock() {
        assertTrue(testPortfolio.containsStock("AAPL"));
        assertFalse(testPortfolio.containsStock("GOOGL"));
        addGoogleAndMicrosoft();
        assertTrue(testPortfolio.containsStock("GOOGL"));
        assertTrue(testPortfolio.containsStock("MSFT"));
    }

    @Test
    void testRemoveStock() {
        testPortfolio.removeStock("AAPL");
        assertTrue(testPortfolio.isEmpty());
        addGoogleAndMicrosoft();
        testPortfolio.removeStock("GOOGL");
        assertEquals(1, testPortfolio.howManyOwned());
        assertTrue(testPortfolio.containsStock("MSFT"));
    }

    @Test
    void testRemoveStocksNotOwned() {
        addGoogleAndMicrosoft();
        testPortfolio.findStockFromTicker("MSFT").sellShares(5);
        testPortfolio.removeNotOwnedStocks();
        assertEquals(2, testPortfolio.howManyOwned());
        testPortfolio.findStockFromTicker("GOOGL").sellShares(5);
        testPortfolio.removeNotOwnedStocks();
        assertEquals(2, testPortfolio.howManyOwned());
        testPortfolio.findStockFromTicker("GOOGL").sellShares(10);
        testPortfolio.findStockFromTicker("AAPL").sellShares(10);
        testPortfolio.removeNotOwnedStocks();
        assertTrue(testPortfolio.isEmpty());
    }

    @Test
    void testIsEmpty() {
        assertFalse(testPortfolio.isEmpty());
        testPortfolio.removeStock("AAPL");
        assertTrue(testPortfolio.isEmpty());
    }

    @Test
    void testHowManyOwned() {
        assertEquals(1, testPortfolio.howManyOwned());
        testPortfolio.removeStock("AAPL");
        assertEquals(0, testPortfolio.howManyOwned());
        addGoogleAndMicrosoft();
        assertEquals(2,testPortfolio.howManyOwned());
    }

    private void addGoogleAndMicrosoft() {
        testPortfolio.addStock(microsoft);
        testPortfolio.addStock(google);
    }
}
