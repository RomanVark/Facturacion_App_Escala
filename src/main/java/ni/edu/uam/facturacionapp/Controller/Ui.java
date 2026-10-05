package ni.edu.uam.facturacionapp.Controller;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import java.sql.SQLException;
import org.postgresql.util.PSQLException;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
public final class Ui {
 private Ui() {}
 public static <T> void ejecutar(Pane root,Callable<T> trabajo,Consumer<T> listo,Consumer<Throwable> error) {
  root.setDisable(true);
  Task<T> task=new Task<>() { @Override protected T call() throws Exception {return trabajo.call();} };
  task.setOnSucceeded(e->{root.setDisable(false);listo.accept(task.getValue());});
  task.setOnFailed(e->{root.setDisable(false);error.accept(task.getException());});
  Thread hilo=new Thread(task,"consulta-catalogos");hilo.setDaemon(true);hilo.start();
 }
 public static String mensaje(Throwable e) {
  if(e instanceof SQLException sql) {
   String estado=sql.getSQLState();
   String restriccion=sql instanceof PSQLException pg && pg.getServerErrorMessage()!=null?pg.getServerErrorMessage().getConstraint():null;
   if("23505".equals(estado)) {
    if("uq_categoria_nombre_normalizado".equals(restriccion))return "Ya existe una categoría con ese nombre.";
    if("producto_codigo_key".equals(restriccion))return "Ya existe un producto con ese código.";
    if("cliente_documento_key".equals(restriccion))return "Ya existe un cliente con ese documento.";
    return "Ya existe un registro con esos datos únicos.";
   }
   if("23503".equals(estado))return "La operación afecta una categoría con productos asociados o una categoría que ya no existe. Recargue las tablas.";
   if("23514".equals(estado))return "Revise los datos: el precio debe ser mayor que cero y la existencia no puede ser negativa.";
   if(estado!=null && estado.startsWith("08"))return "No se pudo conectar con la base de datos. Revise el servidor y la configuración de conexión e intente de nuevo.";
   if("28P01".equals(estado))return "Usuario o contraseña de la base de datos incorrectos.";
   if("42P01".equals(estado))return "Faltan tablas en la base de datos. Ejecute los scripts de creación.";
   return "No fue posible completar la operación en la base de datos. Intente nuevamente.";
  }
  if(e instanceof IllegalArgumentException)return e.getMessage();
  return "No fue posible completar la operación. Intente nuevamente.";
 }
 public static void advertencia(String mensaje) {mostrar(Alert.AlertType.WARNING,"Validación",mensaje);}
 private static void mostrar(Alert.AlertType tipo,String titulo,String mensaje) {
  Alert a=new Alert(tipo);a.setTitle(titulo);a.setHeaderText(titulo);a.setContentText(mensaje);a.showAndWait();
 }
 public static void error(Throwable e) {
  if(e instanceof IllegalArgumentException)advertencia(mensaje(e));
  else {
   System.err.println("Operación fallida: "+e.getClass().getSimpleName()+(e instanceof SQLException s?" SQLState="+s.getSQLState():""));
   mostrar(Alert.AlertType.ERROR,"No se pudo completar la operación",mensaje(e));
  }
 }
}
