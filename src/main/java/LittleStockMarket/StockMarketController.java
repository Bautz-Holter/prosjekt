package LittleStockMarket;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class StockMarketController implements Initializable, iController {

    @FXML
    private Label capital, value, index;

    @FXML
    private Text userID;

    @FXML
    private TableView<OwnedStock> myStocksTable;

    @FXML
    private TableColumn<OwnedStock, String> myStockNameColumn;
    @FXML
    private TableColumn<OwnedStock, Double> myStockVolumeColumn;
    @FXML
    private TableColumn<OwnedStock, Double> myStockPriceColumn;
    @FXML
    private TableColumn<OwnedStock, String> myStockPercChangeColumn;

    @FXML
    private TableView<Stock> availableStocksTable;

    @FXML
    private TableColumn<Stock, String> availableNameColumn;
    @FXML
    private TableColumn<Stock, Double> availablePriceColumn;
    @FXML
    private TableColumn<Stock, String> availablePercChangeColumn;

    @FXML
    private TextField buyStock, buyVolume;

    @FXML
    private Button buy;

    @FXML
    private TextField sellStock, sellVolume;

    @FXML
    private Button sell;

    @FXML
    private Button save;

    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;

    private User user;
    private Market myMarket;
    private Portfolio myPortfolio1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserHolder holder = UserHolder.getInstance();
        this.user = holder.getUser();
        this.myMarket = this.user.getMarket();
        this.myPortfolio1 = this.user.getPortfolio();

        ArrayList<OwnedStock> myStocks = myPortfolio1.getStocks();
        Double cap = myPortfolio1.getCapital();
        Double val = myPortfolio1.getValue();
        Double index = myMarket.getIndex();

        userID.setText("Brukernavn: " + this.user.getUserName());
        initAvailableStocks(myMarket, index);
        initMyStocks(myStocks);
        initValueAndCapital(cap, val);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myMarket.updatePrices();
                myPortfolio1.updatePortfolio();
                ArrayList<OwnedStock> myStocks = myPortfolio1.getStocks();
                Double cap = myPortfolio1.getCapital();
                Double val = myPortfolio1.getValue();
                Double indx = myMarket.getIndex();
                initAvailableStocks(myMarket, indx);
                initMyStocks(myStocks);
                initValueAndCapital(cap, val);
                userID.setText("Brukernavn: " + user.getUserName());
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    private void initValueAndCapital(Double cap, Double val) {
        capital.setText(cap.toString());
        value.setText(val.toString());
    }

    private void initAvailableStocks(Market myMarket, Double indx) {
        index.setText("Markedsindeks: " + indx.toString());
        ArrayList<Stock> availableStocks = myMarket.getAvailableStocks();

        availableNameColumn.setCellValueFactory(new PropertyValueFactory<>("stockName"));

        availablePriceColumn.setCellValueFactory(new PropertyValueFactory<>("stockPrice"));

        availablePercChangeColumn.setCellValueFactory(new PropertyValueFactory<>("stockChangePercent"));

        availableStocksTable.setPlaceholder(new Label("No stocks to display"));

        availableStocksTable.getItems().setAll(availableStocks);

    }

    private void initMyStocks(ArrayList<OwnedStock> myStocks) {

        myStockNameColumn.setCellValueFactory(new PropertyValueFactory<>("stockName"));

        myStockVolumeColumn.setCellValueFactory(new PropertyValueFactory<>("stockAmount"));

        myStockPriceColumn.setCellValueFactory(new PropertyValueFactory<>("stockPrice"));

        myStockPercChangeColumn.setCellValueFactory(new PropertyValueFactory<>("stockChangePercent"));

        myStocksTable.setPlaceholder(new Label("No stocks to display"));

        myStocksTable.getItems().setAll(parseMyStockList(myStocks));
    }

    private ArrayList<OwnedStock> parseMyStockList(ArrayList<OwnedStock> myStocks) {
        if (myStocks.size() == 0) {
            myStocksTable.setPlaceholder(new Label("No stocks to display"));
        }
        ArrayList<OwnedStock> example = new ArrayList<>();
        for (int i = 0; i < myStocks.size(); i++) {
            if (!example.contains(myStocks.get(i))) {
                example.add(myStocks.get(i));
            }
        }
        return example;
    }

    public void handleSellButtonClick() {
        String stockNameWantedSold = sellStock.getText();
        String stockAmountWantedSold = sellVolume.getText();
        if (stockAmountWantedSold == "")
            showErrorMessage("You need to write an amount");
        if (stockNameWantedSold == "")
            showErrorMessage("You need to write a name");
        try {
            Integer amount = Integer.valueOf(stockAmountWantedSold);
            myPortfolio1.sellStock(stockNameWantedSold, amount);
        } catch (NumberFormatException e) {
            showErrorMessage("You need to write an integer");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            showErrorMessage(e.getMessage());
        }
    }

    public void handleBuyButtonClick() {
        String stockNameWantedBought = buyStock.getText();
        String stockAmountWantedBought = buyVolume.getText();
        if (stockNameWantedBought == "")
            showErrorMessage("You need to write a name");
        if (stockAmountWantedBought == "")
            showErrorMessage("You need to write an amount");
        try {
            Integer amount = Integer.valueOf(stockAmountWantedBought);
            myPortfolio1.buyStock(stockNameWantedBought, amount);
        } catch (NumberFormatException e) {
            showErrorMessage("You need to write an integer");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            showErrorMessage(e.getMessage());
        }
    }

    public void handleSave() throws Exception {
        PortfolioHandler handler = PortfolioHandler.getInstance();
        handler.writeFile(this.user.getUserName(), myPortfolio1);
    }
}
