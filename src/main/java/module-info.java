module eus.ehu.cinemaProject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires com.h2database;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;
    requires com.google.gson;
    requires jbcrypt;

    opens eus.ehu.cinemaProject.domain to javafx.base, org.hibernate.orm.core;

    opens eus.ehu.cinemaProject.domain.users to javafx.base, org.hibernate.orm.core;
    opens eus.ehu.cinemaProject.ui to javafx.fxml;

    exports eus.ehu.cinemaProject.ui;
    exports eus.ehu.cinemaProject.domain;


}