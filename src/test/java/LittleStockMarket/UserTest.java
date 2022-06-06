package LittleStockMarket;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTest {
    private User user;
    private User user2;
    private String userName = "Meg";
    private String password = "12345";

    private static final String test_user_file_content = """
            meg;12345
                        """;

    @BeforeEach
    private void setup() throws IOException {
        Files.write(Paths.get(UserHandler.getInstance().getFile("test_user").getPath()),
                test_user_file_content.getBytes());
        try {
            user = new User(userName, password, "test_user");
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test om brukernvn er tilgjengelig - metoden fungerer")
    public void testCheckAvailableUserName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            user2 = new User("meg", "123456", "test_user");
        }, "Tester et brukernavn som allerede er i bruk");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            user2 = new User("meg", "123456", "test_user");
        }, "Tester små bokstaver");
    }

    @Test
    @DisplayName("Tester om brukernavn og passord - sammenhengen funker")
    public void testCheckUserNameAndPasswordFit() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            user = new User("meg", "1234");
        }, "Tester feil passord");
    }

    @Test
    @DisplayName("Test at passordsjekk - metoden fungerer")
    public void testCheckPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            User.checkPassword("123");
        }, "Tester at for kort passord kaster feilmelding");
        assertThrows(IllegalArgumentException.class, () -> {
            User.checkPassword("");
        }, "Tester at ingenting kaster feilmelding");
        assertThrows(IllegalArgumentException.class, () -> {
            User.checkPassword("heisann;");
        }, "Tester at semikolon kaster feilmelding");
        assertThrows(IllegalArgumentException.class, () -> {
            User.checkPassword("heisann$");
        }, "Tester at spesialtegn kaster feilmelding");
    }

    @Test
    @DisplayName("Tester at brukernavnsjekk - metoden fungerer")
    public void testCheckUserName() {
        assertThrows(IllegalArgumentException.class, () -> {
            User.checkUserName("test;123");
        }, "Tester at semikolon gir feilmelding");
        assertThrows(IllegalArgumentException.class, () -> {
            User.checkUserName("");
        }, "Tester at ingenting gir feilmelding");
        assertThrows(IllegalArgumentException.class, () -> {
            User.checkUserName("hallo$");
        }, "Tester at andre tegn gir feilmelding");
        assertThrows(IllegalArgumentException.class, () -> {
            User.checkPassword("123456789123456789123");
        }, "Tester at passord som er 21 tegn langt gir feilmelding");
    }

    @Test
    @DisplayName("Tester at det ikke er lov å instansiere en bruker med tomt brukernavn/passord")
    public void testUserConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            user2 = new User("", "abc123", "test_user");
        }, "Tester tomt brukernavn og gyldig passord");
        assertThrows(IllegalArgumentException.class, () -> {
            user2 = new User("heisann", "", "test_user");
        }, "Tester gyldig brukernavn og tomt passord");
        assertThrows(IllegalArgumentException.class, () -> {
            user2 = new User("", "", "test_user");
        }, "Tester tomt brukernavn og passord");
    }

    @AfterAll
    public void teardown() throws FileNotFoundException {
        UserHandler.getInstance().getFile("test_user").delete();
    }
}
