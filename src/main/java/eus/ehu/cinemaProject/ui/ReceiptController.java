package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.domain.Seat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.util.List;

public class ReceiptController {

    @FXML
    private AnchorPane contentArea;

    @FXML
    private Button goBack;

    @FXML
    private TextField movieInfo;

    @FXML
    private Button orderSnacks;

    @FXML
    private Button proceedPayment;

    @FXML
    private TextField receipt;

    @FXML
    private TextField totalPrize;

    private double seatPrices = 0.0;
    private double totalSnacks = 0.0;
    String seatInfo;
    private UIState uiState = UIState.getInstance();


    @FXML
    public void initialize() {
        List<Seat> seats = uiState.getSelectedSeats();
        for (Seat seat : seats) {
            seatInfo = seatInfo + seat.toString();
            seatPrices += seat.getPrice();
        }

        receipt.setText(seatInfo);
        totalPrize.setText(String.format("€%.2f", seatPrices));
    }

    @FXML
    private void goToSnacksSelect(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("orderfood.fxml"));
            Parent root = loader.load();

            OrderFoodController controller = loader.getController();
            controller.setReceiptController(this);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToSeatSelect(ActionEvent event){
        uiState.setCurrentView("seatSelection.fxml");
    }

    @FXML
    private void proceedPaymentButton(ActionEvent event){
        System.out.println("proceed");
    }

    public void setSnackData(String snackSummary, double snackPrice) {
        receipt.setText(snackSummary);
        snackPrice += seatPrices;
        totalPrize.setText(String.format("€%.2f", snackPrice));
        totalSnacks = snackPrice;
    }

}

