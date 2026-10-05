package ni.edu.uam.facturacionapp.Service;

import ni.edu.uam.facturacionapp.Model.Categoria;
import ni.edu.uam.facturacionapp.DAO.CategoriaDao;
import java.sql.SQLException;
import java.util.List;
public class CategoriaService {
 private final CategoriaDao dao=new CategoriaDao();
 public List<Categoria> listar() throws SQLException {return dao.listar();}
 public void validar(Categoria e) {
  if(e==null) throw new IllegalArgumentException("Debe indicar una categoría.");
  e.setNombre(Validacion.campo(e.getNombre(),"El nombre de la categoría","nombre",100));
 }
 private void duplicado(Categoria e,Integer excluirId) throws SQLException {
  if(dao.existeNombre(e.getNombre(),excluirId)) throw new ValidacionException("nombre","Ya existe una categoría con ese nombre.");
 }
 public void guardar(Categoria e) throws SQLException {
  validar(e);
  if(e.getId()!=null) throw new IllegalArgumentException("Para editar use Actualizar seleccionado.");
  duplicado(e,null);dao.guardar(e);
 }
 public void actualizar(Categoria e) throws SQLException {
  Validacion.seleccion(e==null?null:e.getId());validar(e);
  if(dao.buscar(e.getId()).isEmpty()) throw new IllegalArgumentException("La categoría seleccionada ya no existe. Recargue la tabla.");
  duplicado(e,e.getId());dao.actualizar(e);
 }
 public void eliminar(Integer id) throws SQLException {
  Validacion.seleccion(id);
  if(dao.tieneProductos(id)) throw new IllegalArgumentException("No puede eliminar la categoría porque tiene productos asociados.");
  dao.eliminar(id);
 }
}
