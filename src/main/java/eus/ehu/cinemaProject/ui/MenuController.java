package eus.ehu.cinemaProject.ui;
import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.users.Admin;
import eus.ehu.cinemaProject.domain.users.Customer;
import eus.ehu.cinemaProject.domain.users.User;
import eus.ehu.cinemaProject.domain.users.Worker;
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
            if (isNowLoggedIn) {
                User user = uiState.getUser();
                loginButton.setVisible(false);
                registerButton.setVisible(false);
                receiptsButton.setVisible(true);
                if(user instanceof Customer)
                    receiptsButton.setVisible(true);
                else if((user instanceof Worker)&&!(user instanceof Admin)){
                    receiptsButton.setVisible(false);
                    loadContent("workerMenu.fxml");
                } else {
                    receiptsButton.setVisible(false);
                    loadContent("adminMain.fxml");
            }} else {
                loginButton.setVisible(true);
                registerButton.setVisible(true);
                receiptsButton.setVisible(false);
            }
        });
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
            System.out.println("Loaded content from " + fxmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    private void loadContent(String fxmlFile) {
        try {
            contentCache.remove(fxmlFile); //Théo - Remove view if already in cache
            // Check if content is already cached
            Pane content = contentCache.get(fxmlFile);
            if (content == null) {
                // If not cached, load it and store in cache
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                content = loader.load();

                if (loader.getController() instanceof MovieListController) { //Théo
                    ((MovieListController) loader.getController()).setBusinessLogic(bl);
                }

                contentCache.put(fxmlFile, content);
            }
            contentPane.setCenter(content);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
}