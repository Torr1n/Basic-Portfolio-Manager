package persistance;

import model.Purchase;
import model.Stock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Code based on code found in demo project: JsonSerializationDemo
public class JsonTest {
    protected void checkStock(String ticker, ArrayList<Double> prices, ArrayList<Purchase> purchases, Stock stock) {
        assertEquals(ticker, stock.getTicker());
        assertEquals(prices.size(), stock.getPriceHistory().size());
        assertEquals(purchases.size(), stock.getPurchaseHistory().size());
    }
}
