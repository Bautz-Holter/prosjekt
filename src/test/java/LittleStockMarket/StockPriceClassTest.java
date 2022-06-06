package LittleStockMarket;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StockPriceClassTest {
    private StockPriceClass stockPriceClass;
    private double testPrice;

    @BeforeEach
    public void setup() {
        stockPriceClass = new StockPriceClass();
        testPrice = 10.0;
    }

    @Test
    @DisplayName("Test lowriskprice")
    public void testLowRiskPrice() {
        testPrice = testPrice * stockPriceClass.lowRiskPrice();
        assertNotEquals(10.0, testPrice);
        assertTrue(testPrice > 10.0*0.9 && testPrice < 10.0*1.2);
    }

    @Test
    @DisplayName("Test mediumriskprice")
    public void testMediumRiskPrice() {
        testPrice = testPrice * stockPriceClass.lowRiskPrice();
        assertNotEquals(10.0, testPrice);
        assertTrue(testPrice > 10.0*0.8 && testPrice < 10.0*1.4);
    }

    @Test
    @DisplayName("Test highriskprice")
    public void testHighRiskPrice() {
        testPrice = testPrice * stockPriceClass.lowRiskPrice();
        assertNotEquals(10.0, testPrice);
        assertTrue(testPrice > 10.0*0.7 && testPrice < 10.0*1.6);
    }
}
