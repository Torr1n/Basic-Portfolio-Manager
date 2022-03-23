package ui;

import model.Stock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// Some code based on code found in demo project: AlarmSystem
// https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
//Represents user interface for stocks.
public class StockUI extends JInternalFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 100;
    private static final int LOC = 100;
    private Stock stock;
    private JTextField stockInfo;
    private String ticker;
    private PortfolioManagerUI parent;


    //EFFECTS: Constructor sets up user interface for a given stock and parent
    public StockUI(Stock s, PortfolioManagerUI parent) {
        super(s.getTicker(), false, false, false, false);
        stock = s;
        this.parent = parent;
        ticker = s.getTicker();
        stockInfo = new JTextField(infoText(s));
        stockInfo.setEditable(false);
        stockInfo.setAlignmentX(CENTER_ALIGNMENT);

        JPanel buttonPanel = addButtonPanel();
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
        Container cp = getContentPane();
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
        cp.add(stockInfo);
        cp.add(buttonPanel);
        setSize(WIDTH, HEIGHT);
        setPosition(parent);
        setVisible(true);
    }

    public Stock getStock() {
        return stock;
    }

    //EFFECTS: Helper to create a message about the given stock
    private String infoText(Stock s) {
        return "you own " + s.getSharesOwned().toString()
                + " shares of " + ticker + " for " + s.getMostRecentPrice().toString() + " per share";
    }

    //MODIFIES: this
    //EFFECTS: Sets the position of this stock UI relative to parent component
    private void setPosition(Component parent) {
        setLocation(LOC, parent.getHeight() / 2 + LOC / 5);
    }

    //MODIFIES: this
    //EFFECTS: Helper to create and return buttons on the stock
    private JPanel addButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));
        buttonPanel.add(new JButton(new BuySharesAction()));
        buttonPanel.add(new JButton(new SellSharesAction()));
        buttonPanel.add(new JButton(new UpdatePriceAction()));

        return buttonPanel;
    }

    //Represents the action to be taken when buy shares button is pressed
    private class BuySharesAction extends AbstractAction {

        //EFFECTS: creates an abstract action with the name Buy Shares
        BuySharesAction() {
            super("Buy Shares");
        }

        //MODIFIES: this
        //EFFECTS: when button pressed, prompt user to say how many shares they want to buy
        //         then, buys that given number of shares and updates the text
        @Override
        public void actionPerformed(ActionEvent e) {
            String purchaseAmount = JOptionPane.showInputDialog(null,
                    "amount?",
                    "How many shares would you like to buy?",
                    JOptionPane.QUESTION_MESSAGE);
            int amount = 0;
            amount = toInt(purchaseAmount, amount);
            if (amount < 0) {
                JOptionPane.showMessageDialog(null, "Negative number!", "System Error",
                        JOptionPane.ERROR_MESSAGE);
                amount = 0;
            }
            stock.buyMoreShares(amount);
            stockInfo.setText(infoText(stock));
            parent.updateStatus();
        }
    }

    //EFFECTS: turns the given string into an integer or returns a system error if the string isn't an integer
    private int toInt(String purchaseAmount, int amount) {
        try {
            amount = Integer.parseInt(purchaseAmount);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "System Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return amount;
    }

    //Represents the action to be taken when buy shares button is pressed
    private class SellSharesAction extends AbstractAction {

        //EFFECTS: creates an abstract action with the name Sell Shares
        SellSharesAction() {
            super("Sell Shares");
        }

        //MODIFIES: this
        //EFFECTS: when button pressed, prompt user to say how many shares they want to sell
        //         then, sells that given number of shares and updates the text
        @Override
        public void actionPerformed(ActionEvent e) {
            String transactionAmount = JOptionPane.showInputDialog(null,
                    "amount?",
                    "How many shares would you like to sell?",
                    JOptionPane.QUESTION_MESSAGE);
            int amount = 0;
            amount = toInt(transactionAmount, amount);
            if (amount > stock.getSharesOwned()) {
                JOptionPane.showMessageDialog(null, "Not enough shares!", "System Error",
                        JOptionPane.ERROR_MESSAGE);
                amount = 0;
            }
            stock.sellShares(amount);
            stockInfo.setText(infoText(stock));
            if (stock.getSharesOwned() == 0) {
                parent.removeStock(StockUI.this);
            }
            parent.updateStatus();
        }
    }

    //Represents the action to be taken when buy shares button is pressed
    private class UpdatePriceAction extends AbstractAction {

        //EFFECTS: creates an abstract action with the name Buy Shares
        UpdatePriceAction() {
            super("Update Price");
        }

        //MODIFIES: this
        //EFFECTS: when button pressed, prompt user to say the new price
        //         then, sets the stock's price to that and updates the text
        @Override
        public void actionPerformed(ActionEvent e) {
            String newPrice = JOptionPane.showInputDialog(null,
                    "Price?",
                    "What is the new price?",
                    JOptionPane.QUESTION_MESSAGE);
            Double price = stock.getMostRecentPrice();
            try {
                price = Double.parseDouble(newPrice);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            if (price <= 0) {
                JOptionPane.showMessageDialog(null, "Negative number!", "System Error",
                        JOptionPane.ERROR_MESSAGE);
                price = stock.getMostRecentPrice();
            }
            stock.updatePrice(price);
            stockInfo.setText(infoText(stock));
            parent.updateStatus();
        }
    }
}
