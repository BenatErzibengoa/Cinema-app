package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.users.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SignInController {
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;

    @FXML
    private Label outputText;

    BlFacadeImplementation bl;

    // Reference to the UIState
    private final UIState uiState = UIState.getInstance();

    @FXML
    void initialize(){
        bl = BlFacadeImplementation.getInstance();
        outputText.setVisible(false);
    }


    @FXML
    void login(){
        if(!(emailField.getText().isEmpty() || passwordField.getText().isEmpty())){
            User user = bl.login(emailField.getText(), passwordField.getText());
            if(user != null){
                outputText.setText("Login successful! Welcome %s".formatted(emailField.getText()));
                outputText.setStyle("-fx-text-fill: green;");

                //Pass the email to the UIState
                uiState.setCustomerEmail(emailField.getText());
                uiState.setUser(user);
                uiState.setLoggedIn(true);

                uiState.setCurrentView("MovieList.fxml");
            }
            else{
                outputText.setText("Invalid credentials. Please try again");
                outputText.setStyle("-fx-text-fill: red;");
            }
        }
        else{
            outputText.setText("Fill all fields");
            outputText.setStyle("-fx-text-fill: red;");

        }
        outputText.setVisible(true);
    }
}
