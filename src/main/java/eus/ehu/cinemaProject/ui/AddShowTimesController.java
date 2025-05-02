package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.Film;
import eus.ehu.cinemaProject.domain.Schedule;
import eus.ehu.cinemaProject.domain.ScreeningRoom;
import eus.ehu.cinemaProject.domain.ShowTime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class AddShowTimesController {

    @FXML
    private ComboBox<String> movieCombobox;

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
    private final UIState uiState = UIState.getInstance();

    @FXML
    public void initialize() {
        bl = BlFacadeImplementation.getInstance();
        List<String> titles = bl.getAllFilms().stream()
                .map(Film::getTitle)
                .collect(Collectors.toList());
        movieCombobox.getItems().addAll(titles);
        screeningRoomComboBox.getItems().addAll(bl.getScreeningRooms());

        movieCombobox.valueProperty().addListener((obs, oldTitle, newTitle) -> {
            if (newTitle != null) {
                selectedFilm = bl.getFilmbyName(newTitle);
            } else {
                selectedFilm = null;
            }
            refreshAvailableTimes();
        });
        screeningRoomComboBox.valueProperty().addListener((obs, old, niu) -> {
            selectedScreeningRoom = niu;
            refreshAvailableTimes();
        });
        selectedDate.valueProperty().addListener((obs, old, niu) -> {
            refreshAvailableTimes();
        });

        // Initially disable until all three are picked
        showTimeButton.setDisable(true);
    }


   @FXML
    void addShowTime(ActionEvent event) {
        ShowTime showTime = new ShowTime(schedule, startingTime, selectedFilm);
        bl.saveShowTime(showTime);
       refreshAvailableTimes();
   }

    @FXML
    void goBack(ActionEvent event) {
        uiState.setCurrentView("adminMain.fxml");
    }

    private void refreshAvailableTimes() {
        timesCombobox.getItems().clear();
        timesCombobox.getSelectionModel().clearSelection();
        showTimeButton.setDisable(true);

        if (selectedDate.getValue() != null && selectedScreeningRoom != null && selectedFilm != null) {

            //check if selected date is within 14 days
            if (selectedDate.getValue().isAfter(LocalDate.now().plusDays(13))) {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Error");
                dialog.setContentText("The selected date has to be within 14 days. Please change the date.");
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                dialog.showAndWait();
            }else{
                // Fetch fresh schedule and slots
                schedule = bl.getScheduleByRoomAndDate(selectedDate.getValue(), selectedScreeningRoom);
                List<LocalTime> slots = schedule.getAvailableStartTimes(selectedFilm.getDuration());
                timesCombobox.getItems().addAll(slots);

                timesCombobox.valueProperty().addListener((obs, old, niu) -> {
                    startingTime = niu;
                    showTimeButton.setDisable(niu == null);
                });
            }

        }

    }

}
