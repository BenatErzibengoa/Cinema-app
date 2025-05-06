package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.OrderStatus;
import eus.ehu.cinemaProject.domain.PurchaseReceipt;
import eus.ehu.cinemaProject.domain.users.Customer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserReceiptsController {

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
    private TableColumn<PurchaseReceipt, OrderStatus> statusColumn;

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
        List<PurchaseReceipt> receipts = bl.getPurchaseReceiptsByUser((Customer) uiState.getUser());
        purchaseReceipts.addAll(receipts);
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
    void onReviewFilmClick(){
        PurchaseReceipt selectedReceipt = tablePurchaseReceipts.getSelectionModel().getSelectedItem();
        if(selectedReceipt != null){
            // Check if the film has already been reviewed
            if (bl.hasFilmBeenReviewed(selectedReceipt.getShowTime().getFilm(), (Customer) uiState.getUser())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("You have already reviewed this film.");
                alert.showAndWait();
            }
            else{
                Optional<Pair<Integer, String>> result = obtainReview();
                if (result.isPresent()) {
                    int rating = result.get().getKey();
                    String review = result.get().getValue();
                    bl.storeReview(selectedReceipt.getShowTime().getFilm(), rating, review, (Customer) uiState.getUser());
                }
            }
        }
    }

    @FXML
    public Optional<Pair<Integer, String>> obtainReview(){
        Dialog<Pair<Integer, String>> reviewDialog = new Dialog<>();
        reviewDialog.setTitle("Film Review");
        reviewDialog.setHeaderText("Rate the film and write your review");

        //Buttons
        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        reviewDialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);
        //Slider
        Slider ratingSlider = new Slider(1, 5, 3);
        ratingSlider.setMajorTickUnit(1);
        ratingSlider.setMinorTickCount(0);
        ratingSlider.setSnapToTicks(true);
        ratingSlider.setShowTickMarks(true);
        ratingSlider.setShowTickLabels(true);

        //Review TextArea
        TextArea reviewText = new TextArea();
        reviewText.setPromptText("Write your review here...");
        reviewText.setWrapText(true);
        reviewText.setPrefRowCount(4);

        //Design
        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Rating:"),
                ratingSlider,
                new Label("Review:"),
                reviewText
        );
        reviewDialog.getDialogPane().setContent(content);

        //Return result as pair
        reviewDialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                return new Pair<>((int) ratingSlider.getValue(), reviewText.getText());
            }
            return null;
        });
        return reviewDialog.showAndWait();
    }

    @FXML
    void cancelPurchase() {
        PurchaseReceipt selectedReceipt = tablePurchaseReceipts.getSelectionModel().getSelectedItem();
        LocalDateTime showDateTime = LocalDateTime.of(
                selectedReceipt.getShowTime().getScreeningDate(),
                selectedReceipt.getShowTime().getScreeningTime()
        );

        if((selectedReceipt != null) &&
                selectedReceipt.getStatus() == OrderStatus.PAID &&
                showDateTime.isAfter(LocalDateTime.now().plusHours(1))
        ) {
            bl.setOrderStatus(selectedReceipt, OrderStatus.CANCELLATION_PENDING);
            tablePurchaseReceipts.refresh(); // Refresh the table to reflect the changes
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