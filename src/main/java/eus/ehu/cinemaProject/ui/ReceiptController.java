package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacade;
import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.Film;
import eus.ehu.cinemaProject.domain.PurchaseReceipt;
import eus.ehu.cinemaProject.domain.Seat;
import eus.ehu.cinemaProject.domain.users.Customer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.Date;

public class ReceiptController {

    @FXML
    private BorderPane contentPane;
    @FXML
    private Label numberOfRaces;
    @FXML
    private TableView<PurchaseReceipt> tablePurchaseReceipts;

    @FXML
    private TableColumn<PurchaseReceipt, Long> showTimeIdColumn;

    @FXML
    private TableColumn<PurchaseReceipt, String> filmColumn;

    @FXML
    private TableColumn<PurchaseReceipt, Date> dateColumn;

    @FXML
    private TableColumn<PurchaseReceipt, String> seatColumn;

    @FXML
    private TableColumn<PurchaseReceipt, Double> priceColumn;



    private ObservableList<PurchaseReceipt> purchaseReceipts;

    private BlFacadeImplementation bl;

    // initialize the list with some drivers
    @FXML
    public void initialize() {
        bl = BlFacadeImplementation.getInstance();
        //bind each value to the corresponding column
        showTimeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));


        filmColumn.setCellValueFactory(cellData -> {
            PurchaseReceipt pr = cellData.getValue();
            return new SimpleStringProperty(pr.getShowTime().getFilm().getTitle());
        });
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        seatColumn.setCellValueFactory(new PropertyValueFactory<>("bookedSeats"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        purchaseReceipts = FXCollections.observableArrayList();
        purchaseReceipts.addAll(bl.getPurchaseReceiptsByUser((Customer) bl.getUserByEmail("aitor@gmail.com")));
        tablePurchaseReceipts.setItems(purchaseReceipts); //Assign the UI table a reference to the observable list (UI <--> Data)



    }
}