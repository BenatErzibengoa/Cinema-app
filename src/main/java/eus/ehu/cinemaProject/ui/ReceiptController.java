package eus.ehu.cinemaProject.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class ReceiptController {

    @FXML private TextField movieInfo;
    @FXML private TextField receipt;
    @FXML private TextField totalPrize;

    private double totalSnacks = 0.0;

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
        System.out.println("Going to seat selection");
    }

    @FXML
    private void proceedPaymentButton(ActionEvent event){
        System.out.println("proceed");
    }

    public void setSnackData(String snackSummary, double snackPrice) {
        receipt.setText(snackSummary);
        totalPrize.setText(String.format("€%.2f", snackPrice));
        totalSnacks = snackPrice;
    }

}

