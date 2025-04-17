package eus.ehu.cinemaProject.ui;

import java.util.*;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.ScreeningRoom;
import eus.ehu.cinemaProject.domain.Seat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class SeatSelectionController {
    @FXML
    private Label roomNumberLabel;

    @FXML
    private GridPane seatGrid;

    @FXML
    private Label totalPriceLabel;

    private Map<ToggleButton, Seat> seatMap = new HashMap<>();

    private BlFacadeImplementation bl;
    private int seatsPerRow = 10, seatsPerColumn = 4; // this is an example, it should get it from the ShowRoom settings
    private ArrayList<Seat> selectedSeats = new ArrayList<>();

    /* This controller needs to track the previous controllers' parameters: customer, ShowTime/ScreeningRoom
    * Customer is needed just to create the PurchaseReceipt
    * The ShowTime -> ScreeningRoom -> Seat is needed to create the button/seat selection.
    * It is important to know the dimensions - row x column - of the room to create the grid. Another solution could be to divide the total number of seats by a fixed number of rows.
    * */

    @FXML
    public void initialize() {
        bl = BlFacadeImplementation.getInstance();
        ScreeningRoom screeningRoom = bl.getScreeningRooms().get(0); // Get the first screening room for demonstration
        for( int r=0; r<seatsPerRow; r++) seatGrid.getColumnConstraints().add(new ColumnConstraints());
        for( int c=0; c<seatsPerColumn; c++) seatGrid.getRowConstraints().add(new RowConstraints());

        int index =0;
        String id;

        for (Seat seat : screeningRoom.getSeats()) {
            id= seat.getSeatId().substring(2);
            ToggleButton seatButton = new ToggleButton(id);
            seatMap.put(seatButton, seat);
            seatButton.setStyle(seat.getType().getStyle());

            seatButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    selectedSeats.add(seatMap.get(seatButton));
                    seatButton.setOpacity(0.5); // Set opacity to 50% when selected
                } else {
                    selectedSeats.remove(seatMap.get(seatButton));
                    seatButton.setOpacity(1.0); // Restore opacity to 100% when unselected
                }
            });

            seatGrid.add(seatButton, index % seatsPerRow, index / seatsPerRow);
            GridPane.setHalignment(seatButton, HPos.CENTER);
            index++;
        }
    }

    /*
    // provisional
    @FXML
    void buyTickets(ActionEvent event) {
        int result = bl.createPurchaseReceipt(customer, showTime, selectedSeats);
        if(result != -1){
            totalPriceLabel.setText(String.valueOf(result));

        }else
            totalPriceLabel.setText("Sorry, there's been an error. Try again.");
    }
     */
}

