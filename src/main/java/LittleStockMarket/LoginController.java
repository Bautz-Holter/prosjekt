package LittleStockMarket;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginController implements Initializable, iController {
    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Button login, newUser;

    @FXML
    private Text infoText;

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void handleNewUser() {
        try {
            String inputUsername = userName.getText();
            String inputPassword = password.getText();
            if (UserHandler.getInstance().checkUserNameExists(inputUsername, "usernames")) {
                throw new IllegalArgumentException("Username taken");
            }
            User.checkUserName(inputUsername);
            User.checkPassword(inputPassword);
            String[] userInfo = { inputUsername.toLowerCase(), inputPassword.toLowerCase() };
            UserHandler.getInstance().writeFile("usernames", userInfo);
            userName.clear();
            password.clear();
            infoText.setText("Vennligst skriv inn brukernavn og passord, og trykk 'Logg inn'");
            infoText.setLayoutX(30);
        } catch (IllegalArgumentException | IOException e) {
            showErrorMessage(e.getMessage());
        }
    }

    public void handleLogin() {
        try {
            String inputUsername = userName.getText();
            String inputPassword = password.getText();
            User user = new User(inputUsername.toLowerCase(), inputPassword.toLowerCase());
            UserHolder holder = UserHolder.getInstance();
            holder.setUser(user);
            FXMLLoader loader = new FXMLLoader(StockMarketApp.class.getResource("LittleStockMarket.fxml"));
            userName.getScene().setRoot(loader.load());
        } catch (IllegalArgumentException | IOException e) {
            showErrorMessage(e.getMessage());
        }
    }
}
