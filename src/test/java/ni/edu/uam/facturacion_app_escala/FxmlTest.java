package ni.edu.uam.facturacion_app_escala;
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
}
