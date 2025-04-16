package eus.ehu.cinemaProject.ui;
import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MenuController {

    @FXML
    private BorderPane contentPane;

    BlFacadeImplementation bl;


    @FXML void initialize(){
        // Load the initial content and it changes when the view changes
        uiState.currentViewProperty().addListener((obs, oldView, newView) -> {
            loadContent(newView);
        });
        bl = BlFacadeImplementation.getInstance();
        loadContent("seatSelection.fxml");
    }


    @FXML
    void loginPane(ActionEvent event) {
        uiState.setCurrentView("signin.fxml");
    }

    @FXML
    void registerPane(ActionEvent event) {

        uiState.setCurrentView("signup.fxml");
    }

    // Reference to the UIState
    private final UIState uiState = UIState.getInstance();

    private Map<String, AnchorPane> contentCache = new HashMap<>();

    private void loadContent(String fxmlFile) {
        try {
            // Check if content is already cached
            AnchorPane content = contentCache.get(fxmlFile);
            if (content == null) {
                // If not cached, load it and store in cache
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                content = loader.load();
                contentCache.put(fxmlFile, content);
            }
            contentPane.setCenter(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}