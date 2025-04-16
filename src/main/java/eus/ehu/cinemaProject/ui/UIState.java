package eus.ehu.cinemaProject.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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

    //Attributes to share between different view controllers


}
