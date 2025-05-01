package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.users.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class BuyForCustomersController {

    @FXML
    private TextField emailField;

    @FXML
    private Label outputText;

    BlFacadeImplementation bl= BlFacadeImplementation.getInstance();
    UIState uiState = UIState.getInstance();

    @FXML
    void proceedBuying(ActionEvent event) {
        if(emailField.getText().isEmpty()){
            outputText.setText("Please enter an email address.");
            outputText.setStyle("-fx-text-fill: red;");
        } else {
            User cust = bl.getUserByEmail(emailField.getText());
            if(cust == null){
                outputText.setText("User not found.");
                outputText.setStyle("-fx-text-fill: red;");
                return;
            }

            uiState.setEmail(emailField.getText());



            // Here you would typically call a method to proceed with the purchase
            // For example: bl.proceedPurchase(emailField.getText());
            outputText.setText("Purchase process initiated for %s".formatted(emailField.getText()));
            outputText.setStyle("-fx-text-fill: green;");
        }

    }

}