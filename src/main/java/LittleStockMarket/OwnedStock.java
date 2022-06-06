package LittleStockMarket;

public class OwnedStock extends Stock {

    private Portfolio portfolio;

    public OwnedStock(String stockName, double stockPrice, char risk, Portfolio portfolio) {
        super(stockName, stockPrice, risk);
        this.portfolio = portfolio;
    }

    public Portfolio getPortfolio() {
        return this.portfolio;
    }

    public int getStockAmount() {
        return this.portfolio.getStockAmount(this.getStockName());
    }

    public String toString() {
        return super.toString() + "Eier: " + this.portfolio.getUserName();
    }
}
