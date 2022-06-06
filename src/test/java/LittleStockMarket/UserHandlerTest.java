package LittleStockMarket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserHandlerTest {

    private static final String test_user_file_content = """
            fredloff;12345
            maggie;rolp3
            gibby;tatata
            dragonslayer;l0lfish
            """;

    private static final String invalid_userfile_content = """
            fred;loff;12,345
            ;;
            rolp3;maggie
            """;

    private UserHandler getUserHandler() {
        return new UserHandler();
    }

    private Map<String, String> getFilledUserMap() {

        Map<String, String> users = new HashMap<String, String>();

        users.put("fredloff", "12345");
        users.put("maggie", "rolp3");
        users.put("gibby", "tatata");
        users.put("dragonslayer", "l0lfish");

        return users;
    }

    @BeforeAll
    public void setup() throws IOException {
        Files.write(Paths.get(getUserHandler().getFile("test_user").getPath()),
                test_user_file_content.getBytes());
        Files.write(Paths.get(getUserHandler().getFile("invalid_userfile").getPath()),
                invalid_userfile_content.getBytes());
    }

    @Test
    public void testReadFromUsernames() throws FileNotFoundException {
        Map<String, String> expectedUserMap = getFilledUserMap();
        Map<String, String> actualUserMap = getUserHandler().readFromUserNames("test_user");

        Iterator<Map.Entry<String, String>> expectedIterator = expectedUserMap.entrySet().iterator();
        Iterator<Map.Entry<String, String>> actualIterator = actualUserMap.entrySet().iterator();

        while (expectedIterator.hasNext()) {
            try {
                Map.Entry<String, String> expectedMap = expectedIterator.next();
                Map.Entry<String, String> actualMap = actualIterator.next();
                assertEquals(expectedMap, actualMap);
            } catch (IndexOutOfBoundsException e) {
                fail("The lists were not the same length");
            }
        }
    }

    @Test
    public void testReadNonExistingFile() {
        assertThrows(FileNotFoundException.class, () -> getUserHandler().readFromUserNames("non_existing_file"),
                "FileNotFoundException should be thrown if file does not exist.");
    }

    @Test
    public void testReadInvalidUserFile() {
        assertThrows(IllegalArgumentException.class, () -> getUserHandler().readFromUserNames("invalid_userfile"),
                "IllegalArgumentException should be thrown if the content of the file is invalid.");
    }

    @Test
    public void testWriteNewUser() throws IOException {
        String[] user1 = { "fredloff", "12345" };
        String[] user2 = { "maggie", "rolp3" };
        String[] user3 = { "gibby", "tatata" };
        String[] user4 = { "dragonslayer", "l0lfish" };

        getUserHandler().writeFile("new_userfile", user1);
        getUserHandler().writeFile("new_userfile", user2);
        getUserHandler().writeFile("new_userfile", user3);
        getUserHandler().writeFile("new_userfile", user4);


        String expectedFile = getUserHandler().readFile("test_user");
        String actualFile = getUserHandler().readFile("new_userfile");

        assertEquals(expectedFile, actualFile, "Content of files are not the same.");
    }

    @AfterAll
    public void teardown() throws FileNotFoundException {
        getUserHandler().getFile("test_user").delete();
        getUserHandler().getFile("invalid_userfile").delete();
        getUserHandler().getFile("new_userfile").delete();
    }

}
