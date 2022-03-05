package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Objects;

//Represents a list of different shares
public class Portfolio implements Writable {

    private ArrayList<Stock> owned;

    //REQUIRES: the given stock is not already in the list of owned stocks
    //EFFECTS: creates a new portfolio with the given stock in it
    public Portfolio() {
        this.owned = new ArrayList<>();
    }

    public ArrayList<Stock> getOwned() {
        return owned;
    }

    //MODIFIES: this
    //EFFECTS: adds the given stock to the list of stocks owned
    public void addStock(Stock stock) {
        owned.add(stock);
    }

    //EFFECTS: sums the value of all shares owned of all stocks
    public Double portfolioValue() {
        Double v = 0.0;
        for (Stock s : owned) {
            v += s.currentValueOfShares();
        }
        return v;
    }

    //EFFECTS: sums the profit of all shares owned of all stocks
    public Double portfolioProfit() {
        Double v = 0.0;
        for (Stock s : owned) {
            v += s.profit();
        }
        return v;
    }

    //EFFECTS: sums the initial amount invested in all stocks
    public Double portfolioInvested() {
        Double v = 0.0;
        for (Stock s : owned) {
            v += s.getAmountInvested();
        }
        return v;
    }

    //REQUIRES: the list of stocks owned is not empty
    //EFFECTS: returns a list of the tickers of all the stocks invested in
    public ArrayList<String> getAllTickers() {
        ArrayList<String> t = new ArrayList<>();
        for (Stock s : owned) {
            t.add(s.getTicker());
        }
        return t;
    }

    //REQUIRES: the ticker you are searching for is some stock in your portfolio
    //EFFECTS: when given a ticker, returns the stock in your portfolio that has that ticker
    public Stock findStockFromTicker(String ticker) {
        Stock found = null;
        for (Stock s : owned) {
            if (Objects.equals(s.getTicker(), ticker)) {
                found = s;
                break;
            }
        }
        return found;
    }

    //EFFECTS: returns true if the list of owned stocks contains the given ticker
    public boolean containsStock(String ticker) {
        boolean found = false;
        for (Stock s : owned) {
            if (Objects.equals(s.getTicker(), ticker)) {
                found = true;
                break;
            }
        }
        return found;
    }

    //REQUIRES: the stock ticker you are searching for is some stock in your portfolio
    //MODIFIES: this
    //EFFECTS: when given a stock's ticker, removes that stock from your portfolio
    public void removeStock(String ticker) {
        for (Stock s : owned) {
            if (Objects.equals(s.getTicker(), ticker)) {
                owned.remove(s);
                break;
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: removes all stocks with 0 owned shares
    public void removeNotOwnedStocks() {
        ArrayList<Stock> toRemove = new ArrayList<>();
        for (Stock s : owned) {
            if (s.getSharesOwned() == 0) {
                toRemove.add(s);
            }
        }
        for (Stock s : toRemove) {
            owned.remove(s);
        }
    }

    //EFFECTS: return true if the list of stocks owned is empty
    public boolean isEmpty() {
        return owned.isEmpty();
    }

    //EFFECTS: returns the amount of stocks in owned
    public int howManyOwned() {
        return owned.size();
    }

    //EFFECTS: turns the given portfolio into a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("owned", ownedToJson());
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray ownedToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Stock s : owned) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }
}

