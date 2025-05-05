package eus.ehu.cinemaProject.ui.Admin;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.dataAccess.FilmDataFetcher;
import eus.ehu.cinemaProject.domain.Film;
import eus.ehu.cinemaProject.ui.UIState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.format.DateTimeFormatter;

public class AddMovieController {


    @FXML
    private Button aMovieButton;

    @FXML
    private Label movieDescription;

    @FXML
    private Label movieDuration;

    @FXML
    private Label movieGenre;

    @FXML
    private ImageView movieImage;

    @FXML
    private TextField movieName;

    @FXML
    private Label movieTitle;

    @FXML
    private TableView<Film> moviesTable;

    @FXML
    private TableColumn<Film, String> titleColumn;


    BlFacadeImplementation bl;
    private Film newFilm;
    private ObservableList<Film>films;
    private final UIState uiState = UIState.getInstance();

    @FXML
    void initialize() {
        bl = BlFacadeImplementation.getInstance();
        aMovieButton.setDisable(true);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        films= FXCollections.observableArrayList(bl.getAllFilms());
        moviesTable.setItems(films);
    }
    @FXML
    void searchMovie(ActionEvent event) {
        String name = movieName.getText();
        newFilm = FilmDataFetcher.fetchFilmDataByName(name);

        if (newFilm == null) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Error");
            dialog.setContentText("No movie found with the name: " + name + ". Please try again.");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.showAndWait(); // Show the dialog and wait for it to be closed
        }
        else if (bl.getFilmbyName(newFilm.getTitle()) != null) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Error");
            dialog.setContentText("The movie is already in the database.");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.showAndWait();
            newFilm = null;
        }
        else {
            movieTitle.setText(newFilm.getTitle());
            movieDescription.setText(newFilm.getDescription());
            movieDuration.setText(newFilm.getDuration().format(DateTimeFormatter.ofPattern("HH:mm")));
            movieGenre.setText(newFilm.getGenre().toString());
            loadMovieImage(newFilm);
            aMovieButton.setDisable(false);
            uiState.setMovieListDirty(true);
        }
    }

    @FXML
    void addNewMovie(ActionEvent event) {
        bl.addFilm(newFilm);
        films.add(newFilm);

        movieName.clear();
        movieTitle.setText("");
        movieDescription.setText("");
        movieDuration.setText("");
        movieGenre.setText("");
        movieImage.setImage(null);
        aMovieButton.setDisable(true);
    }

    private void loadMovieImage(Film film) {
        // Check if the film image path is an external URL
        if (film.getImagePath().startsWith("http")) {
            // Set placeholder initially
            movieImage.setImage(new Image(getClass().getResourceAsStream("/eus/ehu/cinemaProject/ui/pictures/default-poster.jpg")));

            // Load the image in a background thread
            new Thread(() -> {
                try {
                    Image image = new Image(film.getImagePath(), false); // false = no background loading
                    javafx.application.Platform.runLater(() -> movieImage.setImage(image));
                } catch (Exception e) {
                    System.err.println("Failed to load image: " + film.getImagePath());
                }
            }).start();
        } else {
            // If the path is local, load it from resources
            movieImage.setImage(new Image(getClass().getResourceAsStream(film.getImagePath())));
        }
    }

    @FXML
    void goBack(ActionEvent event) {
        uiState.setCurrentView("adminMain.fxml");
    }
}
