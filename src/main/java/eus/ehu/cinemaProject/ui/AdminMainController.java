package eus.ehu.cinemaProject.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AdminMainController {

    private final UIState uiState = UIState.getInstance();
    @FXML
    void manageWorkers(ActionEvent event) {
        uiState.setCurrentView("manageWorkers.fxml");
    }

}

