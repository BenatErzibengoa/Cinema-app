module eus.ehu.cinemaproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens eus.ehu.cinemaproject to javafx.fxml;
    exports eus.ehu.cinemaproject;
    exports eus.ehu.cinemaproject.ui;
    opens eus.ehu.cinemaproject.ui to javafx.fxml;
}