package LittleStockMarket;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StockTest {
    private Stock stock;
    private String stockName = "Jinneleddet";
    private double stockPrice = 30;
    private char risk = 'H';

    @BeforeEach
    public void setup() {
        stock = new Stock(stockName, stockPrice, risk);
    }

    @Test
    @DisplayName("Tester at setPrice funker")
    public void testSetStockPrice() {
        stock.setStockPrice(stockPrice - 1);
        Assertions.assertNotEquals(stockPrice, stock.getStockPrice());
        Assertions.assertEquals(stockPrice-1, stock.getStockPrice());
    }

    @Test
    @DisplayName("Tester at prisen endrer seg ved metodekall")
    public void testFluctuatingStockPrice() {
        stock.fluctuatingStockPrice();
        Assertions.assertNotEquals(stockPrice, stock.getStockPrice());
        assertTrue(stock.getStockPrice() >= stockPrice*0.7);
        assertTrue(stock.getStockPrice() <= stockPrice*1.6);
    }
   
}
