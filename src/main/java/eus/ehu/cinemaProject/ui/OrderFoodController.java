package eus.ehu.cinemaProject.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class OrderFoodController {

    @FXML private Spinner<Integer> popcornNum;
    @FXML private Spinner<Integer> nachosNum;
    @FXML private Spinner<Integer> sodaNum;
    @FXML private Spinner<Integer> candyNum;
    @FXML private Spinner<Integer> hotdogNum;

    private final UIState uiState = UIState.getInstance();

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
    private void handleSave(javafx.event.ActionEvent event) {
        int popcorn = popcornNum.getValue();
        int nachos = nachosNum.getValue();
        int soda = sodaNum.getValue();
        int candy = candyNum.getValue();
        int hotdog = hotdogNum.getValue();

        double totalPrice = popcorn * 4.0 + nachos * 3.5 + soda * 2.5 +
                candy * 1.5 + hotdog * 3.0;

        StringBuilder summary = new StringBuilder();
        if (popcorn > 0) summary.append("Popcorn: ").append(popcorn).append("\n");
        if (nachos > 0) summary.append("Nachos: ").append(nachos).append("\n");
        if (soda > 0) summary.append("Soda: ").append(soda).append("\n");
        if (candy > 0) summary.append("Candy: ").append(candy).append("\n");
        if (hotdog > 0) summary.append("Hotdog: ").append(hotdog).append("\n");
        summary.append("-----------------------\n");
        summary.append(String.format("Snacks total: €%.2f", totalPrice));

        uiState.setCurrentView("receipt.fxml");
        uiState.setSnackprice(totalPrice);
        uiState.setSummary(summary.toString());

       /** // Volver a la vista original y pasar datos
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("receipt.fxml"));
            Parent root = loader.load();

            ReceiptController controller = loader.getController();
            controller.setSnackData(summary.toString(), totalPrice);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }**/
    }
}
