package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.Film;
import eus.ehu.cinemaProject.domain.Schedule;
import eus.ehu.cinemaProject.domain.ScreeningRoom;
import eus.ehu.cinemaProject.domain.ShowTime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.time.LocalTime;
import java.util.List;

public class AddShowTimesController {

    @FXML
    private ComboBox<Film> movieCombobox;

    @FXML
    private ComboBox<ScreeningRoom> screeningRoomComboBox;

    @FXML
    private ComboBox<LocalTime> timesCombobox;


    @FXML
    private Button showTimeButton;

    @FXML
    private DatePicker selectedDate;
    private Film selectedFilm;
    private ScreeningRoom selectedScreeningRoom;
    private LocalTime startingTime=null;
    private Schedule schedule;

    BlFacadeImplementation bl;

    @FXML
    public void initialize() {
        bl = BlFacadeImplementation.getInstance();
        movieCombobox.getItems().addAll(bl.getAllFilms());
        screeningRoomComboBox.getItems().addAll(bl.getScreeningRooms());

        movieCombobox.setOnAction(event -> {
            selectedFilm = movieCombobox.getValue();
        });

        screeningRoomComboBox.setOnAction(event ->{
            selectedScreeningRoom=screeningRoomComboBox.getValue();
        });
        showTimeButton.setDisable(true);

    }

    @FXML
    void displayTimes(ActionEvent event) {
        schedule=bl.getScheduleByRoomAndDate(selectedDate.getValue(), selectedScreeningRoom);
        LocalTime duration = selectedFilm.getDuration();
        timesCombobox.getItems().addAll(schedule.getAvailableStartTimes(duration));
        timesCombobox.setOnAction(event1 -> {
            startingTime = timesCombobox.getValue();
        });
        showTimeButton.setDisable(false);
    }

   @FXML
    void addShowTime(ActionEvent event) {
        ShowTime showTime = new ShowTime(schedule, startingTime, selectedFilm);
        bl.saveShowTime(showTime);

   }


}
