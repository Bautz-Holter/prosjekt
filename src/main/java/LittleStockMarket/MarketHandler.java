package LittleStockMarket;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MarketHandler extends txtFileSaving {
    private final static MarketHandler instance = new MarketHandler();

    public static MarketHandler getInstance() {
        return instance;
    }

    public ArrayList<Stock> getStocksFromFile(String filename) throws FileNotFoundException {
        String fileContent = readFile(filename);

        if (!fileContent.lines().allMatch(l -> l.matches("^[a-zA-Z]+,{1}[0-9]+\\.[0-9]+,{1}[H,M,L]{1}$"))) {
            throw new IllegalArgumentException("Marketfile is on the wrong format.");
        }

        ArrayList<Stock> list = new ArrayList<Stock>();
        String[] stocks = fileContent.split("\n");

        for (String stock : stocks) {
            String[] stockArray = stock.split(",");
            list.add(new Stock(stockArray[0], Double.parseDouble(stockArray[1]), stockArray[2].charAt(0)));
        }
        return list;
    }

    @Override
    public void writeFile(String filename, Object object) throws FileNotFoundException, IllegalArgumentException {
        if (object instanceof Market) {
            try (PrintWriter writer = new PrintWriter(getFile(filename))) {
                Market market = (Market) object;
                for (Stock stock : market.getAvailableStocks()) {
                    writer.print(stock.getStockName() + "," + stock.getStockPrice() + "," + stock.getRisk() + "\n");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new FileNotFoundException();
            }
        }
        else throw new IllegalArgumentException("Object must be instance of Market");
    }
}