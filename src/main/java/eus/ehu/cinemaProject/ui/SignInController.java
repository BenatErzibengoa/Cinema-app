package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SignInController {
    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label outputText;

    BlFacadeImplementation bl;

    @FXML
    void initialize(){
        bl = BlFacadeImplementation.getInstance();
        outputText.setVisible(false);
    }


    @FXML
    void login(){
        if(checkLogin()) {
            if(bl.login(usernameField.getText(), passwordField.getText()) != null){
                outputText.setText("Login successful! Welcome %s".formatted(usernameField.getText()));
                outputText.setStyle("-fx-text-fill: green;");
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


    public boolean checkLogin(){
        return !usernameField.getText().isEmpty()  && !passwordField.getText().isEmpty();
    }
}
