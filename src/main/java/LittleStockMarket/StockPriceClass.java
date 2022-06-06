package LittleStockMarket;

import java.util.Random;

public class StockPriceClass {

    public double lowRiskPrice() {
        Random randint = new Random();
        double upperbound = 1.07;
        double lowerbound = 0.94;

        double price = 1.0;
        while (price == 1.0)
            price = randint.nextDouble(lowerbound, upperbound);

        return price;

    }

    public double mediumRiskPrice() {
        Random randint = new Random();
        double upperbound = 1.09;
        double lowerbound = 0.92;

        double price = 1.0;
        while (price == 1.0)
            price = randint.nextDouble(lowerbound, upperbound);

        return price;

    }

    public double highRiskPrice() {
        Random randint = new Random();
        double upperbound = 1.11;
        double lowerbound = 0.90;

        double price = 1.0;
        while (price == 1.0)
            price = randint.nextDouble(lowerbound, upperbound);

        return price;

    }
}
