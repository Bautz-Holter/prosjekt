package LittleStockMarket;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserHandler extends txtFileSaving {
    private final static UserHandler instance = new UserHandler();

    public static UserHandler getInstance() {
        return instance;
    }

    public Map<String, String> readFromUserNames(String filename)
            throws FileNotFoundException, IllegalArgumentException {
        String fileContent = readFile(filename);

        if (fileContent == "") {
            Map<String, String> userMap = new HashMap<>();
            return userMap;
        }

        if (!fileContent.lines().allMatch(l -> l.matches("^[a-zA-Z0-9]{1,20};[a-zA-Z0-9]{4,20}$"))) {
            throw new IllegalArgumentException("Userfile is on the wrong format.");
        }

        String[] users = fileContent.split("\n");
        Map<String, String> userMap = new HashMap<>();

        for (String user : users) {
            String[] u = user.split(";");
            userMap.put(u[0], u[1]);
        }
        return userMap;
    }

    @Override
    public void writeFile(String filename, Object object) throws FileNotFoundException, IOException, IllegalArgumentException {
        if (object instanceof String[]) {
            String[] user = (String[]) object;
            String username = user[0];
            String password = user[1];

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFile(filename), true))) {
                writer.write(username.toLowerCase() + ";" + password + "\n");
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException();
            }
        }
        else throw new IllegalArgumentException("Object must be instance of StringArray");
    }

    public boolean checkUserNameExists(String userName, String filename) throws FileNotFoundException {
        if (readFromUserNames(filename).keySet().contains(userName.toLowerCase())) {
            return true;
        }
        return false;
    }

    public boolean checkUserNameAndPasswordFit(String userName, String password, String filename)
            throws FileNotFoundException {
        Optional<String> result = readFromUserNames(filename).entrySet().stream()
                .filter(u -> u.getKey().toLowerCase().equals(userName.toLowerCase())).map(Map.Entry::getValue)
                .findAny();
        if (!result.get().equals(password)) {
            return false;
        }
        return true;
    }
}
