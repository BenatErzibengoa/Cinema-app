package eus.ehu.cinemaProject.ui.Admin;
import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.users.Admin;
import eus.ehu.cinemaProject.domain.users.User;
import eus.ehu.cinemaProject.ui.UIState;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class AdminMainController {

    private final UIState uiState = UIState.getInstance();
    private final BlFacadeImplementation bl = BlFacadeImplementation.getInstance();
    private final ResourceBundle bundle = ResourceBundle.getBundle("eus.ehu.cinemaProject.ui.Language", Locale.getDefault());

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
        String name = (user instanceof Admin) ? user.getName() : bundle.getString("default.admin.name");
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern(bundle.getString("date.format")));

        welcomeLabel.setText(MessageFormat.format(bundle.getString("label.welcome"), name, today));

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
        movieCountLabel.setText(MessageFormat.format(bundle.getString("label.movie.count"), bl.getAllFilms().size()));

        long workerCount = bl.getAllWorkers().stream()
                .filter(w -> !(w instanceof Admin))
                .count();
        workerCountLabel.setText(MessageFormat.format(bundle.getString("label.worker.count"), workerCount));

        showtimeCountLabel.setText(MessageFormat.format(bundle.getString("label.showtime.count"), bl.getAllShowtimes().size()));
    }
}
