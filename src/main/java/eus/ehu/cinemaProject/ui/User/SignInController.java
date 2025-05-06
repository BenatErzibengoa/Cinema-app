package eus.ehu.cinemaProject.ui.User;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.users.Admin;
import eus.ehu.cinemaProject.domain.users.User;
import eus.ehu.cinemaProject.domain.users.Worker;
import eus.ehu.cinemaProject.ui.UIState;
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
                uiState.setCustomerEmail(null);
                uiState.setWorkerEmail(emailField.getText());
                uiState.setUser(null);
                uiState.setLoggedIn(true);

                if(user instanceof Admin){
                    uiState.setCurrentView("adminMain.fxml");
                } else if (user instanceof Worker) {
                    uiState.setCurrentView("workerMenu.fxml");
                }else{
                    uiState.setCustomerEmail(emailField.getText());
                    uiState.setWorkerEmail(null);
                    uiState.setUser(user);
                    uiState.setCurrentView("MovieList.fxml");
                }

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
