package eus.ehu.cinemaProject.ui;
import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MenuController {

    @FXML
    private BorderPane contentPane;

    @FXML
    private Button receiptsButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Text titleText; //Théo

    BlFacadeImplementation bl;

    @FXML //Théo
    private void showMovieList() {
        uiState.setSummary("");
        uiState.setSnackprice(0.0);
        loadContent("MovieList.fxml");
    }

    @FXML void initialize(){
        bl = BlFacadeImplementation.getInstance(); //Théo
        showMovieList(); //Théo


        // Load the initial content and it changes when the view changes
        uiState.currentViewProperty().addListener((obs, oldView, newView) -> {
            loadContent(newView);
        });

        uiState.loggedInProperty().addListener((obs, wasLoggedIn, isNowLoggedIn) -> {
            loginButton.setVisible(!isNowLoggedIn);
            registerButton.setVisible(!isNowLoggedIn);
            receiptsButton.setVisible(isNowLoggedIn);
        });

        //loadContent("seatSelection.fxml");
    }


    @FXML
    void loginPane(ActionEvent event) {
        loadContent("signin.fxml");
    }

    @FXML
    void registerPane(ActionEvent event) {
        loadContent("signup.fxml");
    }

    @FXML
    void receiptsPane(ActionEvent event) {
        loadContent("userReceipts.fxml");
    }

    // Reference to the UIState
    private final UIState uiState = UIState.getInstance();

    private Map<String, Pane> contentCache = new HashMap<>();

    private void loadContent(String fxmlFile) {
        try {
            // Check if content is already cached
            Pane content = contentCache.get(fxmlFile);
            if (content == null) {
                // If not cached, load it and store in cache
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                content = loader.load();
                contentCache.put(fxmlFile, content);
                if (loader.getController() instanceof MovieListController) {
                    ((MovieListController) loader.getController()).setBusinessLogic(bl);
                }
            }
            contentPane.setCenter(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}