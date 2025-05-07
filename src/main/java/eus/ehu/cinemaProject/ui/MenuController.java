package eus.ehu.cinemaProject.ui;
import eus.ehu.cinemaProject.businessLogic.BlFacadeImplementation;
import eus.ehu.cinemaProject.ui.Customer.SeatSelectionController;
import eus.ehu.cinemaProject.ui.User.MovieListController;
import eus.ehu.cinemaProject.domain.users.Admin;
import eus.ehu.cinemaProject.domain.users.User;
import eus.ehu.cinemaProject.domain.users.Worker;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class MenuController {

    @FXML
    private BorderPane contentPane;

    @FXML
    private Button receiptsButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Text titleText; //Théo

    BlFacadeImplementation bl;

    Locale locale = Locale.forLanguageTag("es");
    ResourceBundle bundle = ResourceBundle.getBundle("eus.ehu.cinemaProject.ui.Language", locale);


    @FXML
    private void showMovieList() {
        User user = bl.getUserByEmail(uiState.getWorkerEmail());
        if(!(user instanceof Admin)){
            uiState.setSummary("");
            uiState.setSnackprice(0.0);
            if(user instanceof Worker)
                loadContent("workerMenu.fxml");
            else
                loadContent("MovieList.fxml");
        }
        contentCache.clear();
    }


    @FXML
    void initialize() {
        bl = BlFacadeImplementation.getInstance();

        showMovieList(); // Default behavior for other users
        registerButton.setVisible(true);


        // Add listeners for view and login state changes
        uiState.currentViewProperty().addListener((obs, oldView, newView) -> {
            loadContent(newView);
        });



        uiState.loggedInProperty().addListener((obs, wasLoggedIn, isNowLoggedIn) -> {
            if (isNowLoggedIn) {
                User loggedInUser = uiState.getUser();
                loginButton.setVisible(false);
                registerButton.setText(bundle.getString("logout"));
                receiptsButton.setVisible(!(bl.getUserByEmail(uiState.getWorkerEmail()) instanceof Admin));

                registerButton.onMouseClickedProperty().set(event -> {
                    uiState.setUser(null);
                    uiState.setWorkerEmail(null);
                    uiState.setCustomerEmail(null);
                    registerButton.setText("Register");
                    loginButton.setVisible(true);
                    receiptsButton.setVisible(false);
                    uiState.setLoggedIn(false);
                    contentCache.clear(); // Clear the cache on logout
                    loadContent("MovieList.fxml");
                });
            } else {
                loginButton.setVisible(true);
                receiptsButton.setVisible(false);
                registerButton.setVisible(true);
                registerButton.setText("Register");
                registerButton.onMouseClickedProperty().set(event -> {
                    loadContent("signup.fxml");
                });
            }
        });
        uiState.setMenuController(this);
    }



    @FXML
    void loginPane(ActionEvent event) {
        loadContent("signin.fxml");
    }

    @FXML
    void registerPane(ActionEvent event) {
        loadContent("signup.fxml");
    }

    @FXML
    void receiptsPane(ActionEvent event) {
        if(uiState.getWorkerEmail() == null)
            loadContent("userReceipts.fxml");
        else
            loadContent("workerReceipts.fxml");
    }

    // Reference to the UIState
    private final UIState uiState = UIState.getInstance();

    private Map<String, Pane> contentCache = new HashMap<>();


    private void loadContent(String fxmlFile) {
        try {
            // Check if content is already cached
            Pane content = contentCache.get(fxmlFile);
            if (content == null) {
                // If not cached, load it and store in cache
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile), bundle);
                content = loader.load();
                contentCache.put(fxmlFile, content);
                if (loader.getController() instanceof MovieListController) {
                    ((MovieListController) loader.getController()).setBusinessLogic(bl);
                }
                if (loader.getController() instanceof SeatSelectionController) {
                    uiState.setSeatSelectionController((SeatSelectionController) loader.getController());
                }
            }
            contentPane.setCenter(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearCache(){
        contentCache.clear();
    }
    public void clearReceipt(){
        if(contentCache.containsKey("receipt.fxml")){
            contentCache.remove("receipt.fxml");
        }
    }




}