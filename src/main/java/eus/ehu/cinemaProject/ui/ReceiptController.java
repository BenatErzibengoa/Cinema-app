package eus.ehu.cinemaProject.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ReceiptController {

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

    @FXML
    void goToSeatSelect(ActionEvent event) {
        System.out.println("goToSeatSelect");
    }

    @FXML
    void goToSnacksSelect(ActionEvent event) {
        System.out.println("goToSnacksSelect");
    }

    @FXML
    void proceedPaymentButton(ActionEvent event) {
        System.out.println("proceedPaymentButton");
    }


}
