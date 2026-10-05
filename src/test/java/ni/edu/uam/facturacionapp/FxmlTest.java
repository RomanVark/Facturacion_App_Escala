package ni.edu.uam.facturacionapp;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;
@EnabledIfEnvironmentVariable(named="RUN_UI_TESTS",matches="true")
class FxmlTest {
 @BeforeAll static void iniciar() throws Exception { CountDownLatch l=new CountDownLatch(1);Platform.startup(()->{Platform.setImplicitExit(false);l.countDown();});assertTrue(l.await(10,TimeUnit.SECONDS)); }
 @Test void todasLasVistasCarganConControladoresYCss() throws Exception {
  for(String archivo:new String[]{"Main.fxml","Categoria.fxml","Producto.fxml","Cliente.fxml"}) {
   FutureTask<Void> tarea=new FutureTask<>(()->{
    FXMLLoader loader=new FXMLLoader(Main.class.getResource(archivo));Parent root=loader.load();
    assertNotNull(loader.getController());assertFalse(root.getStylesheets().isEmpty());
    Stage stage=new Stage();stage.setScene(new Scene(root));stage.show();root.applyCss();root.layout();
    assertNotNull(root.lookup("#estado"));stage.close();return null;
   });Platform.runLater(tarea);tarea.get(20,TimeUnit.SECONDS);
  }
 }
 @Test void errorDeConexionNoBloqueaLaInterfaz() throws Exception {
  CompletableFuture<Boolean> recuperada=new CompletableFuture<>();
  Platform.runLater(()->{
   javafx.scene.layout.VBox root=new javafx.scene.layout.VBox();
   ni.edu.uam.facturacionapp.Controller.Ui.ejecutar(root,()->{throw new java.sql.SQLException("Conexión simulada", "08001");},ok->recuperada.complete(false),error->{
    if(root.isDisabled() || !ni.edu.uam.facturacionapp.Controller.Ui.mensaje(error).contains("conectar")){recuperada.complete(false);return;}
    ni.edu.uam.facturacionapp.Controller.Ui.ejecutar(root,()->true,ok->recuperada.complete(ok && !root.isDisabled()),e->recuperada.complete(false));
   });
  });assertTrue(recuperada.get(10,TimeUnit.SECONDS));
 }
 @Test void actualizarYEliminarSinSeleccionMuestranAdvertencia() throws Exception {
  for(String archivo:new String[]{"Categoria.fxml","Producto.fxml"}) {
   FutureTask<Void> tarea=new FutureTask<>(()->{
    FXMLLoader loader=new FXMLLoader(Main.class.getResource(archivo));Parent root=loader.load();
    Stage stage=new Stage();stage.setScene(new Scene(root));stage.show();
    ni.edu.uam.facturacionapp.Controller.CatalogoController<?> controller=loader.getController();
    for(Runnable accion:new Runnable[]{controller::actualizarSeleccionado,controller::eliminar}) {
     java.util.concurrent.atomic.AtomicReference<String> mensaje=new java.util.concurrent.atomic.AtomicReference<>();
     Platform.runLater(()->{
      for(javafx.stage.Window w:java.util.List.copyOf(javafx.stage.Window.getWindows())) {
       if(w.getScene()!=null && w.getScene().getRoot() instanceof javafx.scene.control.DialogPane dialog) {mensaje.set(dialog.getContentText());w.hide();}
      }
     });
     accion.run();assertNotNull(mensaje.get());assertTrue(mensaje.get().contains("Debe seleccionar"));
    }
    stage.close();return null;
   });Platform.runLater(tarea);tarea.get(20,TimeUnit.SECONDS);
  }
 }
}
