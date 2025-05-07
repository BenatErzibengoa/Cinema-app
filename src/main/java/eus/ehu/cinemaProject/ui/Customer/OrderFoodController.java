package eus.ehu.cinemaProject.ui.Customer;

import eus.ehu.cinemaProject.ui.UIState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;


import java.io.IOException;
import java.util.ResourceBundle;

public class OrderFoodController {

    @FXML private Spinner<Integer> popcornNum;
    @FXML private Spinner<Integer> nachosNum;
    @FXML private Spinner<Integer> sodaNum;
    @FXML private Spinner<Integer> candyNum;
    @FXML private Spinner<Integer> hotdogNum;

    private final UIState uiState = UIState.getInstance();
    private final ResourceBundle bundle = uiState.getBundle();

    private ReceiptController receiptController;

    public void setReceiptController(ReceiptController controller) {
        this.receiptController = controller;
    }

    @FXML
    private void initialize() {
        popcornNum.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        nachosNum.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        sodaNum.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        candyNum.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        hotdogNum.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
    }

    @FXML
    private void handleSave(ActionEvent event) {
        int popcorn = popcornNum.getValue();
        int nachos = nachosNum.getValue();
        int soda = sodaNum.getValue();
        int candy = candyNum.getValue();
        int hotdog = hotdogNum.getValue();

        double totalPrice = popcorn * 4.0 + nachos * 3.5 + soda * 2.5 +
                candy * 1.5 + hotdog * 3.0;

        StringBuilder summary = new StringBuilder();
        if (popcorn > 0) summary.append(bundle.getString("popCorn")).append(": ").append(popcorn).append("\n");
        if (nachos > 0) summary.append(bundle.getString("nacho")).append(": ").append(nachos).append("\n");
        if (soda > 0) summary.append(bundle.getString("soda")).append(": ").append(soda).append("\n");
        if (candy > 0) summary.append(bundle.getString("candy")).append(": ").append(candy).append("\n");
        if (hotdog > 0) summary.append(bundle.getString("hotDog")).append(": ").append(hotdog).append("\n");
        summary.append("-----------------------\n");
        summary.append(String.format(bundle.getString("snacksTotal"), totalPrice));

        uiState.setSnackprice(totalPrice);
        uiState.setSummary(summary.toString());
        uiState.getMenuController().clearReceipt();
        uiState.setCurrentView("receipt.fxml");
    }
}