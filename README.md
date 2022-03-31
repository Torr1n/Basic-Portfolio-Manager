# Investment Portfolio Manager

## An application that allows you to buy and sell shares of stocks, and shows the growth and profit of your portfolio

***What will the application do?***
- The application will allow the user to specify a stock and its share price and purchase a number of shares
- The user will be able to see all the stocks they own, and their total value
- Within the application, the user can change the price of a stock, which is reflected in their portfolio 
- The user can sell shares and will be told how much profit they made on the stock, in *$ and %*

***Who will use it?***
- People who want to trade stocks both over a long or short period of time
- People who want to track how much money they made or lost on specific stocks
- People who want to be able to see and track all of their stocks in one place

***Why is this project of interest to you?***
This project is of interest to me because I am passionate about finance and, and being in the BUCS program I am 
pursuing business in addition to computer science. I feel like this project is an opportunity for me to start to 
combine these two interests by creating a program that integrates these two fields, even if it is only at a basic 
level.

## User Stories

- *As a user,* I want to be able to buy a given number of shares of a stock for my portfolio 
- *As a user,* I want to be able to view the list of stocks that I own 
- *As a user,* I want to be able to get the total value of all the stocks I own
- *As a user,* I want to be able to get the total profit I have made on all of my owned stocks
- *As a user,* I want to be able to sell shares of a stock 
- *As a user,* I want to be able to change the price of a stock and see that reflected in the value of my portfolio
- *As a user,* I want to be given the option to save my portfolio to file
- *As a user,* I want to be given the option to load my portfolio from file

***Phase 4: Task 2***
Mon Mar 28 22:08:27 PDT 2022
Added a new stock: AAPL with 10 shares and at a price of $100.0 per share
Mon Mar 28 22:08:31 PDT 2022
Bought 10 shares of AAPL
Mon Mar 28 22:08:34 PDT 2022
Sold 5 shares of AAPL
Mon Mar 28 22:08:43 PDT 2022
Added a new stock: TSLA with 100 shares and at a price of $90.0 per share
Mon Mar 28 22:09:34 PDT 2022
Updated AAPL's price to 150.0

***Phase 4: Task 3***
- If I had more time, I would refactor the PortfolioManagerUI so that it doesn't have the two paths in the
UML diagram to get information about a stock. Currently, there is excess coupling here that should ideally be reduced.
I would do this by having the StockUI getting information about stock through PortfolioManagerUI, since they are
already coupled.