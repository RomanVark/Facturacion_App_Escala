module ni.edu.uam.facturacion_app_escala {
 requires javafx.controls;
 requires javafx.fxml;
 requires java.sql;
 requires org.postgresql.jdbc;
 opens ni.edu.uam.facturacion_app_escala.Controller to javafx.fxml;
 exports ni.edu.uam.facturacion_app_escala;
 exports ni.edu.uam.facturacion_app_escala.Model;
}
