package LittleStockMarket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class User {
    private String userName;
    private String password;
    private double capital = 100;
    private Market marked;
    private Portfolio portfolio;

    public String getPassword() {
        return this.password;
    }

    public Market getMarket() {
        return this.marked;
    }

    public Portfolio getPortfolio() {
        return this.portfolio;
    }

    public String getUserName() {
        return this.userName;
    }

    public double getCapital() {
        return this.capital;
    }

    public static void checkUserName(String username) throws IllegalArgumentException {
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException(
                    "Username can only contain small and large cap english characters and numbers");
        }
    }

    public static void checkPassword(String password) throws IllegalArgumentException {
        if (!password.matches("^[a-zA-Z0-9]{4,20}$")) {
            throw new IllegalArgumentException(
                    "Password can only contain small and large cap english characters and numbers and must be between 4 and 20 characters");
        }
    }

    private void assignPortfolio() throws FileNotFoundException {
        if (hasPortfolio()) {
            try {
                this.portfolio = PortfolioHandler.getInstance().createPortfolio(this.getUserName());
                this.marked = this.portfolio.getMarket();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new FileNotFoundException("Portfolio file could not be accessed");
            }
        } else {
            try {
                this.marked = new Market(MarketHandler.getInstance().getStocksFromFile("Stocks"));
                this.portfolio = new Portfolio(this.capital, this.capital, this.userName, this.marked);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new FileNotFoundException("Market file could not be accessed");
            }

        }
    }

    private boolean hasPortfolio() throws FileNotFoundException {
        File f = UserHandler.getInstance().getFile(this.getUserName());
        if (f.exists()) {
            return true;
        }
        return false;
    }

    public User(String userName, String password) throws IllegalArgumentException, IOException {
        this(userName, password, "usernames");
    }

    public User(String userName, String password, String filename) throws IllegalArgumentException, IOException {
        if (userName == "" || password == "") {
            throw new IllegalArgumentException("Username or password not detected");
        }
        if (!UserHandler.getInstance().checkUserNameExists(userName, filename))
            throw new IllegalArgumentException("User does not exist");
        if (!UserHandler.getInstance().checkUserNameAndPasswordFit(userName, password, filename))
            throw new IllegalArgumentException("Username and password does not fit");
        this.userName = userName;
        this.password = password;
        assignPortfolio();
    }

    @Override
    public String toString() {
        return "Username: " + getUserName() + "Capital: " + getCapital() + "Value: " + portfolio.getValue();
    }
}
