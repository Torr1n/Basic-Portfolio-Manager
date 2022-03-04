package persistance;

import model.Purchase;
import model.Stock;
import model.Portfolio;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Code based on code found in demo project: JsonSerializationDemo
class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Portfolio p = new Portfolio();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyPortfolio() {
        try {
            Portfolio p = new Portfolio();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyPortfolio.json");
            writer.open();
            writer.write(p);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyPortfolio.json");
            p = reader.read();
            assertEquals(0, p.howManyOwned());
            assertTrue(p.isEmpty());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
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
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralPortfolio.json");
            writer.open();
            writer.write(p);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralPortfolio.json");
            p = reader.read();
            List<Stock> owned = p.getOwned();
            assertEquals(2, owned.size());
            super.checkStock("test0", s0PriceHistory, s0PurchaseHistory, owned.get(0));
            super.checkStock("test1", s1PriceHistory, s1PurchaseHistory, owned.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}