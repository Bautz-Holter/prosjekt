package LittleStockMarket;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public interface iController {
    default void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("En feil har oppstått!");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
