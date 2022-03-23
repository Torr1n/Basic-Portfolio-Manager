package ui;

import model.Portfolio;
import model.Stock;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Some code based on code found in demo project: JsonSerializationDemo
//Portfolio Manager Application
public class OldUI {
    private static final String JSON_STORE = "./data/Portfolio.json";
    private Portfolio myPortfolio;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    //EFFECTS: runs the portfolio manager application
    public OldUI() throws FileNotFoundException {
        runPortfolioManager();
    }

    //MODIFIES: this
    //EFFECTS: processes user input
    private void runPortfolioManager() {
        boolean keepGoing = true;
        String command;

        initialize();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("e")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nApplication Closed");
    }

    //MODIFIES: this
    //EFFECTS: processes user command
    private void processCommand(String command) {
        switch (command) {
            case "b": buyStock();
                break;
            case "s": sellStock();
                break;
            case "o": listOwned();
                break;
            case "v": displayTotalValue();
                break;
            case "p": displayTotalProfit();
                break;
            case "u": updateStock();
                break;
            case "f": savePortfolio();
                break;
            case "l": loadPortfolio();
                break;
        }
    }

    // EFFECTS: saves the workroom to file
    private void savePortfolio() {
        try {
            jsonWriter.open();
            jsonWriter.write(myPortfolio);
            jsonWriter.close();
            System.out.println("Saved your portfolio to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadPortfolio() {
        try {
            myPortfolio = jsonReader.read();
            System.out.println("Loaded the saved portfolio from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    //MODIFIES: this
    //EFFECTS: allows the user to change the price of a stock
    private void updateStock() {
        System.out.print("Enter the Stock's Ticker Symbol\n");
        String ticker = input.next();
        if (myPortfolio.containsStock(ticker)) {
            Stock stock = myPortfolio.findStockFromTicker(ticker);
            System.out.println("What is the new price of this stock?\n");
            Double price = input.nextDouble();
            stock.updatePrice(price);
            System.out.println("Successfully Changed!\n");
        } else {
            System.out.println("You don't own that stock!\n");
        }
    }

    //EFFECTS: displays the total profit made in the portfolio
    private void displayTotalProfit() {
        System.out.println(new StringBuilder()
                .append("The total profit in your portfolio currently is: \n$")
                .append(myPortfolio.portfolioProfit().toString()));
    }

    //EFFECTS: displays the total value of the portfolio and the total initial investment
    private void displayTotalValue() {
        System.out.println(new StringBuilder()
                .append("The total value of you portfolio is currently: \n$")
                .append(myPortfolio.portfolioValue().toString())
                .append("\n Your initial investment was: \n$")
                .append(myPortfolio.portfolioInvested().toString()));
    }

    //EFFECTS: lists the amount of shares the user owns and ticker of each stock in their portfolio
    private void listOwned() {
        myPortfolio.removeNotOwnedStocks();
        System.out.println("You own: ");
        if (myPortfolio.isEmpty()) {
            System.out.println("\n No stocks! Go buy some!");
        } else {
            for (Stock stock : myPortfolio.getOwned()) {
                System.out.println(new StringBuilder()
                        .append("\n")
                        .append(stock.getSharesOwned().toString())
                        .append(" shares of ")
                        .append(stock.getTicker()));
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: allows the user to select a stock, and sell shares of that stock
    private void sellStock() {
        System.out.print("Enter the Stock's Ticker Symbol\n");
        String ticker = input.next();
        if (myPortfolio.containsStock(ticker)) {
            Stock stock = myPortfolio.findStockFromTicker(ticker);
            priceAndOwnedOfTicker(ticker);
            howManySharesWanted(ticker, "sell");
            Integer amount = input.nextInt();
            if (amount > stock.getSharesOwned()) {
                System.out.println(new StringBuilder()
                        .append("you only have ")
                        .append(stock.getSharesOwned().toString())
                        .append(" shares of this stock!"));
            } else {
                stock.sellShares(amount);
                System.out.println(new StringBuilder().append("You sold ")
                        .append(amount).append(" shares of ")
                        .append(ticker).append(" for $")
                        .append(amount * stock.getMostRecentPrice()).append("\n"));
                myPortfolio.removeNotOwnedStocks();
            }
        } else {
            System.out.println("You do not own any of this stock!\n");
        }
    }

    //MODIFIES: this
    //EFFECTS: allows the user to buy a stock and add it to their portfolio
    private void buyStock() {
        System.out.print("Enter the Stock's Ticker Symbol\n");
        String ticker = input.next();

        if (myPortfolio.containsStock(ticker)) {
            Stock stock = myPortfolio.findStockFromTicker(ticker);
            priceAndOwnedOfTicker(ticker);
            howManySharesWanted(ticker, "buy");
            int amount = input.nextInt();
            stock.buyMoreShares(amount);
        } else {
            System.out.println("What is the price of the stock?\n");
            Double price = input.nextDouble();
            howManySharesWanted(ticker, "buy");
            int amount = input.nextInt();
            myPortfolio.addStock(new Stock(ticker, price, amount));
        }
        System.out.println("Transaction Complete\n");
    }

    //EFFECTS: when given a ticker, prints out a message telling the user,
    // the price of that stock and how many shares they own.
    private void priceAndOwnedOfTicker(String ticker) {
        Stock stock = myPortfolio.findStockFromTicker(ticker);
        System.out.println(new StringBuilder()
                .append("The Price of ")
                .append(ticker)
                .append(" is $")
                .append((stock.getMostRecentPrice()).toString())
                .append("\n You own: ")
                .append((stock.getSharesOwned()).toString())
                .append(" shares\n"));
    }

    //EFFECTS: when given a ticker and a string either buy or sell, prints out asking user:
    //         how many shares they want to execute this transaction on.
    private void howManySharesWanted(String ticker, String s) {
        System.out.println(new StringBuilder()
                .append("How many shares of ")
                .append(ticker)
                .append(" would you like to ")
                .append(s)
                .append("?\n"));
    }

    //MODIFIES: this
    //EFFECTS: initializes portfolio
    private void initialize() {
        myPortfolio = new Portfolio();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    //EFFECTS: displays menu of options to the user
    private void displayMenu() {
        System.out.println("\nSelect From:");
        System.out.println("\t b -> buy a stock");
        System.out.println("\t s -> sell a stock");
        System.out.println("\t o -> list all the stocks owned");
        System.out.println("\t v -> view the total value of the portfolio");
        System.out.println("\t p -> produce the total profit in the portfolio");
        System.out.println("\t u -> update a stock's price");
        System.out.println("\t f -> save portfolio to file");
        System.out.println("\t l -> load portfolio from file");
        System.out.println("\t e -> exit");
    }
}
