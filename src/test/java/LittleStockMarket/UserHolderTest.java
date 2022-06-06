package LittleStockMarket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserHolderTest {
    private User user;
    private UserHolder userHolder;
    private static final String test_user_file_content = """
            test;12345
                        """;

    @BeforeEach
    public void setup() throws IllegalArgumentException, IOException {
        Files.write(Paths.get(UserHandler.getInstance().getFile("test_user").getPath()),
                test_user_file_content.getBytes());
        user = new User("test", "12345", "test_user");
        userHolder = UserHolder.getInstance();
        userHolder.setUser(user);
    }

    @Test
    @DisplayName("Sjekk at userHolder returneres")
    public void testGetInstance() {
        assertTrue(UserHolder.getInstance() instanceof UserHolder);
    }

    @Test
    @DisplayName("Sjekk at user returneres fra getUser")
    public void testGetUser() {
        assertEquals(user, userHolder.getUser());
    }

    @AfterAll
    public void teardown() throws FileNotFoundException {
        UserHandler.getInstance().getFile("test_user").delete();
    }
}
