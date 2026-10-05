module ni.edu.uam.facturacionapp {
 requires javafx.controls;
 requires javafx.fxml;
 requires java.sql;
 requires org.postgresql.jdbc;
 opens ni.edu.uam.facturacionapp.Controller to javafx.fxml;
 exports ni.edu.uam.facturacionapp;
 exports ni.edu.uam.facturacionapp.Model;
}
