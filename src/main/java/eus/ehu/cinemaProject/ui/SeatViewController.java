package eus.ehu.cinemaProject.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class SeatViewController {
    @FXML
    private GridPane seatGrid;
    @FXML
    private Label screenLabel;
    @FXML
    private Label selectedSeats;

    private Map<String, Button> seatButtons = new HashMap<>();
    

    // These could come from your database or configuration
    private int rowCount = 5;  // Number of rows
    private int seatsPerRow = 8;  // Seats per row
    private String[] rowLabels = {"A", "B", "C", "D", "E"};

    @FXML
    public void initialize() {}

    public void goBack(ActionEvent actionEvent) {
    }

    public void goToConfirmation(ActionEvent actionEvent) {
    }

}

