module eus.ehu.cinemaProject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires com.h2database;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;


    opens eus.ehu.cinemaProject.domain to org.hibernate.orm.core;
    opens eus.ehu.cinemaProject.domain.users to org.hibernate.orm.core;
    opens eus.ehu.cinemaProject.ui to javafx.fxml;

    exports eus.ehu.cinemaProject.ui;

}