package LittleStockMarket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MarketHandlerTest {

    private static final String test_market_file_content = """
            yeet,10.0,L
            polo,10.0,H
            snacky,15.0,H
            hubba,5.0,M
                    """;

    private static final String invalid_marketfile_content = """
            10.0, yeet, L
            polo,10.0,H
            ,,
            hu,1,3,
            """;

    private MarketHandler getMarketHandler() {
        return new MarketHandler();
    }

    private Market getFilledTestMarketObject() {
        ArrayList<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock("yeet", 10.0, 'L'));
        stocks.add(new Stock("polo", 10.0, 'H'));
        stocks.add(new Stock("snacky", 15.0, 'H'));
        stocks.add(new Stock("hubba", 5.0, 'M'));

        return new Market(stocks);
    }

    @BeforeAll
    public void setup() throws IOException {
        Files.write(Paths.get(getMarketHandler().getFile("test_market").getPath()),
                test_market_file_content.getBytes());

        Files.write(Paths.get(getMarketHandler().getFile("invalid_marketfile").getPath()),
                invalid_marketfile_content.getBytes());
    }

    @Test
    public void testGetStocksFromFile() throws FileNotFoundException {
        Market expectedMarketObject = getFilledTestMarketObject();
        Market actualMarketObject = new Market(getMarketHandler().getStocksFromFile("test_market"));

        Iterator<Stock> expectedIterator = expectedMarketObject.getAvailableStocks().iterator();
        Iterator<Stock> actualIterator = actualMarketObject.getAvailableStocks().iterator();

        while (expectedIterator.hasNext()) {
            try {
                Stock expectedStock = expectedIterator.next();
                Stock actualStock = actualIterator.next();
                assertEquals(expectedStock, actualStock);
            } catch (IndexOutOfBoundsException e) {
                fail("The lists were not the same length");
            }
        }
    }

    @Test
    public void testReadNonExistingFile() {
        assertThrows(FileNotFoundException.class, () -> getMarketHandler().getStocksFromFile("non_existing_file"),
                "FileNotFoundException should be thrown if file does not exist.");
    }

    @Test
    public void testReadInvalidUserFile() {
        assertThrows(IllegalArgumentException.class, () -> getMarketHandler().getStocksFromFile("invalid_marketfile"),
                "IllegalArgumentException should be thrown if the content of the file is invalid.");
    }

    @Test
    public void testWriteStocks() throws IOException {
        ArrayList<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock("yeet", 10.0, 'L'));
        stocks.add(new Stock("polo", 10.0, 'H'));
        stocks.add(new Stock("snacky", 15.0, 'H'));
        stocks.add(new Stock("hubba", 5.0, 'M'));

        Market m = new Market(stocks);

        getMarketHandler().writeFile("new_marketfile", m);

        String expectedFile = getMarketHandler().readFile("test_market");
        String actualFile = getMarketHandler().readFile("new_marketfile");

        assertEquals(expectedFile, actualFile, "Content of files are not the same.");
    }

    @AfterAll
    public void teardown() throws FileNotFoundException {
        getMarketHandler().getFile("test_market").delete();
        getMarketHandler().getFile("invalid_marketfile").delete();
        getMarketHandler().getFile("new_marketfile").delete();
    }
}
