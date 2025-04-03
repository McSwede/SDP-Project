module org.grupp2.sdpproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.naming;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires org.hibernate.orm.core;
    requires org.locationtech.jts;
    requires org.hibernate.orm.spatial;
    requires org.hibernate.validator;
    requires jdk.xml.dom;

    opens org.grupp2.sdpproject.entities to org.hibernate.orm.core;

    exports org.grupp2.sdpproject.Utils to org.hibernate.orm.core;
    requires jbcrypt;

    opens org.grupp2.sdpproject to javafx.fxml;
    exports org.grupp2.sdpproject;
    exports org.grupp2.sdpproject.GUI;
    exports org.grupp2.sdpproject.entities;
    exports org.grupp2.sdpproject.ENUM;
    opens org.grupp2.sdpproject.GUI to javafx.fxml;

}