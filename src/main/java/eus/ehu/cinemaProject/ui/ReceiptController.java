package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.PurchaseReceipt;
import eus.ehu.cinemaProject.domain.users.Customer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ReceiptController {

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

    private final UIState uiState = UIState.getInstance();

    @FXML
    void onReviewFilmClick(){
        if(tablePurchaseReceipts.getSelectionModel().getSelectedItem() != null){
            TextInputDialog dialog1 = new TextInputDialog();
            dialog1.setTitle("Film rating");
            dialog1.setHeaderText("Introduce a number from 1 to 5");
            dialog1.setContentText("Rating:");
            Optional<String> rating = dialog1.showAndWait();

            TextInputDialog dialog2 = new TextInputDialog();
            dialog2.setTitle("Film review");
            dialog2.setHeaderText("Write a review");
            dialog2.setContentText("Review:");
            Optional<String> review = dialog2.showAndWait();



        }
    }


    @FXML
    public void initialize() {

        bl = BlFacadeImplementation.getInstance();
        showTimeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));


        filmColumn.setCellValueFactory(cellData -> {
            PurchaseReceipt pr = cellData.getValue();
            return new SimpleStringProperty(pr.getShowTime().getFilm().getTitle());
        });
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        seatColumn.setCellValueFactory(new PropertyValueFactory<>("bookedSeats"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        purchaseReceipts = FXCollections.observableArrayList();
        List<PurchaseReceipt> receipts = bl.getPurchaseReceiptsByUser((Customer) uiState.getUser());
        purchaseReceipts.addAll(receipts);
        tablePurchaseReceipts.setItems(purchaseReceipts);

    }
}