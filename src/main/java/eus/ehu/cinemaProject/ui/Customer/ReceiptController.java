package eus.ehu.cinemaProject.ui.Customer;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.Seat;
import eus.ehu.cinemaProject.domain.users.Customer;
import eus.ehu.cinemaProject.ui.UIState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.List;
import java.util.ResourceBundle;

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

    @FXML
    private ImageView filmImage;

    private double seatPrices = 0.0;
    private double totalSnacks = 0.0;
    private String seatInfo;
    private UIState uiState = UIState.getInstance();
    private BlFacadeImplementation bl = BlFacadeImplementation.getInstance();

    private final ResourceBundle bundle = uiState.getBundle();

    @FXML
    public void initialize() {
        filmImage.setImage(new javafx.scene.image.Image(uiState.getFilm().getImagePath()));        uiState = UIState.getInstance();
        movieInfo.setText(uiState.getSelectedShowtime().toString2());
        double snackPrice = uiState.getSnackprice(); // Obtén el precio de los snacks desde uiState
        seatPrices = 0.0;
        seatInfo = bundle.getString("selectedSeats") + ":\n";

        List<Seat> seats = uiState.getSelectedSeats();
        for (Seat seat : seats) {
            seatInfo = seatInfo + seat.toString() + "\n";
            seatPrices += seat.getPrice();
        }

        if (uiState.getSummary() == null) {
            receipt.setText(seatInfo + "\n" + "-----------------------\n" + "\n" +
                    String.format(bundle.getString("seatsTotal"), seatPrices));
        } else {
            receipt.setText(seatInfo + "\n" + uiState.getSummary() + "\n" +
                    String.format(bundle.getString("seatsTotal"), seatPrices));
        }
        totalPrize.setText(String.format("€%.2f", seatPrices + snackPrice)); // Usa snackPrice aquí
    }

    @FXML
    private void goToSnacksSelect(ActionEvent event) {
        uiState.setCurrentView("orderfood.fxml");
    }

    @FXML
    private void goToSeatSelect(ActionEvent event) {
        uiState.setCurrentView("seatSelection.fxml");
        if (uiState.getSeatSelectionController() != null) {
            uiState.getSeatSelectionController().reselectSeats();
        }
    }

    @FXML
    private void proceedPaymentButton(ActionEvent event) {
        bl.createPurchaseReceipt((Customer) uiState.getUser(), uiState.getSelectedShowtime(), uiState.getSelectedSeats());
        System.out.println(bundle.getString("proceedButton"));
        uiState.getMenuController().clearCache();
        if(uiState.getWorkerEmail() != null)
            uiState.setCurrentView("workerMenu.fxml");
        else
            uiState.setCurrentView("MovieList.fxml");
    }

    public void setSnackData(String snackSummary, double snackPrice) {
        receipt.setText(snackSummary);
        snackPrice += seatPrices;
        totalPrize.setText(String.format("€%.2f", snackPrice));
        totalSnacks = snackPrice;
    }
}