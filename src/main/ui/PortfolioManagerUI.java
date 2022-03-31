package ui;

import model.Event;
import model.EventLog;
import model.Portfolio;
import model.Stock;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

// Some code based on code found in demo project: AlarmSystem
// https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
// Represents a Portfolio UI with stocks and a profitability display
public class PortfolioManagerUI extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private Portfolio portfolio;
    private JDesktopPane desktop;
    private JPanel status;
    private JInternalFrame portManager;
    private JInternalFrame profitOrLoss;
    private ArrayList<StockUI> stocks;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/Portfolio.json";

    //EFFECTS: initializes all fields and sets up the desktop
    public PortfolioManagerUI() {
        portfolio = new Portfolio();
        stocks = new ArrayList<>();
        status = new JPanel();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        desktop = new JDesktopPane();
        desktop.addMouseListener(new DesktopFocusAction());
        profitOrLoss = new JInternalFrame("Profitable?",
                false, false, false, true);
        profitOrLoss.setLayout(new BorderLayout());
        portManager = new JInternalFrame("Portfolio Manager",
                false, false, false, false);
        portManager.setLayout(new BorderLayout());

        setContentPane(desktop);
        setTitle("Torrin's Portfolio Manager Application");
        setSize(WIDTH, HEIGHT);
        addButtonPanel();
        addMenu();
        profitOrLoss.add(giveStatus("data/crymoji.png",
                "No Stocks!"));

        addInternalFrame(profitOrLoss);
        addInternalFrame(portManager);

        finishSetUp();
    }

    //MODIFIES: this
    //EFFECTS: finishes the setup of the desktop by setting the close operation,
    //         centring the screen, and making it visible
    private void finishSetUp() {
        addWindowListener(new CustomCloser());
        centreOnScreen();
        setVisible(true);
    }

    //Represents the custom behaviour wanted when closing the application
    private class CustomCloser extends WindowAdapter {

        @Override
        //EFFECTS: provides actions to be taken when the window is closing
        public void windowClosing(WindowEvent e) {
            printLog();
            super.windowClosing(e);
        }

        //EFFECTS: prints the log to the console
        public void printLog() {
            if (EventLog.getInstance() != null) {
                for (Event e : EventLog.getInstance()) {
                    System.out.println(e.toString());
                }
            }
            System.exit(0);
        }
    }


    //MODIFIES: this
    //EFFECTS: packs, makes visible and adds to desktop the given JInternalFrame
    private void addInternalFrame(JInternalFrame frame) {
        frame.pack();
        frame.setVisible(true);
        desktop.add(frame);
    }

    //MODIFIES: this
    //EFFECTS: Helper to add the stock status panel by assembling and returning the JPanel with the given
    //         image and text
    private JPanel giveStatus(String imageLocation, String desiredText) {
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BorderLayout());
        JLabel emoji = new JLabel(
                new ImageIcon(imageLocation));
        statusPanel.add(emoji, BorderLayout.EAST);
        JLabel text = new JLabel(desiredText);
        text.setFont(new Font(Font.SANS_SERIF, Font.BOLD + Font.CENTER_BASELINE, 12));
        statusPanel.add(text, BorderLayout.SOUTH);
        statusPanel.setBounds(new Rectangle(new Point(WIDTH / 2, 10), statusPanel.getPreferredSize()));
        status = statusPanel;
        return statusPanel;
    }

    //EFFECTS; Updates the profit or loss status depending on if the portfolio is profitable,
    //         losing money,
    //         or break-even
    //MODIFIES: this
    public void updateStatus() {
        profitOrLoss.remove(status);
        if (portfolio.portfolioProfit() > 0) {
            profitOrLoss.add(giveStatus(
                    "data/moneymoji.png",
                    portfolio.portfolioProfit().toString() + " in Profit!"));
        } else if (portfolio.portfolioProfit() < 0) {
            profitOrLoss.add(giveStatus(
                    "data/crymoji.png",
                    portfolio.portfolioProfit().toString() + " in Losses!"));
        } else {
            profitOrLoss.add(giveStatus(
                    "data/zzzmoji.png",
                    "no profits!"));
        }
        profitOrLoss.repaint();
    }

    //MODIFIES: this
    //EFFECTS: helper to add buttons to the portfolio manager internal frame
    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 1));
        buttonPanel.add(new JButton(new AddStockAction()));

        portManager.add(buttonPanel, BorderLayout.EAST);
    }

    //MODIFIES: this
    //EFFECTS: helper to add a menu bar to the UI
    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu stockMenu = new JMenu("Stock");
        stockMenu.setMnemonic('S');
        addMenuItem(stockMenu, new AddStockAction(),
                KeyStroke.getKeyStroke("control S"));
        menuBar.add(stockMenu);

        JMenu portfolioMenu = new JMenu("Portfolio");
        portfolioMenu.setMnemonic('P');
        addMenuItem(portfolioMenu, new LoadPortfolioAction(), null);
        addMenuItem(portfolioMenu, new SavePortfolioAction(), null);
        menuBar.add(portfolioMenu);

        setJMenuBar(menuBar);
    }

    //MODIFIES: this
    //EFFECTS: Adds an item with given handler to the given menu
    private void addMenuItem(JMenu theMenu, AbstractAction action, KeyStroke accelerator) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setMnemonic(menuItem.getText().charAt(0));
        menuItem.setAccelerator(accelerator);
        theMenu.add(menuItem);
    }

    // Represents the action to be taken when the user wants to save the portfolio to file
    private class SavePortfolioAction extends AbstractAction {

        //EFFECTS: creates an abstract action with the name Save Portfolio
        SavePortfolioAction() {
            super("Save Portfolio");
        }

        //MODIFIES: this
        //EFFECTS: when button pressed, saves the portfolio to file
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                jsonWriter.open();
                jsonWriter.write(portfolio);
                jsonWriter.close();
                JOptionPane.showMessageDialog(null, "Successfully Saved!", "Complete",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: Represents a helper to remove a stockUI from the desktop and the stock from portfolio
    public void removeStock(StockUI stockUI) {
        desktop.remove(stockUI);
        portfolio.removeStock(stockUI.getStock().getTicker());
        repaint();
    }

    // Represents the action to be taken when the user wants to load the portfolio from file
    private class LoadPortfolioAction extends AbstractAction {

        //EFFECTS: creates an abstract action with the name Load Portfolio
        LoadPortfolioAction() {
            super("Load Portfolio");
        }

        //MODIFIES: this
        //EFFECTS: when button pressed, removes all current stocks from UI and portfolio and loads portfolio from file
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                portfolio = jsonReader.read();
                for (StockUI s : stocks) {
                    desktop.remove(s);
                    repaint();
                }
                for (Stock s : portfolio.getOwned()) {
                    StockUI stockUI = new StockUI(s, PortfolioManagerUI.this);
                    stocks.add(stockUI);
                    desktop.add(stockUI);
                    repaint();
                }
                updateStatus();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // Represents the action to be taken when the user wants to add a new stock to the system.
    private class AddStockAction extends AbstractAction {

        //EFFECTS: creates an abstract action with the name Add Stock
        AddStockAction() {
            super("Add Stock");
        }

        //MODIFIES: this
        //EFFECTS: when button pressed, prompt user to give stock information and add a new stock with that info
        @Override
        public void actionPerformed(ActionEvent e) {
            String stockTicker = JOptionPane.showInputDialog(null,
                    "Stock ticker?", "Enter stock ticker", JOptionPane.QUESTION_MESSAGE);
            String stockPrice = JOptionPane.showInputDialog(null,
                    "Stock price?", "Enter stock price", JOptionPane.QUESTION_MESSAGE);
            String amountOwned = JOptionPane.showInputDialog(null,
                    "Amount Owned?", "Enter amount owned", JOptionPane.QUESTION_MESSAGE);
            double initialPrice = 0.0;
            int initialPurchase = 0;
            try {
                initialPrice = Double.parseDouble(stockPrice);
                initialPurchase = Integer.parseInt(amountOwned);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            Stock s = new Stock(stockTicker, initialPrice, initialPurchase);
            portfolio.addStock(s);
            StockUI stockUI = new StockUI(s, PortfolioManagerUI.this);
            stocks.add(stockUI);
            desktop.add(stockUI);
            updateStatus();
        }
    }

    //MODIFIES: this
    //EFFECTS: Helper to centre main application window on desktop
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    //Represents action to be taken when user clicks desktop to switch focus. (Needed for key handling.)
    private class DesktopFocusAction extends MouseAdapter {

        //MODIFIES: this
        //EFFECTS: on mouse click, requests a focus in window
        @Override
        public void mouseClicked(MouseEvent e) {
            PortfolioManagerUI.this.requestFocusInWindow();
        }
    }

    //EFFECTS: starts the application
    public static void main(String[] args) {
        new PortfolioManagerUI();
    }
}


