package eus.ehu.cinemaProject.ui;
import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.users.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.concurrent.TimeUnit;

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
            outputText.setText("Please enter an email address.");
            outputText.setStyle("-fx-text-fill: red;");
        } else {
            System.out.println("Proceeding with email: " + emailField.getText());
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
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            uiState.setCurrentView("MovieList.fxml");
        }

    }



}
