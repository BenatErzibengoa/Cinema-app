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
    private TextField emailField;

    @FXML
    private Label message;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordField2;

    @FXML
    private TextField surenameField;

    BlFacadeImplementation bl;



    @FXML
    void signUp(ActionEvent event) {
        message.setText("");
        message.setStyle("-fx-text-fill: red; -fx-alignment: center;");

        if(nameField.getText().isEmpty()||surenameField.getText().isEmpty()||emailField.getText().isEmpty()||passwordField.getText().isEmpty()||passwordField2.getText().isEmpty()){
            message.setText("Fill all fields");

        } else if(!validateEmail(emailField.getText())){
            message.setText("Enter a valid email");
            emailField.clear();

        } else if (!passwordField.getText().equals(passwordField2.getText())) {
            message.setText("Passwords do not match");

        } else if (passwordField.getText().length()<8) {
            message.setText("Password must be at least 8 characters long");

        }else {
            bl.signUp(nameField.getText(), surenameField.getText(), emailField.getText(), passwordField.getText());
            message.setText("You've successfully registered! Welcome "+nameField.getText()+"!");
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
