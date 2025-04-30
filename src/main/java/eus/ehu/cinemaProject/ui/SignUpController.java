package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Locale;
import java.util.ResourceBundle;

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
    private TextField surnameField;

    BlFacadeImplementation bl;
    Locale locale = Locale.forLanguageTag("es");
    ResourceBundle bundle = ResourceBundle.getBundle("eus.ehu.cinemaProject.ui.Language", locale);

    // Reference to the UIState
    private final UIState uiState = UIState.getInstance();


    @FXML
    void signUp(ActionEvent event) {
        message.setText("");
        message.setStyle("-fx-text-fill: red; -fx-alignment: center;");

        if(nameField.getText().isEmpty()||surnameField.getText().isEmpty()||emailField.getText().isEmpty()||passwordField.getText().isEmpty()||passwordField2.getText().isEmpty()){
            message.setText(bundle.getString("emptyFields"));

        } else if(!validateEmail(emailField.getText())){
            message.setText(bundle.getString("invalidEmail"));
            emailField.clear();

        } else if (!passwordField.getText().equals(passwordField2.getText())) {
            message.setText(bundle.getString("noMatch"));

        } else if (passwordField.getText().length()<5) {
            message.setText(bundle.getString("shortPassword"));

        } else if(bl.getUserByEmail(emailField.getText()) != null){
            message.setText(bundle.getString("repeatedEmail")+emailField.getText());

        }else {
            bl.signUp(emailField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText());
            message.setText(bundle.getString("successfulRegister")+nameField.getText()+"!");
            message.setStyle("-fx-text-fill: green; -fx-alignment: center;");

            //Pass the email to the UIState
            uiState.setEmail(emailField.getText());
            uiState.setUser(bl.getUserByEmail(emailField.getText()));
            uiState.setLoggedIn(true);
            uiState.setCurrentView("MovieList.fxml");
        }

    }

    @FXML void initialize(){
        bl = BlFacadeImplementation.getInstance();
    }

    private boolean validateEmail(String email){
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }
}
