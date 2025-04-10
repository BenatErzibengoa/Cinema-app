package eus.ehu.cinemaProject.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.ScreeningRoom;
import eus.ehu.cinemaProject.domain.Seat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

public class SeatViewController {
    @FXML
    private GridPane seatGrid;

    private Map<String, Button> seatButtons = new HashMap<>();
    private BlFacadeImplementation bl;

    private int seatsPerRow = 10, seatsPerColumn = 4;


    @FXML
    public void initialize() {
        bl = BlFacadeImplementation.getInstance();
        ScreeningRoom screeningRoom = bl.getScreeningRooms().get(0); // Get the first screening room for demonstration
        int index =0;

        for (Seat seat : screeningRoom.getSeats()) {
            ToggleButton seatButton = new ToggleButton(seat.getSeatId());
            seatButton.setStyle(seat.getType().getStyle());
            seatButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    seatButton.setOpacity(0.5); // Set opacity to 50% when selected
                } else {
                    seatButton.setOpacity(1.0); // Restore opacity to 100% when unselected
                }
            });
            seatGrid.add(seatButton, index % seatsPerRow, index / seatsPerRow);
            GridPane.setHalignment(seatButton, HPos.CENTER);
            index++;
        }
    }

    public void goBack(ActionEvent actionEvent) {
    }

    public void goToConfirmation(ActionEvent actionEvent) {
    }

}

