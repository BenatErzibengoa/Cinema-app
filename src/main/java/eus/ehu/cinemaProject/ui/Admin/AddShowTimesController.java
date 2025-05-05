package eus.ehu.cinemaProject.ui.Admin;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.Film;
import eus.ehu.cinemaProject.domain.Schedule;
import eus.ehu.cinemaProject.domain.ScreeningRoom;
import eus.ehu.cinemaProject.domain.ShowTime;
import eus.ehu.cinemaProject.ui.UIState;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


public class AddShowTimesController {

    @FXML
    private ComboBox<String> movieCombobox;

    @FXML
    private ComboBox<ScreeningRoom> screeningRoomComboBox;

    @FXML
    private Pane timePane;

    @FXML
    private VBox datesContainer; // Add this to replace the DatePicker

    @FXML private Region dateSpacer;

    @FXML
    private ScrollPane timelineScrollPane;

    private static final int PIXELS_PER_MINUTE = 2;
    private static final int TRACK_HEIGHT = 40; // Height for each timeline entry
    private static final int FRAME_MINUTES = 15;
    private static final LocalTime OPENING_TIME = LocalTime.of(15,30);
    private static final LocalTime CLOSING_TIME = LocalTime.of(1,0); // next-day





    private Film selectedFilm;
    private ScreeningRoom selectedScreeningRoom;

    BlFacadeImplementation bl;
    private final UIState uiState = UIState.getInstance();


    //To cache the timelines
    private final Map<LocalDate, Pane> dateCache = new HashMap<>();

    private boolean isRefreshing = false;

    @FXML
    public void initialize() {
        bl = BlFacadeImplementation.getInstance();
        refreshMovieList();
        timelineScrollPane.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/eus/ehu/cinemaProject/ui/timeline-style.css")).toExternalForm()
        );

        screeningRoomComboBox.valueProperty().addListener((obs, old, room) -> {
            selectedScreeningRoom = room;
            dateCache.clear();
            refreshDateRows();
        });

        movieCombobox.valueProperty().addListener((obs, old, title) -> {
            selectedFilm = bl.getFilmbyName(title);
            dateCache.clear();
            refreshDateRows();
        });

        screeningRoomComboBox.getItems().addAll(bl.getScreeningRooms());
        uiState.currentViewProperty().addListener((obs, oldView, newView) -> {
            if ("addShowTimes.fxml".equals(newView) && UIState.getInstance().isMovieListDirty()) {
                refreshMovieList();
                UIState.getInstance().setMovieListDirty(false);
            }
        });

    }


    private void setupHeader() {
        timePane.getChildren().clear();
        // compute total open minutes (overnight aware)
        int openMin  = OPENING_TIME.toSecondOfDay()/60;
        int closeMin = CLOSING_TIME.toSecondOfDay()/60;
        if (closeMin <= openMin) closeMin += 24*60;
        int totalMin  = closeMin - openMin;
        int totalSlots = totalMin / FRAME_MINUTES;

        // make timePane wide enough to hold every slot
        timePane.setPrefWidth(totalMin * PIXELS_PER_MINUTE);

        for (int i = 0; i <= totalSlots; i++) {
            LocalTime t = OPENING_TIME.plusMinutes(i * FRAME_MINUTES);
            if (t.isBefore(OPENING_TIME)) t = t.plusHours(24);
            Label lbl = new Label(t.toString());
            lbl.setLayoutX(i * FRAME_MINUTES * PIXELS_PER_MINUTE);
            lbl.setLayoutY(0);
            timePane.getChildren().add(lbl);
        }
    }


    private void refreshDateRows() {


        datesContainer.getChildren().clear();
        if (selectedScreeningRoom == null || selectedFilm == null) return;

        LocalDate startDate = LocalDate.now();
        List<LocalDate> dates = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            dates.add(startDate.plusDays(i));
        }

        // Maintain sorted order
        dates.sort(LocalDate::compareTo);

        for (LocalDate date : dates) {
            HBox row = (HBox) dateCache.computeIfAbsent(date, d -> createDateRow(d));
            datesContainer.getChildren().add(row);
        }
        if (!datesContainer.getChildren().isEmpty()) {
            HBox firstRow = (HBox) datesContainer.getChildren().get(0);
            Label firstLabel = (Label) firstRow.getChildren().get(0);
            dateSpacer.prefWidthProperty().bind(firstLabel.widthProperty());
        }
        setupHeader();
    }

    private HBox createDateRow(LocalDate date) {

        HBox row = new HBox(10);
        row.getStyleClass().add("date-row");

        // Store date in the row's properties for later reference
        row.getProperties().put("date", date);

        // Date label
        Label dateLabel = new Label(date.toString());
        dateLabel.getStyleClass().add("date-label");

        // Timeline pane
        Pane timelinePane = new Pane();
        timelinePane.getProperties().put("date", date);
        timelinePane.setPrefSize(1200, TRACK_HEIGHT);

        // Store date in timeline pane properties for reference in click handlers
        timelinePane.getProperties().put("date", date);

        // Load schedule
        try {
            if (selectedScreeningRoom != null) {
                Schedule schedule = bl.getScheduleByRoomAndDate(date, selectedScreeningRoom);
                if (schedule != null) {
                    drawTimeline(timelinePane, schedule, date);
                }
            }
        } catch (Exception e) {
            showDialog("Error", "Failed to load schedule: " + e.getMessage());
        }

        row.getChildren().addAll(dateLabel, timelinePane);
        return row;
    }

    private void drawTimeline(Pane timelinePane, Schedule schedule, LocalDate date) {
        timelinePane.getChildren().clear();

        // Grid lines
        for (int slot = 0; slot <= schedule.getSize(); slot++) {
            Line gridLine = new Line(
                    slot * 15 * PIXELS_PER_MINUTE, 0,
                    slot * 15 * PIXELS_PER_MINUTE, TRACK_HEIGHT
            );
            gridLine.getStyleClass().add("grid-line");
            timelinePane.getChildren().add(gridLine);
        }

        // Existing showtimes
        List<ShowTime> showTimesCopy = new ArrayList<>(schedule.getShowTimes());
        for (ShowTime showtime : showTimesCopy) {
            createShowtimeRectangle(
                    timelinePane,
                    showtime.getScreeningTime(),
                    showtime.getFilm().getDuration(),
                    "occupied-slot",
                    showtime.getFilm().getTitle()
            );
        }

        if (selectedFilm == null || selectedFilm.getDuration() == null) {
            showDialog("Error", "No valid film duration");
            return;
        }

        List<LocalTime> availableSlots = schedule.getAvailableStartTimes(selectedFilm.getDuration());
        for (LocalTime slot : availableSlots) {
            int x = calculateMinutesFromOpening(slot, schedule.getOpeningTime()) * PIXELS_PER_MINUTE;
            int w = FRAME_MINUTES * PIXELS_PER_MINUTE;  // width = one slot unit

            Rectangle slotMarker = new Rectangle(x, 2, w, TRACK_HEIGHT - 4);
            slotMarker.getStyleClass().add("available-slot-marker");

            //  Tooltip shows start + film-duration end
            LocalTime end = slot.plusMinutes(selectedFilm.getDuration().toSecondOfDay()/60);
            Tooltip.install(slotMarker,
                    new Tooltip(String.format("Starts: %s\nEnds:   %s", slot, end))
            );

            slotMarker.setOnMouseClicked(e -> createShowTime(slot, date));
            timelinePane.getChildren().add(slotMarker);
        }
    }

    @FXML
    void goBack(ActionEvent event) {
        uiState.setCurrentView("adminMain.fxml");
    }



    private void createShowtimeRectangle(Pane parent,
                                         LocalTime startTime,
                                         LocalTime duration,
                                         String styleClass,
                                         String movieName) {
        // Get date from parent pane's user data
        LocalDate date = (LocalDate) parent.getProperties().get("date");
        if (date == null) return;

        Schedule schedule = bl.getScheduleByRoomAndDate(date, selectedScreeningRoom);

        if (schedule == null) return;

        int startMinutes = calculateMinutesFromOpening(startTime, schedule.getOpeningTime());
        int durationMinutes = convertLocalTimeToMinutes(duration);

        Rectangle rect = new Rectangle(
                startMinutes * PIXELS_PER_MINUTE,
                2,
                durationMinutes * PIXELS_PER_MINUTE,
                TRACK_HEIGHT - 4
        );
        rect.getStyleClass().addAll("showtime-rect", styleClass);

        Label movieLabel = new Label(movieName);
        movieLabel.setLayoutX(startMinutes * PIXELS_PER_MINUTE + 5);
        movieLabel.setLayoutY(TRACK_HEIGHT / 2 - 8);
        movieLabel.setStyle("-fx-font-size: 10px;");

        parent.getChildren().addAll(rect, movieLabel);
    }

    private int convertLocalTimeToMinutes(LocalTime time) {
        return (time.getHour() * 60) + time.getMinute();
    }

    private int calculateMinutesFromOpening(LocalTime time, LocalTime openingTime) {
        if (time.isBefore(openingTime)) {
            return (int) Duration.between(openingTime, time.plusHours(24)).toMinutes();
        }
        return (int) Duration.between(openingTime, time).toMinutes();
    }
    private void createShowTime(LocalTime selectedTime, LocalDate date) {
        Schedule schedule = bl.getScheduleByRoomAndDate(date, selectedScreeningRoom);
        if (schedule != null && selectedFilm != null) {
            // Re-validate availability before booking
            if (schedule.isBetweenBoundsFree(selectedTime, selectedFilm.getDuration())) {
                ShowTime showTime = new ShowTime(schedule, selectedTime, selectedFilm);
                if (schedule.setShowTime(showTime)) {
                    // Run DB save in background
                    Task<Void> dbTask = new Task<>() {
                        @Override
                        protected Void call() {
                            bl.saveShowTime(showTime);
                            return null;
                        }
                    };

                    dbTask.setOnSucceeded(e -> {
                        // UI update must be on JavaFX thread
                        refreshSingleDate(date);
                    });

                    dbTask.setOnFailed(e -> {
                        // Optional: handle save failure (e.g., log or show dialog)
                        showDialog("Error", "Failed to save showtime: " + dbTask.getException().getMessage());
                    });

                    new Thread(dbTask).start();
                    showDialog("Success", "Showtime created successfully");
                }
            } else {
                showDialog("Error", "Slot no longer available");
            }
        }
    }


    private void refreshSingleDate(LocalDate date) {

        if (isRefreshing) return;
        isRefreshing = true;

        // Remove existing date row
        dateCache.remove(date);
        datesContainer.getChildren().removeIf(node ->
                ((LocalDate) ((HBox) node).getProperties().get("date")).equals(date)
        );

        // Create new row
        HBox newRow = createDateRow(date);

        // Find correct position using original date order
        int insertIndex = (int) dateCache.keySet().stream()
                .filter(d -> !d.isAfter(date))
                .count();

        // Insert at correct position
        datesContainer.getChildren().add(insertIndex, newRow);
        dateCache.put(date, newRow);

        isRefreshing = false;
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
