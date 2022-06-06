package LittleStockMarket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PortfolioHandlerTest {
    private String userName = "trine";
    private double capital = 100;
    private double value = 100;

    private static final String test_portfolio_file_content = """
            trine;100.0;100.0-Uara;5.0;M*Uara;5.0;M*Uara;5.0;M*Equisouth;10.0;L*Equisouth;10.0;L*-Uara;5.0;M*Equisouth;10.0;L*
                """;

    private static final String test_portfolio_market = """
            Uara,5.0,M
            Equisouth,10.0,L
            """;

    private static final String invalid_portfoliofile_content = """
            tri.ne;65;;;100.0-Uara;5.0;M*Uar457a;5.0;M*U0ra;5.0;M*Equis;10.0;L*-Uara;5.0;M*fd,,:;;Equisouth;10.0;L*
                """;

    private PortfolioHandler getPortfolioHandler() {
        return new PortfolioHandler();
    }

    private Portfolio getPlainTestPortfolioObject() throws FileNotFoundException {
        return new Portfolio(capital, value, userName,
                new Market(new MarketHandler().getStocksFromFile("test_portfolio_market")));
    }

    private Portfolio getFilledTestPortfolioObject() throws FileNotFoundException {
        Portfolio portfolio = getPlainTestPortfolioObject();

        portfolio.getStocks().add(new OwnedStock("Uara", 5.0, 'M', portfolio));
        portfolio.getStocks().add(new OwnedStock("Uara", 5.0, 'M', portfolio));
        portfolio.getStocks().add(new OwnedStock("Uara", 5.0, 'M', portfolio));
        portfolio.getStocks().add(new OwnedStock("Equisouth", 10.0, 'L', portfolio));
        portfolio.getStocks().add(new OwnedStock("Equisouth", 10.0, 'L', portfolio));

        return portfolio;
    }

    @BeforeAll
    public void setup() throws IOException {
        Files.write(Paths.get(getPortfolioHandler().getFile("test_portfolio").getPath()),
                test_portfolio_file_content.getBytes());

        Files.write(Paths.get(getPortfolioHandler().getFile("invalid_portfoliofile").getPath()),
                invalid_portfoliofile_content.getBytes());

        Files.write(Paths.get(getPortfolioHandler().getFile("test_portfolio_market").getPath()),
                test_portfolio_market.getBytes());
    }

    @Test
    public void testReadPortfolio() throws FileNotFoundException {
        Portfolio expectedPortfolioObject = getFilledTestPortfolioObject();
        Portfolio actualPortfolioObject = getPortfolioHandler().createPortfolio("test_portfolio");

        Iterator<OwnedStock> expectedIterator = expectedPortfolioObject.getStocks().iterator();
        Iterator<OwnedStock> actualIterator = actualPortfolioObject.getStocks().iterator();

        while (expectedIterator.hasNext()) {
            try {
                OwnedStock expectedOwnedStock = expectedIterator.next();
                OwnedStock actualOwnedStock = actualIterator.next();
                assertEquals(expectedOwnedStock, actualOwnedStock);
            } catch (IndexOutOfBoundsException e) {
                fail("The lists were not the same length");
            }
        }
    }

    @Test
    public void testReadNonExistingFile() {
        assertThrows(FileNotFoundException.class, () -> getPortfolioHandler().createPortfolio("non_existing_file"),
                "FileNotFoundException should be thrown if file does not exist.");
    }

    @Test
    public void testReadInvalidPortfolioFile() {
        assertThrows(IllegalArgumentException.class,
                () -> getPortfolioHandler().createPortfolio("invalid_portfoliofile"),
                "IllegalArgumentException should be thrown if the content of the file is invalid.");
    }

    @Test
    public void testWriteNewPortfolio() throws IOException {
        double capital = 100;
        double value = 100;
        String userName = "trine";

        Portfolio p = new Portfolio(capital, value, userName,
                new Market(new MarketHandler().getStocksFromFile("test_portfolio_market")));

        p.getStocks().add(new OwnedStock("Uara", 5.0, 'M', p));
        p.getStocks().add(new OwnedStock("Uara", 5.0, 'M', p));
        p.getStocks().add(new OwnedStock("Uara", 5.0, 'M', p));
        p.getStocks().add(new OwnedStock("Equisouth", 10.0, 'L', p));
        p.getStocks().add(new OwnedStock("Equisouth", 10.0, 'L', p));

        getPortfolioHandler().writeFile("new_portfoliofile", p);

        String expectedFile = getPortfolioHandler().readFile("test_portfolio");
        String actualFile = getPortfolioHandler().readFile("new_portfoliofile");

        assertEquals(expectedFile, actualFile, "Content of files are not the same.");

    }

    @AfterAll
    public void teardown() throws FileNotFoundException {
        getPortfolioHandler().getFile("test_portfolio").delete();
        getPortfolioHandler().getFile("invalid_portfoliofile").delete();
        getPortfolioHandler().getFile("new_portfoliofile").delete();
        getPortfolioHandler().getFile("test_portfolio_market").delete();
    }
}
