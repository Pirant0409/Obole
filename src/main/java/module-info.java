module com.pirant.obole {
    requires javafx.controls;
    requires javafx.fxml;
    requires javax.jmdns;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.databind;

    opens com.pirant.obole to javafx.fxml;
    exports com.pirant.obole.app;
    exports com.pirant.obole.controller;
    opens com.pirant.obole.controller to javafx.fxml;
    opens com.pirant.obole.app to javafx.fxml;
    opens com.pirant.obole.model to com.fasterxml.jackson.databind;
}