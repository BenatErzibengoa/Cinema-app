package eus.ehu.cinemaProject.ui.Admin;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.users.Admin;
import eus.ehu.cinemaProject.domain.users.User;
import eus.ehu.cinemaProject.ui.UIState;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminMainController {

    private final UIState uiState = UIState.getInstance();
    private final BlFacadeImplementation bl = BlFacadeImplementation.getInstance();

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label movieCountLabel;

    @FXML
    private Label workerCountLabel;

    @FXML
    private Label showtimeCountLabel;

    @FXML
    public void initialize() {
        User user = uiState.getUser();  // Assuming logged-in user is stored in UIState
        String name = (user instanceof Admin) ? user.getName() : "Admin";
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
        welcomeLabel.setText("Welcome, " + name + "     [ " + today + "]");

        refreshStats();

        uiState.currentViewProperty().addListener((obs, oldView, newView) -> {
            if ("adminMain.fxml".equals(newView)) {
                refreshStats();
            }
        });
    }

    @FXML
    void manageWorkers(javafx.event.ActionEvent event) {
        uiState.setCurrentView("manageWorkers.fxml");
    }

    @FXML
    void addMovies(javafx.event.ActionEvent event) {
        uiState.setCurrentView("addMovies.fxml");
    }

    @FXML
    void addShowtimes(javafx.event.ActionEvent event) {
        uiState.setCurrentView("addShowTimes.fxml");
    }

    private void refreshStats() {
        // Update movie count
        movieCountLabel.setText("Total Movies: " + bl.getAllFilms().size());

        // Update worker count (exclude admins)
        long workerCount = bl.getAllWorkers().stream()
                .filter(w -> !(w instanceof Admin))
                .count();
        workerCountLabel.setText("Workers Employed: " + workerCount);

        // Update showtime count
        showtimeCountLabel.setText("Showtimes Scheduled: " + bl.getAllShowtimes().size());
    }
}
