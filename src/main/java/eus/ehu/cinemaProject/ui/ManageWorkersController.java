package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.users.Worker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class ManageWorkersController {

    @FXML
    private TableColumn<Worker, String> emailColumn;

    @FXML
    private TableColumn<Worker, String> nameColumn;

    @FXML
    private TableColumn<Worker, String> surnameColumn;

    @FXML
    private TableColumn<Worker, Integer> salaryColumn;

    @FXML
    private TableView<Worker> tableWorkers;
    BlFacadeImplementation bl;
    private ObservableList<Worker> workers;
    private final UIState uiState = UIState.getInstance();

    @FXML
    void addWorker(ActionEvent event) {
        Dialog<Worker> dialog = new Dialog<>();
        dialog.setTitle("Add Worker");
        dialog.setHeaderText("Enter the worker's details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
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

        grid.add(new Label("Email:"), 0, 0);     grid.add(emailField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);  grid.add(passwordField, 1, 1);
        grid.add(new Label("Name:"), 0, 2);      grid.add(nameField, 1, 2);
        grid.add(new Label("Surname:"), 0, 3);   grid.add(surnameField, 1, 3);
        grid.add(new Label("Salary:"), 0, 4);    grid.add(salaryField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Prevent dialog from closing if validation fails
        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.addEventFilter(ActionEvent.ACTION, e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            String name = nameField.getText();
            String surname = surnameField.getText();
            String salaryStr = salaryField.getText();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty() || salaryStr.isEmpty()) {
                showAlert("Fill all fields");
                e.consume(); // Prevent dialog from closing
                return;
            }

            if (!validateEmail(email)) {
                showAlert("Enter a valid email");
                e.consume();
                return;
            }

            if (password.length() < 5) {
                showAlert("Password must be at least 5 characters long");
                e.consume();
                return;
            }

            if (bl.getUserByEmail(email) != null) {
                showAlert("There is already an account related to " + email);
                e.consume();
                return;
            }

            try {
                Integer.parseInt(salaryStr);
            } catch (NumberFormatException ex) {
                showAlert("Salary must be a valid number");
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
        result.ifPresent(worker -> workers.add(worker));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateEmail(String email){
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }




    @FXML
    void deleteWorker(ActionEvent event) {
        Worker selectedWorker = tableWorkers.getSelectionModel().getSelectedItem();
        if (selectedWorker != null) {
            bl.deleteWorker(selectedWorker);
            workers.remove(selectedWorker);
        }

    }

    @FXML
    public void initialize(){
        bl=BlFacadeImplementation.getInstance();
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));

        workers= FXCollections.observableArrayList(
                bl.getAllWorkers().stream()
                        .filter(worker -> !(worker instanceof eus.ehu.cinemaProject.domain.users.Admin))
                        .toList()
        );
        tableWorkers.setItems(workers);
    }

    @FXML
    void goBack(ActionEvent event) {
        uiState.setCurrentView("adminMain.fxml");
    }

}
