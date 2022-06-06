package LittleStockMarket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PortfolioTest {
    private Market market;
    private Portfolio portfolio;
    private double capital = 100;
    private double value = 100;
    private String userName = "fredloff";

    @BeforeEach
    public void setup() {
        try {
            ArrayList<Stock> stocks = new MarketHandler().getStocksFromFile("Stocks");
            market = new Market(stocks);
            portfolio = new Portfolio(capital, value, userName, market);
            portfolio.buyStock("Janus", 1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testBuyStock() throws FileNotFoundException {
        assertThrows(IllegalArgumentException.class, () -> {
            portfolio.buyStock("tull", 1);
        }, "IllegalArgument skal utløses når man prøver å kjøpe en aksje som ikke finnes i markedet");

        portfolio.buyStock("Bindeleddet", 3);
        assertEquals("Bindeleddet", portfolio.getStocks().stream().filter(s -> s.getStockName().equals("Bindeleddet"))
                .findFirst().get().getStockName());

        assertEquals(3, portfolio.getStockAmount("Bindeleddet"));

        assertThrows(IllegalArgumentException.class, () -> {
            portfolio.buyStock("Thilde", -1);
        }, "IllegalArgument skal utløses når man prøver å kjøpe et negativt antall aksjer");
        assertThrows(IllegalArgumentException.class, () -> {
            portfolio.buyStock("Janus", 0);
        }, "IllegalArgument skal utløses når man prøver å kjøpe null antall aksjer");
    }

    @Test
    public void testSellStock() throws FileNotFoundException {
        assertThrows(IllegalArgumentException.class, () -> {
            portfolio.sellStock("tull", 1);
        }, "IllegalArgument skal utløses når man prøver å selge en aksje man ikke eier");

        portfolio.buyStock("Thilde", 4);

        assertThrows(IllegalArgumentException.class, () -> {
            portfolio.sellStock("Thilde", -1);
        }, "IllegalArgument skal utløses når man prøver å selge negativt antall aksjer");
        assertThrows(IllegalArgumentException.class, () -> {
            portfolio.sellStock("Thilde", 5);
        }, "IllegalArgument skal utløses når man prøver å selge fler av en aksje enn det man eier");

        portfolio.sellStock("Thilde", 3);
        assertEquals(1, portfolio.getStockAmount("Thilde"));
    }

    /**
     * I testen under tar vi forbehold for at den nye summen av alle aksjene på
     * markedet ikke er lik den forrige
     * Sannsynligheten for dette er minimal, men samtidig ikke noe vi kan 100%
     * gardere oss for.
     */

    @Test
    @DisplayName("Tester update portfolio ved å oppdatere markedet, kjøpe og selge aksjer")
    public void testUpdatePortfolio() {

        double sum = portfolio.getStocks().stream().map(OwnedStock::getStockPrice).reduce((a, b) -> a + b).get();
        market.updatePrices();
        portfolio.updatePortfolio();
        assertTrue(sum != portfolio.getStocks().stream().map(OwnedStock::getStockPrice).reduce((a, b) -> a + b).get());
        double sum2 = portfolio.getStocks().stream().map(OwnedStock::getStockPrice).reduce((a, b) -> a + b).get();
        portfolio.buyStock("Bindeleddet", 2);
        portfolio.updatePortfolio();
        assertTrue(sum2 != portfolio.getStocks().stream().map(OwnedStock::getStockPrice).reduce((a, b) -> a + b).get());
        double sum3 = portfolio.getStocks().stream().map(OwnedStock::getStockPrice).reduce((a, b) -> a + b).get();
        portfolio.sellStock("Janus", 1);
        portfolio.updatePortfolio();
        assertTrue(sum3 != portfolio.getStocks().stream().map(OwnedStock::getStockPrice).reduce((a, b) -> a + b).get());
    }
}
