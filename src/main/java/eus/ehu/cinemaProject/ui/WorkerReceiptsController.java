package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.configuration.UtilDate;
import eus.ehu.cinemaProject.domain.OrderStatus;
import eus.ehu.cinemaProject.domain.PurchaseReceipt;
import javafx.beans.property.SimpleLongProperty;
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

public class WorkerReceiptsController {

    @FXML
    private TableView<PurchaseReceipt> tablePurchaseReceipts;

    @FXML
    private TableColumn<PurchaseReceipt, Long> customerIdColumn;

    @FXML
    private TableColumn<PurchaseReceipt, String> filmColumn;

    @FXML
    private TableColumn<PurchaseReceipt, String> dateColumn;

    @FXML
    private TableColumn<PurchaseReceipt, Double> priceColumn;

    @FXML
    private TableColumn<PurchaseReceipt, String> statusColumn;

    private ObservableList<PurchaseReceipt> purchaseReceipts;
    private BlFacadeImplementation bl;
    private final UIState uiState = UIState.getInstance();

    @FXML
    public void initialize() {
        bl = BlFacadeImplementation.getInstance();

        customerIdColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getCustomer().getId()).asObject());

        filmColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getShowTime().getFilm().getTitle()));

        dateColumn.setCellValueFactory(cellData -> {
            Date date = cellData.getValue().getOrderDate();
            return new SimpleStringProperty(UtilDate.formatDate(date));
        });

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().translateStatus()));

        purchaseReceipts = FXCollections.observableArrayList();
        purchaseReceipts.addAll(bl.getPendingCancellationPurchaseReceipts());
        tablePurchaseReceipts.setItems(purchaseReceipts);


        tablePurchaseReceipts.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateStatusIfPast(newValue);
            }
        });

        uiState.currentViewProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("workerReceipts.fxml")) {
                purchaseReceipts.clear();
                purchaseReceipts.addAll(bl.getPendingCancellationPurchaseReceipts());
            }
        });
    }

    @FXML
    void cancelPurchase() {
        PurchaseReceipt selectedReceipt = tablePurchaseReceipts.getSelectionModel().getSelectedItem();
        updateStatusIfPast(selectedReceipt);
        if (selectedReceipt != null && selectedReceipt.getStatus() != OrderStatus.PAST) {
            bl.setOrderStatus(selectedReceipt, OrderStatus.CANCELLED);
            tablePurchaseReceipts.refresh();
        }
    }

    private void updateStatusIfPast(PurchaseReceipt receipt) {
        LocalDateTime showDateTime = LocalDateTime.of(
                receipt.getShowTime().getScreeningDate(),
                receipt.getShowTime().getScreeningTime());

        if (showDateTime.isBefore(LocalDateTime.now())) {
            bl.setOrderStatus(receipt, OrderStatus.PAST);
            tablePurchaseReceipts.refresh();
        }
    }
}
