package ni.edu.uam.facturacion_app_escala.Service;

import ni.edu.uam.facturacion_app_escala.Model.Categoria;
import ni.edu.uam.facturacion_app_escala.DAO.CategoriaDao;
import java.sql.SQLException;
import java.util.List;
public class CategoriaService {
 private final CategoriaDao dao=new CategoriaDao();
 public List<Categoria> listar() throws SQLException { return dao.listar(); }
 public void guardar(Categoria e) throws SQLException {
  e.setNombre(Validacion.texto(e.getNombre(), "Nombre",100,true));
  if(e.getId()==null) dao.guardar(e); else dao.actualizar(e);
 }
 public void eliminar(Integer id) throws SQLException { dao.eliminar(id); }
}
