package LittleStockMarket;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PortfolioHandler extends txtFileSaving {
    private final static PortfolioHandler instance = new PortfolioHandler();

    public static PortfolioHandler getInstance() {
        return instance;
    }

    public Portfolio createPortfolio(String filename) throws FileNotFoundException {
        String fileContent = readFile(filename);

        if (!fileContent.strip().matches(
                "^[a-z]+;[0-9]+\\.[0-9]+;[0-9]+\\.[0-9]+\\-([a-zA-Z]+;[0-9]+\\.[0-9]+;[H,M,L]\\*)*\\-([a-zA-Z]+;[0-9]+\\.[0-9]+;[H,M,L]\\*)*$")) {
            throw new IllegalArgumentException("Portfoliofile is on the wrong format.");
        }

        String[] information = fileContent.split("-");

        String portfolioInfo = information[0].strip();

        String ownedStockInfo = information[1].strip();

        String marketInfo = information[2].strip();

        String[] portfolioProperties = portfolioInfo.split(";");
        String userName = portfolioProperties[0];
        double capital = Double.parseDouble(portfolioProperties[1]);
        double value = Double.parseDouble(portfolioProperties[2]);

        String[] singleAvailableStock = marketInfo.split("\\*");

        ArrayList<Stock> availableStocks = new ArrayList<>();

        for (String stock : singleAvailableStock) {
            String[] marketProperties = stock.split(";");
            String stockName = marketProperties[0];
            double stockPrice = Double.parseDouble(marketProperties[1]);
            char risk = marketProperties[2].charAt(0);

            Stock tmp = new Stock(stockName, stockPrice, risk);
            availableStocks.add(tmp);
        }

        Market m = new Market(availableStocks);

        Portfolio p = new Portfolio(capital, value, userName, m);

        String[] singleOwnedStock = ownedStockInfo.split("\\*");

        for (String stock : singleOwnedStock) {
            if (stock.isEmpty())
                continue;
            String[] ownedStockProperties = stock.split(";");

            String stockName = ownedStockProperties[0];
            double stockPrice = Double.parseDouble(ownedStockProperties[1]);
            char risk = ownedStockProperties[2].charAt(0);

            OwnedStock tmp = new OwnedStock(stockName, stockPrice, risk, p);

            p.getStocks().add(tmp);
        }

        return p;
    }

    @Override
    public void writeFile(String filename, Object object) throws FileNotFoundException, IllegalArgumentException {
        if (object instanceof Portfolio) {
            try (PrintWriter writer = new PrintWriter(getFile(filename))) {
                Portfolio portfolio = (Portfolio) object;
                writer.print(portfolio.getUserName() + ";" + portfolio.getCapital() + ";" + portfolio.getValue());

                writer.print("-");

                for (OwnedStock ownedStock : portfolio.getStocks()) {
                    writer.print(ownedStock.getStockName() + ";"
                            + ownedStock.getStockPrice() + ";"
                            + ownedStock.getRisk() + "*");
                }

                writer.print("-");

                for (Stock availableStock : portfolio.getMarket().getAvailableStocks()) {
                    writer.print(availableStock.getStockName()
                            + ";"
                            + availableStock.getStockPrice() + ";" +
                            availableStock.getRisk() + "*");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new FileNotFoundException("File not found.");
            }
        } else
            throw new IllegalArgumentException("Object must be instance of Portfolio");
    }
}
