package LittleStockMarket;

public class Stock {

    private String stockName;
    private double stockPrice;
    private double OGStockPrice;
    private char risk;

    public String getStockName() {
        return this.stockName;
    }

    public double getStockPrice() {
        return Math.round(this.stockPrice * 100.0) / 100.0;
    }

    public char getRisk() {
        return this.risk;
    }

    public String getStockChangePercent() {
        double change = this.getStockPrice() - this.OGStockPrice;
        double percentage = ((change / this.OGStockPrice)) * 100;

        return String.valueOf(Math.round(percentage * 100.0) / 100.0) + "%";
    }

    public void setStockPrice(double stockPrice) throws IllegalArgumentException{
        if(stockPrice < 0) throw new IllegalArgumentException("Stock price can not be negative value");
        this.stockPrice = stockPrice;
    }

    public void fluctuatingStockPrice() {
        StockPriceClass spc = new StockPriceClass();
        if (risk == 'L') {
            setStockPrice(stockPrice * spc.lowRiskPrice());

        }

        else if (risk == 'M') {
            setStockPrice(stockPrice * spc.mediumRiskPrice());
        }

        else if (risk == 'H') {
            setStockPrice(stockPrice * spc.highRiskPrice());
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (!(object instanceof Stock))
            return false;
        Stock stock = (Stock) object;
        if (stock.getStockName().equals(this.stockName))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "navn: " + this.getStockName() + ", pris: " + this.getStockPrice()
                + ", risk: " + this.getRisk();
    }

    public Stock(String stockName, double stockPrice, char risk) {
        this.stockName = stockName;
        this.stockPrice = stockPrice;
        this.OGStockPrice = stockPrice;
        this.risk = risk;
    }
}
