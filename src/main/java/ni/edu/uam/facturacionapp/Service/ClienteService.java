package ni.edu.uam.facturacionapp.Service;

import ni.edu.uam.facturacionapp.Model.Cliente;
import ni.edu.uam.facturacionapp.DAO.ClienteDao;
import java.sql.SQLException;
import java.util.List;
public class ClienteService {
 private final ClienteDao dao=new ClienteDao();
 public List<Cliente> listar() throws SQLException { return dao.listar(); }
 private void validar(Cliente e) {
  if(e==null) throw new IllegalArgumentException("Debe indicar un cliente.");
  e.setNombre(Validacion.texto(e.getNombre(), "Nombre",150,true));
  e.setDocumento(Validacion.texto(e.getDocumento(), "Documento",50,true));
  e.setTelefono(Validacion.texto(e.getTelefono(), "Teléfono",30,false));
  e.setCorreo(Validacion.texto(e.getCorreo(), "Correo",150,false));
  e.setDireccion(Validacion.texto(e.getDireccion(), "Dirección",300,false));
  if(!e.getCorreo().isEmpty() && !e.getCorreo().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) throw new IllegalArgumentException("El correo no tiene un formato válido.");

 }
 public void guardar(Cliente e) throws SQLException { validar(e);if(e.getId()!=null)throw new IllegalArgumentException("Para editar use Actualizar seleccionado.");dao.guardar(e); }
 public void actualizar(Cliente e) throws SQLException { Validacion.seleccion(e==null?null:e.getId());validar(e);dao.actualizar(e); }
 public void eliminar(Integer id) throws SQLException { Validacion.seleccion(id);dao.eliminar(id); }
}
