package ni.edu.uam.facturacionapp;
import ni.edu.uam.facturacionapp.Model.*;
import ni.edu.uam.facturacionapp.Service.*;
import ni.edu.uam.facturacionapp.Controller.Ui;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
class ReglasServiceTest {
 private Producto producto() {return new Producto(null,"P001","Producto",new Categoria(1,"Categoría",true),new BigDecimal("1.50"),0,"",true);}
 @Test void categoriaVaciaYConEspacios() {
  for(String nombre:new String[]{null,"","   ","\t"})assertThrows(IllegalArgumentException.class,()->new CategoriaService().validar(new Categoria(null,nombre,true)));
 }
 @Test void productoObligatorios() {
  ProductoService s=new ProductoService();Producto p=producto();p.setCodigo(" ");Producto sinCodigo=p;assertThrows(IllegalArgumentException.class,()->s.validar(sinCodigo));
  p=producto();p.setNombre(" ");Producto sinNombre=p;assertThrows(IllegalArgumentException.class,()->s.validar(sinNombre));
  p=producto();p.setCategoria(null);Producto sinCategoria=p;assertThrows(IllegalArgumentException.class,()->s.validar(sinCategoria));
 }
 @Test void productoPrecioYExistencia() {
  ProductoService s=new ProductoService();
  for(String precio:new String[]{"0","-15.50"}) {Producto p=producto();p.setPrecioVenta(new BigDecimal(precio));assertThrows(IllegalArgumentException.class,()->s.validar(p));}
  Producto p=producto();p.setExistencia(-3);assertThrows(IllegalArgumentException.class,()->s.validar(p));
  assertDoesNotThrow(()->s.validar(producto()));
 }
 @Test void actualizarOEliminarSinSeleccionNoAccedeALaBase() {
  CategoriaService c=new CategoriaService();ProductoService p=new ProductoService();
  assertThrows(IllegalArgumentException.class,()->c.actualizar(new Categoria(null,"Nombre",true)));
  assertThrows(IllegalArgumentException.class,()->p.actualizar(producto()));
  assertThrows(IllegalArgumentException.class,()->c.eliminar(null));assertThrows(IllegalArgumentException.class,()->p.eliminar(null));
 }
 @Test void erroresComprensiblesSinDetallesSql() {
  assertEquals("El precio debe ser un valor numérico.",assertThrows(IllegalArgumentException.class,()->Validacion.precio("abc")).getMessage());
  assertTrue(assertThrows(IllegalArgumentException.class,()->Validacion.existencia("diez")).getMessage().contains("entero"));
  assertTrue(assertThrows(IllegalArgumentException.class,()->Validacion.existencia("10.5")).getMessage().contains("entero"));
  assertEquals("La existencia no puede ser negativa.",assertThrows(IllegalArgumentException.class,()->Validacion.existencia("-3")).getMessage());
  assertFalse(Ui.mensaje(new SQLException("SELECT secreto", "42000")).contains("secreto"));
  assertTrue(Ui.mensaje(new SQLException("internal", "08001")).contains("conectar"));
 }
}
