package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
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
            if(bl.login(emailField.getText(), passwordField.getText()) != null){
                outputText.setText("Login successful! Welcome %s".formatted(emailField.getText()));
                outputText.setStyle("-fx-text-fill: green;");

                //Pass the email to the UIState
                uiState.setEmail(emailField.getText());
                uiState.setLoggedIn(true);

                //Remove next comment when Theo finishes his work
                //uiState.setCurrentView("films.fxml");
            }
            else{
                outputText.setText("Invalid credentials. Please try again");
                outputText.setStyle("-fx-text-fill: red;");
            }
        }
        else{
            outputText.setText("Fill all fields");
            outputText.setStyle("-fx-text-fill: grey;");
        }
        outputText.setVisible(true);
    }
}
