package eus.ehu.cinemaProject.ui.Customer;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.Film;
import eus.ehu.cinemaProject.domain.ShowTime;
import eus.ehu.cinemaProject.ui.UIState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ShowTimeController {

    @FXML
    private Label movieDescription;
    @FXML
    private Label movieDuration;
    @FXML
    private Label movieGenre;
    @FXML
    private ImageView movieImage;
    @FXML
    private Label movieTitle;
    @FXML
    private TilePane showtimesPane;
    @FXML
    private DatePicker datePicker;

    BlFacadeImplementation bl;

    private final UIState uiState = UIState.getInstance();
    private List<ShowTime>showTimes;
    private Film film;
    private ShowTime selectedShowTime;
    private LocalDate selectedDate;


    @FXML private ImageView star1;
    @FXML private ImageView star2;
    @FXML private ImageView star3;
    @FXML private ImageView star4;
    @FXML private ImageView star5;

    private void updateStarRating(double rating) {
        Image full = new Image(getClass().getResourceAsStream("/eus/ehu/cinemaProject/ui/pictures/filled_star.png"));
        Image half = new Image(getClass().getResourceAsStream("/eus/ehu/cinemaProject/ui/pictures/half_filled_star.png"));
        Image empty = new Image(getClass().getResourceAsStream("/eus/ehu/cinemaProject/ui/pictures/empty_star.png"));

        ImageView[] stars = {star1, star2, star3, star4, star5};

        for (int i = 0; i < 5; i++) {
            if (i < (int) rating) {
                stars[i].setImage(full);
            } else if (i + 1 - rating <= 0.5) {
                stars[i].setImage(half);
            } else {
                stars[i].setImage(empty);
            }
        }
    }


    @FXML
    void initialize() {
        bl = BlFacadeImplementation.getInstance();
        //get film
        film = uiState.getFilm();
        //set date
        datePicker.setValue(java.time.LocalDate.now());

        movieTitle.setText(film.getTitle());
        movieDescription.setText(film.getDescription());
        movieDuration.setText(film.getDuration().format(DateTimeFormatter.ofPattern("HH:mm")));
        movieGenre.setText(film.getGenre().toString());
        //movieImage.setImage(new Image(getClass().getResourceAsStream(film.getImagePath())));
        loadMovieImage(film);
        //Configure the TilePane
        showtimesPane.setHgap(15);
        showtimesPane.setVgap(15);
        showtimesPane.setPadding(new Insets(10, 10, 10, 10));

        updateStarRating(bl.getAverageRating(film));

    }

    private void handleShowTimeClick(ShowTime selectedShowTime) {
        uiState.setSelectedShowtime(selectedShowTime);
        uiState.setCurrentView("seatSelection.fxml");
    }

    @FXML
    void searchShowTimes(ActionEvent event2) {
        showtimesPane.getChildren().clear();
        //get selected date
        selectedDate = datePicker.getValue();
        //get showtimes
        showTimes = bl.getShowTimesByDateAndFilm(selectedDate, film);

        for (ShowTime selectedShowTime: showTimes){
            Button showTimeButton = new Button(selectedShowTime.getScreeningTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            showTimeButton.setOnAction(event -> handleShowTimeClick(selectedShowTime));

            showtimesPane.getChildren().add(showTimeButton);
        }
    }

    @FXML
    void searchReviews(){
        uiState.setReviews(bl.getReviewsByFilm(film));
        uiState.setCurrentView("reviews.fxml");
    }

    private void loadMovieImage(Film film) {
        // Check if the film image path is an external URL
        if (film.getImagePath().startsWith("http")) {
            // Set placeholder initially
            movieImage.setImage(new Image(getClass().getResourceAsStream("/eus/ehu/cinemaProject/ui/pictures/default-poster.jpg")));

            // Load the image in a background thread
            new Thread(() -> {
                try {
                    Image image = new Image(film.getImagePath(), true); // false = no background loading
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

}
