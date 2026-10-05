package ni.edu.uam.facturacionapp.Service;

import ni.edu.uam.facturacionapp.Model.Producto;
import ni.edu.uam.facturacionapp.DAO.ProductoDao;
import ni.edu.uam.facturacionapp.DAO.CategoriaDao;
import java.sql.SQLException;
import java.util.List;
public class ProductoService {
 private final ProductoDao dao=new ProductoDao();
 public List<Producto> listar() throws SQLException {return dao.listar();}
 public void validar(Producto e) {
  if(e==null) throw new IllegalArgumentException("Debe indicar un producto.");
  e.setCodigo(Validacion.campo(e.getCodigo(),"El código","codigo",50));
  e.setNombre(Validacion.campo(e.getNombre(),"El nombre del producto","nombre",150));
  if(e.getCategoria()==null || e.getCategoria().getId()==null)
   throw new ValidacionException("categoria","Debe seleccionar una categoría.");
  e.setPrecioVenta(Validacion.precio(e.getPrecioVenta()==null?null:e.getPrecioVenta().toPlainString()));
  Validacion.existencia(String.valueOf(e.getExistencia()));
  e.setRutaImagen(Validacion.texto(e.getRutaImagen(),"Ruta de imagen",500,false));
 }
 private void relacionesYDuplicados(Producto e,Integer excluirId) throws SQLException {
  if(new CategoriaDao().buscar(e.getCategoria().getId()).isEmpty()) throw new ValidacionException("categoria","La categoría seleccionada ya no existe. Recargue la tabla.");
  if(dao.existeCodigo(e.getCodigo(),excluirId)) throw new ValidacionException("codigo","Ya existe un producto con ese código.");
 }
 public void guardar(Producto e) throws SQLException {
  validar(e);
  if(e.getId()!=null) throw new IllegalArgumentException("Para editar use Actualizar seleccionado.");
  relacionesYDuplicados(e,null);dao.guardar(e);
 }
 public void actualizar(Producto e) throws SQLException {
  Validacion.seleccion(e==null?null:e.getId());validar(e);
  if(dao.buscar(e.getId()).isEmpty()) throw new IllegalArgumentException("El producto seleccionado ya no existe. Recargue la tabla.");
  relacionesYDuplicados(e,e.getId());dao.actualizar(e);
 }
 public void eliminar(Integer id) throws SQLException {Validacion.seleccion(id);dao.eliminar(id);}
}
