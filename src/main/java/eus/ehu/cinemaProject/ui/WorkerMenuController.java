package eus.ehu.cinemaProject.ui;
import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.users.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WorkerMenuController {

    @FXML
    private TextField emailField;

    @FXML
    private Label outputText;

    BlFacadeImplementation bl= BlFacadeImplementation.getInstance();
    UIState uiState = UIState.getInstance();

    @FXML
    private void initialize() {
        emailField.setVisible(true);
        outputText.setVisible(true);
    }

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

            uiState.setCustomerEmail(emailField.getText());
            uiState.setUser(cust);


            outputText.setText("Purchase process initiated for %s".formatted(emailField.getText()));
            outputText.setStyle("-fx-text-fill: green;");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            uiState.setCurrentView("MovieList.fxml");
        }

    }



}
