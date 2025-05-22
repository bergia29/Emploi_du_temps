module fr.isep.algo.gestionemploisdutemps {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;
    requires com.google.gson;

    opens fr.isep.algo.gestionemploisdutemps to javafx.fxml, com.google.gson;
    exports fr.isep.algo.gestionemploisdutemps;
}