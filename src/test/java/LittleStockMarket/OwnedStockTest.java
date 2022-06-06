package LittleStockMarket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OwnedStockTest {
    private OwnedStock stock;
    private String stockName = "Bindeleddet";
    private double stockPrice = 10.0;
    private char risk = 'H';
    private String userName = "Alfred";
    private Market market;
    private Portfolio portfolio;

    @BeforeEach
    public void setup() throws FileNotFoundException {
        market = new Market(MarketHandler.getInstance().getStocksFromFile("Stocks"));
        portfolio = new Portfolio(100.0, 100.0, userName, market);
        portfolio.buyStock(stockName, 1);
        stock = new OwnedStock(stockName, stockPrice, risk, portfolio);
    }

    @Test
    @DisplayName("Test getPortfolio")
    public void testGetPortfolio() {
        assertEquals(portfolio, stock.getPortfolio());
    }

    @Test
    @DisplayName("Test getStockAmount")
    public void testGetStockAmount() {
        assertEquals(1, stock.getStockAmount());
        portfolio.buyStock("Bindeleddet", 1);
        assertEquals(2, stock.getStockAmount());
    }
}
