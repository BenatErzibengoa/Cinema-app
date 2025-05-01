package eus.ehu.cinemaProject.ui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WorkerMenuController {
    @FXML
    private BorderPane menuPane;

    UIState uiState = UIState.getInstance();
    private Map<String, Pane> contentCache = new HashMap<>();

    private void loadContent(String fxmlFile){
        try{
            // Check if content is already cached
            Pane content = contentCache.get(fxmlFile);
            if (content == null) {
                // If not cached, load it and store in cache
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                content = loader.load();

                contentCache.put(fxmlFile, content);
            }
            menuPane.setCenter(content);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buyForCustomersClick(ActionEvent event) {
        loadContent("buyForCustomers.fxml");
    }

    @FXML
    void reservationsClick(ActionEvent event) {
        loadContent("reservationsForWorkers.fxml");
    }



}
