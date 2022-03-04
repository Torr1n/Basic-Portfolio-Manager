package persistance;

import model.Purchase;
import model.Stock;
import model.Portfolio;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Code based on code found in demo project: JsonSerializationDemo
class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Portfolio p = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyPortfolio() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyPortfolio.json");
        try {
            Portfolio p = reader.read();
            assertEquals(0, p.howManyOwned());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralPortfolio() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralPortfolio.json");
        try {
            Portfolio p = new Portfolio();
            Stock s0 = new Stock("test0", 100.0, 10);
            Stock s1 = new Stock("test1", 200.0, 20);
            p.addStock(s0);
            p.addStock(s1);
            s0.buyMoreShares(10);
            s0.updatePrice(150.0);
            s0.buyMoreShares(5);
            s0.updatePrice(200.0);
            s1.buyMoreShares(10);
            s1.updatePrice(250.0);
            ArrayList<Double> s0PriceHistory = s0.getPriceHistory();
            ArrayList<Purchase> s0PurchaseHistory = s0.getPurchaseHistory();
            ArrayList<Double> s1PriceHistory = s1.getPriceHistory();
            ArrayList<Purchase> s1PurchaseHistory = s1.getPurchaseHistory();

            Portfolio readPortfolio = reader.read();
            List<Stock> owned = readPortfolio.getOwned();
            assertEquals(2, owned.size());
            super.checkStock("test0", s0PriceHistory, s0PurchaseHistory, owned.get(0));
            super.checkStock("test1", s1PriceHistory, s1PurchaseHistory, owned.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}