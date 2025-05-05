package eus.ehu.cinemaProject.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        UIState uiState = UIState.getInstance();
        // to change the language, go to constructor of UIState

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("menuLayout.fxml"), uiState.getBundle()); //Menu --> SignIn
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CineFlix");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}