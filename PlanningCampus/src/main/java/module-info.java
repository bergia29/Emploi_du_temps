module fr.planningcampus.planningcampus {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    // Commenté car incompatible avec Java 17
    // requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires itextpdf;

    // Permettre l'accès aux packages iText
    opens fr.planningcampus.planningcampus to javafx.fxml, itextpdf;
    exports fr.planningcampus.planningcampus;

    opens fr.planningcampus.planningcampus.controller to javafx.fxml, itextpdf;
    exports fr.planningcampus.planningcampus.controller;

    opens fr.planningcampus.planningcampus.model to javafx.base, itextpdf;
}