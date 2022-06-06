package LittleStockMarket;

import java.util.ArrayList;

public class Portfolio {
    private double value;
    private double capital;
    private ArrayList<OwnedStock> stocks = new ArrayList<OwnedStock>();
    private Market marked;
    private String userName;

    public Portfolio(double capital, double value, String userName, Market marked) {
        this.capital = capital;
        this.value = value;
        this.userName = userName;
        this.marked = marked;
    }

    public Market getMarket() {
        return this.marked;
    }

    public double getValue() {
        return Math.round(this.value * 100.0) / 100.0;
    }

    public double getCapital() {
        return Math.round(this.capital * 100.0) / 100.0;
    }

    public ArrayList<OwnedStock> getStocks() {
        return this.stocks;
    }

    public String getUserName() {
        return this.userName;
    }

    public int getStockAmount(String stockName) {
        int counter = 0;
        for (OwnedStock s : stocks) {
            if (s.getStockName().equals(stockName)) {
                counter++;
            }
        }
        return counter;
    }

    private OwnedStock castToOwnedStock(String stockName) throws IllegalArgumentException {
        Stock stock = this.marked.getStock(stockName);

        String stockname = stock.getStockName();
        Double stockprice = stock.getStockPrice();
        char stockrisk = stock.getRisk();

        return new OwnedStock(stockname, stockprice, stockrisk, this);
    }

    private boolean findStock(String stockName) {
        for (OwnedStock ownedStock : stocks) {
            if (ownedStock.getStockName().equals(stockName)) {
                return true;
            }
        }
        return false;
    }

    public void buyStock(String stockName, int amount) throws IllegalArgumentException {
        if (amount < 1)
            throw new IllegalArgumentException("You can not buy zero or negative amount of stocks");
        if (marked.getStock(stockName).getStockPrice() * amount > this.capital)
            throw new IllegalArgumentException("You do not have enough money for this stock!");

        OwnedStock myStockBought = castToOwnedStock(stockName);

        for (int i = 0; i < amount; i++) {
            this.getStocks().add(myStockBought);
            this.capital -= myStockBought.getStockPrice();
        }
    }

    public void sellStock(String stockName, int amount) throws IllegalArgumentException {
        if (amount < 1)
            throw new IllegalArgumentException("You can not sell zero or negative amount of stocks");
        OwnedStock owned = castToOwnedStock(stockName);

        if (!findStock(owned.getStockName()))
            throw new IllegalArgumentException("You do not have this stock!");

        int stockAmount = owned.getStockAmount();

        if (amount > stockAmount)
            throw new IllegalArgumentException("You do not have this many stocks to sell!");

        for (int i = 0; i < amount; i++) {
            this.removeStock(owned);
            this.capital += owned.getStockPrice();
        }
    }

    private void removeStock(OwnedStock stock) {
        String name = stock.getStockName();
        this.getStocks().remove(stocks.stream().filter(s -> s.getStockName().equals(name)).findFirst().get());
    }

    public void updatePortfolio() {
        this.value = capital;
        for (OwnedStock ownedStock : stocks) {
            for (Stock stck : marked.getAvailableStocks()) {
                if (stck.getStockName().equals(ownedStock.getStockName())) {
                    double verdi = stck.getStockPrice();
                    ownedStock.setStockPrice(verdi);
                    this.value += verdi;
                }
            }
        }
    }

    @Override
    public String toString() {
        String sysout = "Eier: " + this.getUserName();
        for (OwnedStock stock : stocks) {
            sysout += " " + stock;
        }
        return sysout;
    }

}
