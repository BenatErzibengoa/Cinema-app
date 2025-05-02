package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.Film;
import eus.ehu.cinemaProject.domain.Schedule;
import eus.ehu.cinemaProject.domain.ScreeningRoom;
import eus.ehu.cinemaProject.domain.ShowTime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.time.Duration;
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
    private DatePicker selectedDate;

    @FXML
    private ScrollPane timelineScrollPane;

    @FXML
    private Pane headerPane;

    @FXML
    private Pane timelinePane;

    private static final int PIXELS_PER_MINUTE = 2;
    private static final int TRACK_HEIGHT = 40;      // Height for each timeline entry
    private static final LocalTime OPENING_TIME = LocalTime.of(15, 30);
    private static final int TOTAL_OPERATING_MINUTES = 570; // 9.5 hours



    private Film selectedFilm;
    private ScreeningRoom selectedScreeningRoom;
    private LocalTime startingTime=null;
    private Schedule schedule;

    BlFacadeImplementation bl;
    private final UIState uiState = UIState.getInstance();

    @FXML
    public void initialize() {
        bl = BlFacadeImplementation.getInstance();
        refreshMovieList();
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

        //To update movielist if new ones were added
        uiState.currentViewProperty().addListener((obs, oldView, newView) -> {
            if ("addShowTimes.fxml".equals(newView) && UIState.getInstance().isMovieListDirty()) {
                refreshMovieList();
                UIState.getInstance().setMovieListDirty(false);
            }
        });
    }



    @FXML
    void goBack(ActionEvent event) {
        uiState.setCurrentView("adminMain.fxml");
    }

    private void refreshAvailableTimes() {
        timelinePane.getChildren().clear();

        if (selectedDate.getValue() != null && selectedScreeningRoom != null && selectedFilm != null) {
            if (selectedDate.getValue().isAfter(LocalDate.now().plusDays(13))) {
                showDialog("Error","The selected date has to be within 14 days.");
                return;
            }

            schedule = bl.getScheduleByRoomAndDate(selectedDate.getValue(), selectedScreeningRoom);
            if (schedule != null) {
                schedule.reconstructBookingStateFromShowTimes();
                updateTimeline();
            }
        }
    }

    private void updateTimeline() {
        clearTimeline();

        if (schedule == null || selectedFilm == null) return;

        setupHeader();
        setupTimeline();
    }

    private void clearTimeline() {
        headerPane.getChildren().clear();
        timelinePane.getChildren().clear();
        double width = TOTAL_OPERATING_MINUTES * PIXELS_PER_MINUTE;
        headerPane.setPrefWidth(width);
        timelinePane.setPrefWidth(width);
    }

    private void setupHeader() {
        for (int minutes = 0; minutes <= TOTAL_OPERATING_MINUTES; minutes += 15) {
            LocalTime time = OPENING_TIME.plusMinutes(minutes);
            if (time.equals(LocalTime.MIDNIGHT)) time = LocalTime.of(0, 0);

            Label label = new Label(time.toString());
            label.setLayoutX(minutes * PIXELS_PER_MINUTE);
            headerPane.getChildren().add(label);
        }
    }

    private void setupTimeline() {
        // Existing showtimes
        for (ShowTime showtime : schedule.getShowTimes()) {
            addShowtimeRectangle(showtime.getScreeningTime(),
                    showtime.getFilm().getDuration(),
                    Color.LIGHTGRAY);
        }

        // Available slots
        List<LocalTime> availableSlots = schedule.getAvailableStartTimes(selectedFilm.getDuration());
        for (LocalTime slot : availableSlots) {
            Rectangle rect = addShowtimeRectangle(slot, selectedFilm.getDuration(), Color.LIGHTGREEN);
            rect.setOnMouseClicked(e -> createShowTime(slot));
        }
    }

    private Rectangle addShowtimeRectangle(LocalTime startTime, LocalTime duration, Color color) {
        int startMinutes = calculateMinutesFromOpening(startTime);
        int durationMinutes = convertLocalTimeToMinutes(duration);

        Rectangle rect = new Rectangle(
                startMinutes * PIXELS_PER_MINUTE,
                0,
                durationMinutes * PIXELS_PER_MINUTE,
                TRACK_HEIGHT
        );
        rect.setFill(color);
        rect.setStroke(Color.BLACK);
        timelinePane.getChildren().add(rect);


        return rect;
    }

    private int convertLocalTimeToMinutes(LocalTime time) {
        return (time.getHour() * 60) + time.getMinute();
    }

    private int calculateMinutesFromOpening(LocalTime time) {
        LocalTime openingTime = schedule.getOpeningTime();

        if (time.isBefore(openingTime)) {
            // Handle overnight schedule (after midnight)
            return (int) Duration.between(openingTime, time.plusHours(24)).toMinutes();
        }
        return (int) Duration.between(openingTime, time).toMinutes();
    }
    private void createShowTime(LocalTime selectedTime) {
        ShowTime showTime = new ShowTime(schedule, selectedTime, selectedFilm);
        if (schedule.setShowTime(showTime)) {
            bl.saveShowTime(showTime);
            showDialog("Success","Showtime added successfully");
            refreshAvailableTimes();
        } else {
            showDialog("Error","Time slot no longer available");
        }
    }
    private void showDialog(String title,String message) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setContentText(message);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    private void refreshMovieList() {
        movieCombobox.getItems().clear();
        List<String> titles = bl.getAllFilms().stream()
                .map(Film::getTitle)
                .toList();
        movieCombobox.getItems().addAll(titles);

    }



}
