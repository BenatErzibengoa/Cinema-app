package eus.ehu.cinemaProject.ui.User;

import eus.ehu.cinemaProject.businessLogic.BlFacade;
import eus.ehu.cinemaProject.domain.Film;
import eus.ehu.cinemaProject.domain.ShowTime;
import eus.ehu.cinemaProject.ui.UIState;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MovieListController {

    @FXML private TilePane movieTilePane;
    private final UIState uiState = UIState.getInstance();

    private BlFacade businessLogic;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH'h'mm");

    public void setBusinessLogic(BlFacade blFacade) {
        this.businessLogic = blFacade;
        loadMovies();
    }

    private void loadMovies() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate endDate = today.plusDays(14); // look 14 days

            Map<Film, LocalTime> filmsMap = new LinkedHashMap<>();

            for (LocalDate date = today; date.isBefore(endDate); date = date.plusDays(1)) {
                List<ShowTime> showTimes = businessLogic.getShowTimesByDate(date);

                for (ShowTime showTime : showTimes) {
                    Film film = showTime.getFilm();
                    if (!filmsMap.containsKey(film)) {
                        filmsMap.put(film, showTime.getScreeningTime());
                    }
                }
            }

            movieTilePane.getChildren().clear();

            for (Map.Entry<Film, LocalTime> entry : filmsMap.entrySet()) {
                VBox filmCard = createFilmCard(entry.getKey(), entry.getValue());
                movieTilePane.getChildren().add(filmCard);
            }

        } catch (Exception e) {
            showError("Error loading movies: " + e.getMessage());
        }
    }

    private VBox createFilmCard(Film film, LocalTime screeningTime) {
        // Image
        ImageView posterView = createPosterImageView(film);

        // Texts
        Text titleText = new Text(film.getTitle());
        titleText.setWrappingWidth(200);
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Text detailsText = new Text(formatMovieDetails(film, screeningTime));
        detailsText.setStyle("-fx-font-size: 14px; -fx-fill: #666666;");


        //Star rating
        HBox starRating = createStarRating(businessLogic.getAverageRating(film));

        // Button
        Button bookButton = createBookButton(film);

        // Card assembly
        VBox card = new VBox(8, posterView, titleText, starRating, detailsText, bookButton);
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-padding: 15px;");
        card.setMaxWidth(220);

        return card;
    }

    private ImageView createPosterImageView(Film film) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        try {
            if (film.getImagePath().startsWith("http")) {
                Image placeholder = new Image(getClass().getResourceAsStream("/eus/ehu/cinemaProject/ui/pictures/default-poster.jpg"));
                imageView.setImage(placeholder);

                new Thread(() -> {
                    try {
                        Image image = new Image(film.getImagePath(), true); // false = no background loading
                        javafx.application.Platform.runLater(() -> imageView.setImage(image));
                    } catch (Exception e) {
                        System.err.println("Failed to load image: " + film.getImagePath());
                    }
                }).start();
            } else {
                Image image = new Image(getClass().getResourceAsStream(film.getImagePath()));
                imageView.setImage(image);
            }
        } catch (Exception e) {
            System.err.println("Image not found: " + film.getImagePath());
            imageView.setImage(new Image(getClass().getResourceAsStream("/eus/ehu/cinemaProject/ui/pictures/default-poster.jpg")));
        }

        return imageView;
    }


    private HBox createStarRating(double rating) {
        HBox stars = new HBox(2);

        for (int i = 1; i <= 5; i++) {
            String imagePath;

            if (i <= (int) rating) {
                imagePath = "/eus/ehu/cinemaProject/ui/pictures/filled_star.png";
            } else if (i - rating <= 0.5) {
                imagePath = "/eus/ehu/cinemaProject/ui/pictures/half_filled_star.png";
            } else {
                imagePath = "/eus/ehu/cinemaProject/ui/pictures/empty_star.png";
            }

            ImageView star = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
            star.setFitWidth(20);
            star.setFitHeight(20);
            stars.getChildren().add(star);
        }

        return stars;
    }



    private String formatMovieDetails(Film film, LocalTime screeningTime) {
        /*return String.format("%s | "+uiState.getBundle().getString("screeningTime")+": %s",
                formatDuration(film.getDuration()),
                screeningTime.format(timeFormatter));
                */
         return formatDuration(film.getDuration());
    }

    private String formatDuration(LocalTime duration) {
        return duration != null ? duration.format(timeFormatter) : "N/A";
    }

    private Button createBookButton(Film film) {
        Button button = new Button(uiState.getBundle().getString("bookButton"));
        button.setStyle("-fx-background-color: #dd6600; -fx-text-fill: white; -fx-font-weight: bold;");
        button.setOnAction(event -> showBookingAlert(film));
        return button;
    }

    private void showBookingAlert(Film film) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Booking");
        alert.setHeaderText("Booking for: " + film.getTitle());
        alert.setContentText("Redirecting to booking page...");
        //alert.showAndWait();
        if(uiState.isLoggedIn()){
            uiState.setFilm(film);
            uiState.setCurrentView("showTime.fxml");
        } else {
            uiState.setCurrentView("signin.fxml");
        }

    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}