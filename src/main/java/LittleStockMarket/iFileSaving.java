package LittleStockMarket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface iFileSaving {

    String readFile(String filename) throws FileNotFoundException;

    void writeFile(String filename, Object object) throws FileNotFoundException, IOException, IllegalArgumentException;

    File getFile(String filename) throws FileNotFoundException;

}
