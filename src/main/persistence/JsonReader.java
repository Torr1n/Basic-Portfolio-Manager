package persistence;

import model.Portfolio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import model.Purchase;
import model.Stock;
import org.json.*;

// Code based on code found in demo project: JsonSerializationDemo
// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Portfolio from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Portfolio read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePortfolio(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses portfolio from JSON object and returns it
    private Portfolio parsePortfolio(JSONObject jsonObject) {
        Portfolio p = new Portfolio();
        addOwned(p, jsonObject);
        return p;
    }

    // MODIFIES: p
    // EFFECTS: parses owned from JSON object and adds them to portfolio
    private void addOwned(Portfolio p, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("owned");
        for (Object json : jsonArray) {
            JSONObject nextStock = (JSONObject) json;
            addStock(p, nextStock);
        }
    }

    // MODIFIES: p
    // EFFECTS: parses stock from JSON object
    private void addStock(Portfolio p, JSONObject jsonObject) {
        String ticker = jsonObject.getString("ticker");
        Stock stock = new Stock(ticker,0.0,0);
        stock.setPriceHistory(jsonPriceHistory(jsonObject));
        stock.setPurchaseHistory(jsonPurchaseHistory(jsonObject));
        p.addStock(stock);
    }

    //EFFECTS: parses price history from JSON object and returns it as a list of double
    private ArrayList<Double> jsonPriceHistory(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Price History");
        ArrayList<Double> retrievedPrices = new ArrayList<>();
        for (Object json : jsonArray) {
            JSONObject nextPrice = (JSONObject) json;
            Double price = nextPrice.getDouble("price");
            retrievedPrices.add(price);
        }
        return retrievedPrices;
    }

    //EFFECTS: parses purchase history from JSON object and returns it as a list of purchase
    private ArrayList<Purchase> jsonPurchaseHistory(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Purchase History");
        ArrayList<Purchase> retrievedPurchases = new ArrayList<>();
        for (Object json : jsonArray) {
            JSONObject nextPurchase = (JSONObject) json;
            retrievedPurchases.add(jsonPurchase(nextPurchase));
        }
        return retrievedPurchases;
    }

    //EFFECTS: parses purchase from JSON object and returns it
    private Purchase jsonPurchase(JSONObject nextPurchase) {
        int shares = nextPurchase.getInt("Number of Shares");
        Double price = nextPurchase.getDouble("Purchase Price");
        return new Purchase(shares, price);
    }
}