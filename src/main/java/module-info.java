module org.grupp2.sdpproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires org.hibernate.validator;
    requires org.hibernate.orm.core;

    opens org.grupp2.sdpproject to javafx.fxml;
    exports org.grupp2.sdpproject;
}