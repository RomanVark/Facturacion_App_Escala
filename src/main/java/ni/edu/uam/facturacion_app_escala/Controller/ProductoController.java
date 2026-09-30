package ni.edu.uam.facturacion_app_escala.Controller;

import ni.edu.uam.facturacion_app_escala.Model.*;
import ni.edu.uam.facturacion_app_escala.Service.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.List;
public class ProductoController extends CatalogoController<Producto> {
 @FXML private TextField codigo,nombre,precioVenta,existencia,rutaImagen;
 @FXML private ComboBox<Categoria> categoria;
 @FXML private CheckBox activo;
 @FXML private ImageView vistaImagen;
 private final ProductoService service=new ProductoService();
 private List<Categoria> categorias=List.of();
 protected void configurar() {
  columna(0,e->e.getId().toString());columna(1,Producto::getCodigo);columna(2,Producto::getNombre);columna(3,e->e.getCategoria().getNombre());columna(4,e->e.getPrecioVenta().toPlainString());columna(5,e->String.valueOf(e.getExistencia()));columna(6,e->e.isActivo()?"Activo":"Inactivo");
  rutaImagen.textProperty().addListener((o,a,b)->previsualizar(b));
 }
 protected List<Producto> cargar() throws Exception {categorias=new CategoriaService().listar();return service.listar();}
 protected void despuesDeCargar() {categoria.getItems().setAll(categorias);}
 protected Producto formulario() {return new Producto(seleccionado==null?null:seleccionado.getId(),codigo.getText(),nombre.getText(),categoria.getValue(),Validacion.precio(precioVenta.getText()),Validacion.existencia(existencia.getText()),rutaImagen.getText(),activo.isSelected());}
 protected void mostrar(Producto e) {
  codigo.setText(e.getCodigo());nombre.setText(e.getNombre());precioVenta.setText(e.getPrecioVenta().toPlainString());existencia.setText(String.valueOf(e.getExistencia()));rutaImagen.setText(e.getRutaImagen());activo.setSelected(e.isActivo());
  categoria.setValue(categorias.stream().filter(c->c.getId().equals(e.getCategoria().getId())).findFirst().orElse(e.getCategoria()));
 }
 protected void limpiar() {codigo.clear();nombre.clear();precioVenta.setText("0.00");existencia.setText("0");rutaImagen.clear();categoria.setValue(null);activo.setSelected(true);}
 protected void persistir(Producto e) throws Exception {service.guardar(e);}
 protected void borrar(Producto e) throws Exception {service.eliminar(e.getId());}
 protected String textoBusqueda(Producto e) {return e.getId()+" "+e.getCodigo()+" "+e.getNombre()+" "+e.getCategoria().getNombre();}
 @FXML private void seleccionarImagen() {
  FileChooser selector=new FileChooser();selector.setTitle("Imagen del producto");selector.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes","*.png","*.jpg","*.jpeg","*.gif"));
  File f=selector.showOpenDialog(root.getScene().getWindow());if(f!=null)rutaImagen.setText(f.getAbsolutePath());
 }
 private void previsualizar(String ruta) {
  vistaImagen.setImage(null);
  if(ruta==null || ruta.isBlank())return;
  File archivo=new File(ruta); if(!archivo.isFile())return;
  try {vistaImagen.setImage(new Image(archivo.toURI().toString(),140,90,true,true,true));} catch(IllegalArgumentException ignored) { /* Ruta opcional: no impide editar el producto. */ }
 }
}
