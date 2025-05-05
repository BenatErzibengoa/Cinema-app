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

import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

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

    private final ResourceBundle bundle = ResourceBundle.getBundle("eus.ehu.cinemaProject.ui.Language", Locale.getDefault());

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
            dialog.setTitle(bundle.getString("dialog.error.title"));
            dialog.setContentText(MessageFormat.format(bundle.getString("dialog.error.noMovie"), name));
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.showAndWait();
        } else if (bl.getFilmbyName(newFilm.getTitle()) != null) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle(bundle.getString("dialog.error.title"));
            dialog.setContentText(bundle.getString("dialog.error.duplicate"));
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
