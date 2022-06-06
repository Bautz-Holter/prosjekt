package LittleStockMarket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public abstract class txtFileSaving implements iFileSaving {

    @Override
    public String readFile(String filename) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(getFile(filename))) {

            String fileContent = "";

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fileContent = fileContent.concat(line + "\n");
            }
            return fileContent;

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Did not find file!");
        }
    }

    @Override
    public abstract void writeFile(String filename, Object object) throws FileNotFoundException, IOException, IllegalArgumentException;

    @Override
    public File getFile(String filename) throws FileNotFoundException {
        return new File("src/main/resources/" + filename + ".txt");
    }

}
