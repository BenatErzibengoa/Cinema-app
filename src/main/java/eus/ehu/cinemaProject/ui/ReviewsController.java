package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.Review;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ReviewsController {


    private List<Review> reviews;
    private final UIState uiState = UIState.getInstance();
    private final ResourceBundle bundle = uiState.getBundle();


    @FXML
    void initialize() {
        loadReviews(uiState.getReviews());
    }

    @FXML
    private VBox reviewsContainer;

    public void loadReviews(List<Review> reviews) {
        reviewsContainer.getChildren().clear();

        for (Review review : reviews) {
            VBox reviewBox = new VBox(5);
            reviewBox.setPadding(new Insets(10));
            reviewBox.setStyle("-fx-background-color: #333333; -fx-background-radius: 8;");
            reviewBox.setPrefWidth(550);

            Label nameLabel = new Label(bundle.getString("reviewerLabel") + " " + review.getAuthor().getName());
            nameLabel.setTextFill(javafx.scene.paint.Color.WHITE);

            Label dateLabel = new Label(bundle.getString("dateLabel") + " " + review.getDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            dateLabel.setTextFill(javafx.scene.paint.Color.LIGHTGRAY);

            Label scoreLabel = new Label(bundle.getString("ratingLabel") + " " + review.getRating() + "/5");
            scoreLabel.setTextFill(javafx.scene.paint.Color.GOLD);

            Label commentLabel = new Label(review.getTextReview());
            commentLabel.setWrapText(true);
            commentLabel.setTextFill(javafx.scene.paint.Color.WHITE);

            reviewBox.getChildren().addAll(nameLabel, dateLabel, scoreLabel, commentLabel);
            reviewsContainer.getChildren().add(reviewBox);
        }
    }


    public void goBack(ActionEvent actionEvent) {
        uiState.setCurrentView("showTime.fxml");
    }
}
