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
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;


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
    private Schedule schedule;

    BlFacadeImplementation bl;
    private final UIState uiState = UIState.getInstance();

    @FXML
    public void initialize() {
        //set up timeline css
        timelineScrollPane.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/eus/ehu/cinemaProject/ui/timeline-style.css")).toExternalForm()
        );
        timelineScrollPane.getStyleClass().add("timeline-container");

        //Set up the view
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

        //To update movie list if new ones were added
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
            }else if (selectedDate.getValue().isBefore(LocalDate.now())) {
                showDialog("Error","The selected date has to be in the future.");
                return;
            }

            schedule = bl.getScheduleByRoomAndDate(selectedDate.getValue(), selectedScreeningRoom);
            if (schedule != null) {
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
        headerPane.getChildren().clear();
        if (schedule == null) return;

        LocalTime openingTime = schedule.getOpeningTime();
        int totalSlots = schedule.getSize();

        for (int slot = 0; slot <= totalSlots; slot++) {
            LocalTime time = openingTime.plusMinutes(slot * 15L);
            Label label = new Label(time.toString());

            label.getStyleClass().add("header-label");

            label.setLayoutX(slot * 15 * PIXELS_PER_MINUTE);
            headerPane.getChildren().add(label);
        }
    }

    private void setupTimeline() {
        // Add grid lines
        for (int slot = 0; slot <= schedule.getSize(); slot++) {
            Line gridLine = new Line(
                    slot * 15 * PIXELS_PER_MINUTE, 0,
                    slot * 15 * PIXELS_PER_MINUTE, TRACK_HEIGHT
            );
            gridLine.getStyleClass().add("grid-line");
            timelinePane.getChildren().add(gridLine);
        }

        // Existing showtimes with movie names
        for (ShowTime showtime : schedule.getShowTimes()) {
            createShowtimeRectangle(
                    showtime.getScreeningTime(),
                    showtime.getFilm().getDuration(),
                    "occupied-slot",
                    showtime.getFilm().getTitle()  // Add movie name
            );
        }

        // Available slots (15-minute markers)
        List<LocalTime> availableSlots = schedule.getAvailableStartTimes(selectedFilm.getDuration());
        for (LocalTime slot : availableSlots) {
            Rectangle slotMarker = new Rectangle(
                    calculateMinutesFromOpening(slot) * PIXELS_PER_MINUTE,
                    2,  // Top margin
                    15 * PIXELS_PER_MINUTE,  // Fixed 15-minute width
                    TRACK_HEIGHT - 4  // Height
            );
            slotMarker.getStyleClass().addAll("available-slot-marker");
            slotMarker.setOnMouseClicked(e -> createShowTime(slot));
            timelinePane.getChildren().add(slotMarker);
        }
    }

    private Rectangle createShowtimeRectangle(LocalTime startTime, LocalTime duration, String styleClass, String movieName) {
        int startMinutes = calculateMinutesFromOpening(startTime);
        int durationMinutes = convertLocalTimeToMinutes(duration);

        Rectangle rect = new Rectangle(
                startMinutes * PIXELS_PER_MINUTE,
                2,  // Add small top margin
                durationMinutes * PIXELS_PER_MINUTE,
                TRACK_HEIGHT - 4  // Account for margin
        );

        rect.getStyleClass().addAll("showtime-rect", styleClass);

        // Create and configure the label
        Label movieLabel = new Label(movieName);
        movieLabel.setLayoutX(startMinutes * PIXELS_PER_MINUTE + 5);  // Small offset from left
        movieLabel.setLayoutY(TRACK_HEIGHT / 2 - 8);  // Vertically centered
        movieLabel.setTextFill(Color.BLACK);
        movieLabel.setStyle("-fx-font-size: 10px;");
        movieLabel.setMaxWidth(durationMinutes * PIXELS_PER_MINUTE - 10);  // Prevent overflow
        movieLabel.setWrapText(true);

        Pane container = new Pane();
        container.getChildren().addAll(rect, movieLabel);
        timelinePane.getChildren().add(container);

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
