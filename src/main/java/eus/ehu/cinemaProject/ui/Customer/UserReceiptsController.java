package eus.ehu.cinemaProject.ui.Customer;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.configuration.UtilDate;
import eus.ehu.cinemaProject.domain.OrderStatus;
import eus.ehu.cinemaProject.domain.PurchaseReceipt;
import eus.ehu.cinemaProject.domain.Seat;
import eus.ehu.cinemaProject.domain.ShowTime;
import eus.ehu.cinemaProject.domain.users.Customer;
import eus.ehu.cinemaProject.ui.UIState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserReceiptsController {

    @FXML
    private VBox receiptsContainer;

    private BlFacadeImplementation bl;

    UIState uiState = UIState.getInstance();
    ResourceBundle bundle = uiState.getBundle();

    HashMap<PurchaseReceipt, VBox> receiptMap = new HashMap<>();

    @FXML
    public void initialize() {
        bl = BlFacadeImplementation.getInstance();
        List<PurchaseReceipt> receipts = bl.getPurchaseReceiptsByUser((Customer) uiState.getUser());
        loadReceipts(receipts);

        uiState.currentViewProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("userReceipts.fxml")) {
                loadReceipts(bl.getPurchaseReceiptsByUser((Customer) uiState.getUser()));
            }
        });
    }
    public void loadReceipts(List<PurchaseReceipt> receipts) {
        receiptsContainer.getChildren().clear();
        receiptMap.clear();

        for (PurchaseReceipt receipt : receipts) {
            VBox receiptBox = new VBox(8);
            receiptBox.setPadding(new Insets(10));
            receiptBox.setStyle("-fx-background-color: #333333; -fx-background-radius: 8;");
            receiptBox.setPrefWidth(550);

            Label filmLabel = new Label(bundle.getString("filmLabel") + " " + receipt.getShowTime().getFilm().getTitle()
            + "\t" + UtilDate.formatDateTime(receipt.getShowTime().getScreeningDate(), receipt.getShowTime().getScreeningTime()));
            filmLabel.setTextFill(Color.WHITE);


            Label dateLabel = new Label(bundle.getString("dateLabel") + " " + UtilDate.formatDate(receipt.getOrderDate()));
            dateLabel.setTextFill(Color.LIGHTGRAY);

            // Contenedor de asientos
            VBox seatsBox = new VBox(2);
            Label seatsTitle = new Label(bundle.getString("seatsTitle"));
            seatsTitle.setTextFill(Color.LIGHTGRAY);
            seatsBox.getChildren().add(seatsTitle);

            for (Seat seat : receipt.getBookedSeats()) {
                Label seatLabel = new Label(seat.toString());
                seatLabel.setTextFill(Color.LIGHTGRAY);
                seatsBox.getChildren().add(seatLabel);
            }

            Label priceLabel = new Label(bundle.getString("priceLabel") + " " + receipt.getTotalAmount() + "€");
            priceLabel.setTextFill(Color.GOLD);

            Label statusLabel = new Label(bundle.getString("statusLabel") + " " + receipt.translateStatus());

            updateStatusIfPast(receipt);

            Button rateButton = new Button(bundle.getString("rateButton"));
            rateButton.setStyle("-fx-background-color: #e31837; -fx-text-fill: white; -fx-cursor: hand;");
            rateButton.setOnAction(e -> handleRateFilm(receipt));
            rateButton.setDisable(receipt.getStatus() != OrderStatus.PAST |
                    bl.hasFilmBeenReviewed(receipt.getShowTime().getFilm(), (Customer) uiState.getUser()) );

            if(receipt.getStatus() != OrderStatus.PAST) {
                Button requestCancellation = new Button(bundle.getString("reqCancelButton"));
                requestCancellation.setStyle("-fx-background-color: #e31837; -fx-text-fill: white; -fx-cursor: hand;");
                requestCancellation.setOnAction(e -> requestCancellation(receipt));

                receiptBox.getChildren().addAll(filmLabel, dateLabel, seatsBox, priceLabel, statusLabel, rateButton, requestCancellation);
            }
            else {
                receiptBox.getChildren().addAll(filmLabel, dateLabel, seatsBox, priceLabel, statusLabel, rateButton);
            }
            receiptMap.put(receipt, receiptBox);
            receiptsContainer.getChildren().add(receiptBox);
        }
    }

    private void requestCancellation(PurchaseReceipt receipt) {
        LocalDateTime showDateTime = LocalDateTime.of(
                receipt.getShowTime().getScreeningDate(),
                receipt.getShowTime().getScreeningTime());
        if(showDateTime.isAfter(LocalDateTime.now().plusHours(3))) {
            bl.setOrderStatus(receipt, OrderStatus.PENDING_CANCELLATION);
            VBox receiptBox = receiptMap.get(receipt);
            receiptBox.getChildren().remove(6);
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("cancelError"));
            alert.showAndWait();
        }
    }

    private void handleRateFilm(PurchaseReceipt receipt) {
        updateStatusIfPast(receipt);
        if(receipt.getStatus() != OrderStatus.PAST) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("filmNotWatched"));
            alert.showAndWait();
            return;
        }
        // Verify if it has been reviewed
        if (bl.hasFilmBeenReviewed(receipt.getShowTime().getFilm(), (Customer) uiState.getUser())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("filmReviewError"));
            alert.showAndWait();
        } else {
            Optional<Pair<Integer, String>> result = obtainReview();
            result.ifPresent(pair -> {
                int rating = pair.getKey();
                String review = pair.getValue();
                bl.storeReview(receipt.getShowTime().getFilm(), rating, review, (Customer) uiState.getUser());
            });
        }
    }

    @FXML
    public Optional<Pair<Integer, String>> obtainReview(){
        Dialog<Pair<Integer, String>> reviewDialog = new Dialog<>();
        reviewDialog.setTitle(bundle.getString("reviewDialogTitle"));
        reviewDialog.setHeaderText(bundle.getString("reviewDialogHeader"));

        //Buttons
        ButtonType submitButtonType = new ButtonType(bundle.getString("submitButton"), ButtonBar.ButtonData.OK_DONE);
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
        reviewText.setPromptText(bundle.getString("reviewPrompt"));
        reviewText.setWrapText(true);
        reviewText.setPrefRowCount(4);

        //Design
        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label(bundle.getString("ratingLabel")),
                ratingSlider,
                new Label(bundle.getString("reviewLabel")),
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
    void goBack(ActionEvent event) {
        uiState.setCurrentView("MovieList.fxml");
        //user could log in and just view receipts. If it went back, they would see the login screen again
    }

    private void updateStatusIfPast(PurchaseReceipt receipt) {
        LocalDateTime showDateTime = LocalDateTime.of(
                receipt.getShowTime().getScreeningDate(),
                receipt.getShowTime().getScreeningTime());

        if (showDateTime.isBefore(LocalDateTime.now())) {
            bl.setOrderStatus(receipt, OrderStatus.PAST);
        }
    }
}