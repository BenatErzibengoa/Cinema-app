package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.domain.*;

import eus.ehu.cinemaProject.domain.users.User;
import eus.ehu.cinemaProject.ui.Customer.SeatSelectionController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Shared model class to store UI state between controllers
 */
public class UIState {

    // Singleton instance
    private static final UIState instance = new UIState();

    // The current view property that will be bound to UI elements
    private final StringProperty currentView = new SimpleStringProperty();

    private final BooleanProperty loggedIn = new SimpleBooleanProperty(false);

    private final ResourceBundle bundle;
    private SeatSelectionController seatSelectionController;


    private UIState() {
        // Private constructor for singleton
        Locale locale = Locale.forLanguageTag("es");
        bundle = ResourceBundle.getBundle("eus.ehu.cinemaProject.ui.Language", locale);
    }

    public static UIState getInstance() {
        return instance;
    }

    // Getter for the current view property
    public StringProperty currentViewProperty() {
        return currentView;
    }

    // Convenience getter for the current view value
    public String getCurrentView() {
        return currentView.get();
    }

    // Convenience setter for the current view value
    public void setCurrentView(String view) {
        currentView.set(view);
    }

    // Login state property
    public BooleanProperty loggedInProperty() {
        return loggedIn;
    }

    public boolean isLoggedIn() {
        return loggedIn.get();
    }

    public void setLoggedIn(boolean value) {
        loggedIn.set(value);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    //Attributes to share between different view controllers (we have to add more!!!)
    private String customerEmail;
    private String workerEmail;
    private Film selectedFilm;
    private ShowTime selectedShowtime;
    private List<Seat> selectedSeats;
    private PurchaseReceipt provisionalReceipt;
    private String lastView = "MovieList.fxml"; // Default view

    private User user;

    private Film film;

    private List<ShowTime> showtimes;
    private List<Review> reviews;

    public String summary;
    public double snackprice;


    //Corresponding getters and setters to interchange data between controllers
    public String getCustomerEmail() {
        return customerEmail;
    }
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    public String getWorkerEmail() { return workerEmail;}
    public void setWorkerEmail(String workerEmail) { this.workerEmail = workerEmail; }
    public User getUser(){return user;}
    public void setUser(User user){this.user = user;}
    public Film getFilm() {
        return film;
    }
    public void setFilm(Film film) {
        this.film = film;
    }
    public List<ShowTime> getShowtimes() {
        return showtimes;
    }
    public void setShowtimes(List<ShowTime> showtimes) {
        this.showtimes = showtimes;
    }
    public ShowTime getSelectedShowtime() { return selectedShowtime; }
    public void setSelectedShowtime(ShowTime selectedShowtime) { this.selectedShowtime = selectedShowtime; }
    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }
    public PurchaseReceipt getProvisionalReceipt() { return provisionalReceipt; }
    public void setProvisionalReceipt(PurchaseReceipt provisionalReceipt) { this.provisionalReceipt = provisionalReceipt; }

    public List<Seat> getSelectedSeats() { return selectedSeats; }
    public void setSelectedSeats(List<Seat> selectedSeats) { this.selectedSeats = selectedSeats; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public double getSnackprice() { return snackprice; }
    public void setSnackprice(double snackprice) { this.snackprice = snackprice; }

    public String getLastView() { return lastView; }
    public void setLastView(String lastView) { this.lastView = lastView; }

    //To update addShowtime after adding movies
    private final BooleanProperty movieListDirty = new SimpleBooleanProperty(false);

    public BooleanProperty movieListDirtyProperty() {
        return movieListDirty;
    }

    public boolean isMovieListDirty() {
        return movieListDirty.get();
    }

    public void setMovieListDirty(boolean dirty) {
        movieListDirty.set(dirty);
    }


    public void setSeatSelectionController(SeatSelectionController controller) {
        this.seatSelectionController = controller;
    }

    public SeatSelectionController getSeatSelectionController() {
        return seatSelectionController;
    }
}
