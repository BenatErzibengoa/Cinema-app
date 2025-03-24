package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignUpController {

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordField2;

    @FXML
    private TextField usernameField;

    BlFacadeImplementation bl;

    @FXML
    private Label message;


    @FXML
    void signUp(ActionEvent event) {
        message.setText("");

        if (!validateEmail(usernameField.getText())) {
            message.setText("Invalid email");
            message.setStyle("-fx-text-fill: red; -fx-alignment: center;");
            usernameField.clear();
        } else if (passwordField.getText().isEmpty()||passwordField.getText().length()<5) {
            message.setText("Enter a valid password");
            message.setStyle("-fx-text-fill: red; -fx-alignment: center;");
            passwordField.clear();
            passwordField2.clear();
        }else if (!passwordField.getText().equals(passwordField2.getText())) {
            message.setText("Passwords do not match");
            message.setStyle("-fx-text-fill: red; -fx-alignment: center;");
            passwordField.clear();
            passwordField2.clear();
        } else {
                bl.signUp(usernameField.getText(), passwordField.getText());
                message.setText("You've successfully registered!");
                message.setStyle("-fx-text-fill: green; -fx-alignment: center;");
        }
    }

    @FXML void initialize(){
        bl = BlFacadeImplementation.getInstance();
    }

    private boolean validateEmail(String email){
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }
}
