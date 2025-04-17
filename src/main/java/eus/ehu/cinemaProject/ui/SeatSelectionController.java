package eus.ehu.cinemaProject.ui;

import java.util.*;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.ScreeningRoom;
import eus.ehu.cinemaProject.domain.Seat;
import eus.ehu.cinemaProject.domain.users.Customer;
import eus.ehu.cinemaProject.domain.users.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
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
    private UIState uiState = UIState.getInstance();

    private BlFacadeImplementation bl;
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

        bl = BlFacadeImplementation.getInstance();
        ScreeningRoom room = uiState.getSelectedShowtime().getSchedule().getScreeningRoom(); // Get the first screening room for demonstration
        roomNumberLabel.setText(String.valueOf(room.getRoomNumber()));
        seatsPerRow = room.getMAX_SEATS_PER_ROW();

        // Create column and row constraints for the grid
        for( int r=0; r<=room.getMAX_ROWS(); r++) seatGrid.getColumnConstraints().add(new ColumnConstraints());
        for( int c=0; c<=seatsPerRow; c++) seatGrid.getRowConstraints().add(new RowConstraints());

        // Create ToggleButton for each seat, each has a listener that changes opacity and adds/removes the seat from the selectedSeats list
        for (Seat seat : room.getSeats()) {
            id= seat.getSeatId().substring(2);
            ToggleButton seatButton = new ToggleButton(id);
            seatMap.put(seatButton, seat);
            seatButton.setStyle(seat.getType().getStyle());
            seatButton.setGraphic(new ImageView(seat.getImage()));

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

    // provisional
    @FXML
    void buyTickets(ActionEvent event) {
        User customer = bl.getUserByEmail(uiState.getEmail());

        bl.createPurchaseReceipt((Customer)customer, uiState.getSelectedShowtime(), selectedSeats);
        //TODO: when Ekhi finishes his fxml, update the String of the view
        uiState.setCurrentView("buyFood.fxml");
    }
}

