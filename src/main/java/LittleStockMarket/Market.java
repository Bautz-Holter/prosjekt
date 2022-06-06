package LittleStockMarket;

import java.util.ArrayList;
import java.util.Optional;

public class Market {
    private ArrayList<Stock> availableStocks = new ArrayList<Stock>();
    private double index;

    public ArrayList<Stock> getAvailableStocks() {
        return this.availableStocks;
    }

    public double getIndex() {
        return Math.round(this.index * 100.0) / 100.0;
    }

    public Stock getStock(String stockName) {
        if(stockName == null) throw new IllegalArgumentException("This stock does not exist");
        Optional<Stock> s = this.availableStocks.stream()
                .filter(stck -> stck.getStockName().toLowerCase().equals(stockName.toLowerCase())).findAny();
        if (!s.isPresent())
            throw new IllegalArgumentException("This stock does not exist");
        return s.get();
    }

    public void updatePrices() {
        for (Stock stock : this.availableStocks) {
            stock.fluctuatingStockPrice();
        }
        updateIndex();
    }

    private void updateIndex() {
        this.index = availableStocks.stream().map(Stock::getStockPrice).reduce((a, b) -> a+b).get();
    }

    public Market(ArrayList<Stock> availableStocks) {
        this.availableStocks = availableStocks;
        updateIndex();
    }

    @Override
    public String toString() {
        return "" + this.availableStocks;
    }
}
