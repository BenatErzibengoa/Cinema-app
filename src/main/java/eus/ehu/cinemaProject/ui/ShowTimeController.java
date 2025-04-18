package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.Film;
import eus.ehu.cinemaProject.domain.ShowTime;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

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

    BlFacadeImplementation bl;

    private final UIState uiState = UIState.getInstance();
    private List<ShowTime>showTimes;
    private Film film;
    private ShowTime selectedShowTime;

    @FXML
    void initialize() {
        bl = BlFacadeImplementation.getInstance();
        film = uiState.getFilm();

        movieTitle.setText(film.getTitle());
        movieDescription.setText(film.getDescription());
        movieDuration.setText(film.getDuration().format(DateTimeFormatter.ofPattern("HH:mm")));
        movieGenre.setText(film.getGenre().toString());
        movieImage.setImage(new Image(film.getImageurl()));

        //Configure the TilePane
        showtimesPane.setHgap(15);
        showtimesPane.setVgap(15);
        showtimesPane.setPadding(new Insets(10, 10, 10, 10));
        showtimesPane.getChildren().clear();

        //Create showTime Buttons
        showTimes=uiState.getShowtimes();

        for (ShowTime selectedShowTime: showTimes){
            Button showTimeButton = new Button(selectedShowTime.getScreeningTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            showTimeButton.setOnAction(event -> handleShowTimeClick(selectedShowTime));

            showtimesPane.getChildren().add(showTimeButton);
        }
    }

    private void handleShowTimeClick(ShowTime selectedShowTime) {
        uiState.setSelectedShowTime(selectedShowTime);
        uiState.setCurrentView("seatSelection");
    }


}
