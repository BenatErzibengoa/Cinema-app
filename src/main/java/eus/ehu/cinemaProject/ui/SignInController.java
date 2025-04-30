package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.configuration.PasswordHasher;
import eus.ehu.cinemaProject.domain.users.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Locale;
import java.util.ResourceBundle;

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
    ResourceBundle bundle = uiState.getBundle();


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
                outputText.setText(bundle.getString("successfulLogin")+emailField.getText());
                outputText.setStyle("-fx-text-fill: green;");

                //Pass the email to the UIState
                uiState.setEmail(emailField.getText());
                uiState.setUser(user);
                uiState.setLoggedIn(true);

                uiState.setCurrentView("MovieList.fxml");
            }
            else{
                outputText.setText(bundle.getString("loginError"));
                outputText.setStyle("-fx-text-fill: red;");
            }
        }
        else{
            outputText.setText(bundle.getString("emptyFields"));
            outputText.setStyle("-fx-text-fill: red;");

        }
        outputText.setVisible(true);
    }
}
