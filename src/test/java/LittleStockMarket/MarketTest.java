package LittleStockMarket;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MarketTest {
    private Market market;
    private ArrayList<Stock> stocks;
    private Stock stock1;
    private Stock stock2;
    private Stock stock3;
    private double startIndex;
   
   
    @BeforeEach
    public void setup() {
        stocks = new ArrayList<>();
        stock1 = new Stock("Eksempel 1", 10.0, 'L');
        stock2 = new Stock("Eksempel 2", 20.0, 'M');
        stock3 = new Stock("Eksempel 3", 30.0, 'H');

        stocks.add(stock1);
        stocks.add(stock2);
        stocks.add(stock3);

        market = new Market(stocks);

        startIndex = stock1.getStockPrice() + stock2.getStockPrice() + stock3.getStockPrice();
    }

    @Test
    @DisplayName("Sjekk at index stemmer")
    public void checkIndex() {
        Assertions.assertEquals(startIndex, market.getIndex());
    }

    @Test
    @DisplayName("Sjekk at aksjer som skal være i markedet er der")
    public void checkAvailableStocks() {
        Assertions.assertTrue(market.getAvailableStocks().contains(stock1));
        Assertions.assertEquals(stock2, market.getStock("Eksempel 2"));
        for (Stock stock : stocks) {
            assertTrue(market.getAvailableStocks().contains(stock));
        }
    }

    @Test
    @DisplayName("Sjekk at bare aksjer som skal være i markedet er der")
    public void checkThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            market.getStock("Eksempel 5");
        }, "Tester en aksje som ikke skal eksistere");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            market.getStock(null);
        }, "Tester en aksje som ikke skal eksistere");
    }

    @Test
    @DisplayName("Sjekk at prisene oppdaterer seg")
    public void testUpdatePrices() {
        double eks1price = stock1.getStockPrice();
        market.updatePrices();
        Assertions.assertNotEquals(startIndex, market.getIndex());
        Assertions.assertNotEquals(eks1price, stock1.getStockPrice());
    }
}
