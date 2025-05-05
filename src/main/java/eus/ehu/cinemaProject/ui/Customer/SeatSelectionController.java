package eus.ehu.cinemaProject.ui.Customer;

import java.util.*;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.ScreeningRoom;
import eus.ehu.cinemaProject.domain.Seat;
import eus.ehu.cinemaProject.ui.UIState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class SeatSelectionController {
    @FXML
    private Label roomNumberLabel;

    @FXML
    private GridPane seatGrid;

    @FXML
    private Label totalPriceLabel;

    private Map<ToggleButton, Seat> seatMap = new HashMap<>();
    private UIState uiState = UIState.getInstance();

    private BlFacadeImplementation bl = BlFacadeImplementation.getInstance();;
    private ArrayList<Seat> selectedSeats = new ArrayList<>();

    /* This controller needs to track the previous controllers' parameters: customer, ShowTime/ScreeningRoom
    * Customer is needed just to create the PurchaseReceipt
    * The ShowTime -> ScreeningRoom -> Seat is needed to create the button/seat selection.
    * It is important to know the dimensions - row x column - of the room to create the grid. Another solution could be to divide the total number of seats by a fixed number of rows.
    * */

    @FXML
    public void initialize() {
        int index =0, seatsPerRow;
        String id;
        ScreeningRoom room = uiState.getSelectedShowtime().getSchedule().getScreeningRoom();
        roomNumberLabel.setText(String.valueOf(room.getRoomNumber()));
        seatsPerRow = room.getMAX_SEATS_PER_ROW();

        // Create column constraints for the grid
        for (int c = 0; c <= seatsPerRow; c++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.SOMETIMES); // Ensures the columns grow evenly
            seatGrid.getColumnConstraints().add(column);
        }

        // Create row constraints for the grid
        for (int r = 0; r <= room.getMAX_ROWS(); r++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.SOMETIMES); // Ensures the rows grow evenly
            row.setMinHeight(1);
            seatGrid.getRowConstraints().add(row);
        }


        // Create ToggleButton for each seat, each has a listener that changes opacity and adds/removes the seat from the selectedSeats list
        for (Seat seat : room.getSeats()) {
            id= seat.getSeatId().substring(2);
            ToggleButton seatButton = new ToggleButton(id);
            seatMap.put(seatButton, seat);
            seatButton.setContentDisplay(ContentDisplay.TOP); //id will appear on top of the image
            seatButton.setStyle(seat.getType().getStyle() + "-fx-font-size: 10px;");

            //Add image but make the size smaller
            ImageView seatImage = new ImageView(seat.getImage());
            seatImage.setFitWidth(35);
            seatImage.setFitHeight(35);
            seatImage.setPreserveRatio(true);
            seatButton.setGraphic(seatImage);

            seatButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if(seat.isReserved()) {
                    seatButton.setSelected(false);
                    return;
                }
                if (newValue) {
                    selectedSeats.add(seatMap.get(seatButton));
                    seatButton.setOpacity(0.5); // Set opacity to 50% when selected
                } else {
                    selectedSeats.remove(seatMap.get(seatButton));
                    seatButton.setOpacity(1.0); // Restore opacity to 100% when unselected
                }
                // Update the total price label
                updateTotalPrice();
            });

            seatGrid.add(seatButton, index % seatsPerRow, index / seatsPerRow);
            GridPane.setHalignment(seatButton, HPos.CENTER);
            index++;
        }

    }

    @FXML
    void buyTickets(ActionEvent event) {
        uiState.setSelectedSeats(selectedSeats);
        // Receipt is created when proceeding to payment
        //bl.createPurchaseReceipt((Customer)customer, uiState.getSelectedShowtime(), selectedSeats);
        uiState.setCurrentView("receipt.fxml");
    }

    //Update the total price label with the sum of the selected seats, using stream
    private void updateTotalPrice() {
        double totalPrice = selectedSeats.stream()
                .mapToDouble(Seat::getPrice)
                .sum();
        totalPriceLabel.setText(String.format("%.2f €", totalPrice));
    }

    @FXML
    void goBack(ActionEvent event) {
        uiState.setCurrentView("showTime.fxml");
    }

}

