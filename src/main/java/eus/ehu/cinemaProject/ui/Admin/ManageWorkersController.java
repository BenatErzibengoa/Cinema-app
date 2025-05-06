package eus.ehu.cinemaProject.ui.Admin;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.users.Worker;
import eus.ehu.cinemaProject.ui.UIState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageWorkersController {

    @FXML
    private VBox workersContainer;

    @FXML
    private TableView<Worker> tableWorkers;

    private final BlFacadeImplementation bl = BlFacadeImplementation.getInstance();
    private final UIState uiState = UIState.getInstance();
    private final ResourceBundle bundle = ResourceBundle.getBundle("eus.ehu.cinemaProject.ui.Language", Locale.getDefault());

    private ObservableList<Worker> workers;

    @FXML
    public void initialize() {
        workers = FXCollections.observableArrayList(
                bl.getAllWorkers().stream()
                        .filter(w -> !(w instanceof eus.ehu.cinemaProject.domain.users.Admin))
                        .toList()
        );
        loadWorkers();
    }

    @FXML
    void addWorker(ActionEvent event) {
        Dialog<Worker> dialog = new Dialog<>();
        dialog.setTitle(bundle.getString("hireB"));
        dialog.setHeaderText(bundle.getString("dialog.add.header"));

        ButtonType addButtonType = new ButtonType(bundle.getString("hireB"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField emailField = new TextField();
        TextField passwordField = new TextField();
        TextField nameField = new TextField();
        TextField surnameField = new TextField();
        TextField salaryField = new TextField();

        grid.add(new Label(bundle.getString("emailL") + ":"), 0, 0);     grid.add(emailField, 1, 0);
        grid.add(new Label(bundle.getString("passwordL") + ":"), 0, 1);  grid.add(passwordField, 1, 1);
        grid.add(new Label(bundle.getString("nameL") + ":"), 0, 2);      grid.add(nameField, 1, 2);
        grid.add(new Label(bundle.getString("suenameL") + ":"), 0, 3);   grid.add(surnameField, 1, 3);
        grid.add(new Label(bundle.getString("salaryL") + ":"), 0, 4);    grid.add(salaryField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.addEventFilter(ActionEvent.ACTION, e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            String name = nameField.getText();
            String surname = surnameField.getText();
            String salaryStr = salaryField.getText();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty() || salaryStr.isEmpty()) {
                showAlert(bundle.getString("error.fill.all"));
                e.consume();
                return;
            }

            if (!validateEmail(email)) {
                showAlert(bundle.getString("error.invalid.email"));
                e.consume();
                return;
            }

            if (password.length() < 5) {
                showAlert(bundle.getString("error.short.password"));
                e.consume();
                return;
            }

            if (bl.getUserByEmail(email) != null) {
                showAlert(bundle.getString("error.duplicate.email") + " " + email);
                e.consume();
                return;
            }

            try {
                Integer.parseInt(salaryStr);
            } catch (NumberFormatException ex) {
                showAlert(bundle.getString("error.invalid.salary"));
                e.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String email = emailField.getText();
                String password = passwordField.getText();
                String name = nameField.getText();
                String surname = surnameField.getText();
                int salary = Integer.parseInt(salaryField.getText());
                return (Worker) bl.signUpWorker(email, password, name, surname, salary);
            }
            return null;
        });

        Optional<Worker> result = dialog.showAndWait();
        result.ifPresent(worker -> {
            workers.add(worker);
            loadWorkers();
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(bundle.getString("error.title"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateEmail(String email){
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    private void loadWorkers() {
        workersContainer.getChildren().clear();
        for (Worker worker : workers) {
            VBox workerBox = new VBox(5);
            workerBox.setPadding(new Insets(10));
            workerBox.setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 8;");
            workerBox.setPrefWidth(550);

            Label nameLabel = new Label(bundle.getString("nameL") + ": " + worker.getName() + " " + worker.getSurname());
            nameLabel.setTextFill(Color.WHITE);

            Label emailLabel = new Label(bundle.getString("emailL") + ": " + worker.getEmail());
            emailLabel.setTextFill(Color.LIGHTGRAY);

            Label salaryLabel = new Label(bundle.getString("salaryL") + ": " + worker.getSalary());
            salaryLabel.setTextFill(Color.GOLD);

            Button deleteBtn = new Button(bundle.getString("fireB"));
            deleteBtn.setStyle("-fx-background-color: #e31837; -fx-text-fill: white; -fx-cursor: hand;");
            deleteBtn.setOnAction(e -> {
                bl.deleteWorker(worker);
                workers.remove(worker);
                loadWorkers();
            });

            workerBox.getChildren().addAll(nameLabel, emailLabel, salaryLabel, deleteBtn);
            workersContainer.getChildren().add(workerBox);
        }
    }

    @FXML
    void goBack(ActionEvent event) {
        uiState.setCurrentView("adminMain.fxml");
    }
}