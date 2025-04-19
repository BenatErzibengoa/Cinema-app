package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.domain.Film;
import eus.ehu.cinemaProject.domain.Seat;
import eus.ehu.cinemaProject.domain.ShowTime;
import eus.ehu.cinemaProject.domain.users.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

/**
 * Shared model class to store UI state between controllers
 */
public class UIState {

    // Singleton instance
    private static final UIState instance = new UIState();

    // The current view property that will be bound to UI elements
    private final StringProperty currentView = new SimpleStringProperty();

    private final BooleanProperty loggedIn = new SimpleBooleanProperty(false);


    private UIState() {
        // Private constructor for singleton
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

    //Attributes to share between different view controllers (we have to add more!!!)
    private String email;
    private Film selectedFilm;
    private ShowTime selectedShowtime;
    private List<Seat> selectedSeats;

    private User user;

    private Film film;

    private List<ShowTime> showtimes;

    //Corresponding getters and setters to interchange data between controllers
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
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

    public List<Seat> getSelectedSeats() { return selectedSeats; }
    public void setSelectedSeats(List<Seat> selectedSeats) { this.selectedSeats = selectedSeats; }



}
