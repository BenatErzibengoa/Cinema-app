package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.domain.Seat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
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
    private TextArea movieInfo;

    @FXML
    private Button orderSnacks;

    @FXML
    private Button proceedPayment;

    @FXML
    private TextArea receipt;

    @FXML
    private TextField totalPrize;

    private double seatPrices = 0.0;
    private double totalSnacks = 0.0;
    String seatInfo;
    private UIState uiState = UIState.getInstance();

    private String summary="";
    private double foodprice=0.0;
    private double snackprice=0.0;



    @FXML
    public void initialize() {
        uiState = UIState.getInstance();
        movieInfo.setText(uiState.getSelectedShowtime().toString2());
        snackprice = uiState.getSnackprice();
        seatPrices = 0.0;
        seatInfo = "Selected seats:\n";

        List<Seat> seats = uiState.getSelectedSeats();
        for (Seat seat : seats) {
            seatInfo = seatInfo + seat.toString() + "\n";
            seatPrices += seat.getPrice();
        }
        receipt.setText(seatInfo + "\n" + uiState.getSummary());
        totalPrize.setText(String.format("€%.2f", seatPrices + snackprice));
    }

    @FXML
    private void goToSnacksSelect(javafx.event.ActionEvent event) {
        uiState.setCurrentView("orderfood.fxml");
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

