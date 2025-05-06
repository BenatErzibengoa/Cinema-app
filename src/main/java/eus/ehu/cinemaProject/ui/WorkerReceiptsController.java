package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.OrderStatus;
import eus.ehu.cinemaProject.domain.PurchaseReceipt;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class WorkerReceiptsController {
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

    @FXML
    private TableColumn<PurchaseReceipt, OrderStatus> statusColumn;     // TODO: Remove this column when done

    private ObservableList<PurchaseReceipt> purchaseReceipts;

    private BlFacadeImplementation bl;

    private final UIState uiState = UIState.getInstance();

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
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        purchaseReceipts = FXCollections.observableArrayList();
        List<PurchaseReceipt> receipts = bl.getPendingCancellationPurchaseReceipts();
        System.out.println("Pending cancellation receipts: " + receipts);
        purchaseReceipts.addAll(receipts);
        System.out.println("Loading all pending cancellation receipts");
        tablePurchaseReceipts.setItems(purchaseReceipts);


        // Add a listener to observe changes in the list
        purchaseReceipts.addListener(
                (ListChangeListener<PurchaseReceipt>) change -> {
                    while (change.next()) {
                        if (change.wasUpdated()) {
                            for (PurchaseReceipt receipt : change.getRemoved()) {
                                updateStatusIfPast(receipt);
                            }
                        }
                    }
                }
        );


    }

    @FXML
    void cancelPurchase() {
        PurchaseReceipt selectedReceipt = tablePurchaseReceipts.getSelectionModel().getSelectedItem();
        if((selectedReceipt != null) && selectedReceipt.getStatus() != OrderStatus.PAST) {
            bl.setOrderStatus(selectedReceipt, OrderStatus.CANCELLED);
        }
    }

    private void updateStatusIfPast(PurchaseReceipt receipt) {
        LocalDateTime showDateTime = LocalDateTime.of(
                receipt.getShowTime().getScreeningDate(),
                receipt.getShowTime().getScreeningTime()
        );

        if (showDateTime.isBefore(LocalDateTime.now())) {
            bl.setOrderStatus(receipt, OrderStatus.PAST);
            tablePurchaseReceipts.refresh(); // Refresh the table to reflect the changes
        }
    }
}
