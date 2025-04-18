package eus.ehu.cinemaProject.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Menu extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Menu.class.getResource("menuLayout.fxml")); // Menu --> catalogue
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CineFlix");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}