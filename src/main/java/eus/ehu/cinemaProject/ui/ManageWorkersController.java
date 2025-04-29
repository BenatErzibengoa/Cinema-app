package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.domain.users.Worker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManageWorkersController {

    @FXML
    private TableColumn<Worker, String> emailColumn;

    @FXML
    private TableColumn<Worker, String> nameColumn;

    @FXML
    private TableColumn<Worker, String> surnameColumn;

    @FXML
    private TableView<Worker> tableWorkers;
    BlFacadeImplementation bl;
    private ObservableList<Worker> workers;

    @FXML
    void addWorker(ActionEvent event) {


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

        workers= FXCollections.observableArrayList(
                bl.getAllWorkers().stream()
                        .filter(worker -> !(worker instanceof eus.ehu.cinemaProject.domain.users.Admin))
                        .toList()
        );
        tableWorkers.setItems(workers);
    }

}
