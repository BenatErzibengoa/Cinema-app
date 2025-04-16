package eus.ehu.cinemaProject.ui;

import eus.ehu.cinemaProject.domain.Film;
import eus.ehu.cinemaProject.domain.ShowTime;
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

    //Attributes to share between different view controllers (we have to add more!!!)
    private String email;

    private Film film;

    private List<ShowTime> showtimes;

    //Corresponding getters and setters to interchange data between controllers
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
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


}
