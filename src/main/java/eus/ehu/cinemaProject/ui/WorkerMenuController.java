package eus.ehu.cinemaProject.ui;
import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.users.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class WorkerMenuController {

    @FXML
    private TextField emailField;

    @FXML
    private Label outputText;

    BlFacadeImplementation bl= BlFacadeImplementation.getInstance();
    UIState uiState = UIState.getInstance();
    ResourceBundle bundle = uiState.getBundle();

    @FXML
    private void initialize() {
        emailField.setVisible(true);
        outputText.setVisible(true);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    @FXML
    void proceedBuying(ActionEvent event) {
        if(emailField.getText().isEmpty()){
            System.out.println("Please enter an email address.");
            outputText.setText(bundle.getString("error.invalid.email"));
            outputText.setStyle("-fx-text-fill: red;");
        } else {
            System.out.println(bundle.getString("proceedEmail")+" " + emailField.getText());
            User cust = bl.getUserByEmail(emailField.getText());
            if(cust == null){
                outputText.setText(bundle.getString("userNotFound"));
                outputText.setStyle("-fx-text-fill: red;");
                return;
            }

            uiState.setCustomerEmail(emailField.getText());
            uiState.setUser(cust);


            outputText.setText(bundle.getString("proceedForCust")+emailField.getText());
            outputText.setStyle("-fx-text-fill: green;");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            uiState.setCurrentView("MovieList.fxml");
        }

    }



}
