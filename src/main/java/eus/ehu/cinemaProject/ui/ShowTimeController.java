package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.time.LocalTime;
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


    //THESE ARE TEMPORARY ATTRIBUTES, just to ensure  buttons are created correctly (The showtimes and the movie will be passed as parameters to the controller)
    private List<LocalTime> showTimes=List.of(LocalTime.of(10, 0), LocalTime.of(12, 30), LocalTime.of(15, 0), LocalTime.of(17, 30), LocalTime.of(20, 0));
    private String showTime;

    @FXML
    void initialize() {
        bl = BlFacadeImplementation.getInstance();

        //load movie info(WE WILL GET THE FILM FROM THE PREVIOUS CONTROLLER, TODO: change parameters)
        movieTitle.setText("Movie Title");
        movieDescription.setText("Movie Description");
        movieDuration.setText("120 min");
        movieGenre.setText("Action, Adventure");
        movieImage.setImage(null);

        //Configure the TilePane
        showtimesPane.setHgap(15);
        showtimesPane.setVgap(15);
        showtimesPane.setPadding(new Insets(10, 10, 10, 10));
        showtimesPane.getChildren().clear();

        //Create showTime Buttons
        for (LocalTime showTime: showTimes){
            Button showTimeButton = new Button(showTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            showTimeButton.setOnAction(event -> handleShowTimeClick(showTime));

            showtimesPane.getChildren().add(showTimeButton);
        }

        //Correct one, with list of ShowTimes:

        //for (ShowTime showTime: showTimes){
        //    Button showTimeButton = new Button(showTime.getScreeningTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        //    showTimeButton.setOnAction(event -> handleShowTimeClick(showTime));

        //    showtimesPane.getChildren().add(showTimeButton);
        // }

    }

    private void handleShowTimeClick(LocalTime showTime) {
        //TODO:when user clicks on a showtime button, the seatView will load, and we will pass the selected showtime to the seatView controller

    }


}
