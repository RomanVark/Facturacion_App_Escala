module ni.edu.uam.facturacion_app_escala {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens ni.edu.uam.facturacion_app_escala to javafx.fxml;
    exports ni.edu.uam.facturacion_app_escala;
}