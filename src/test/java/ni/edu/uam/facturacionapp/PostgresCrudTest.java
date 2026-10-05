package ni.edu.uam.facturacionapp;
import ni.edu.uam.facturacionapp.DAO.*;
import ni.edu.uam.facturacionapp.Model.*;
import ni.edu.uam.facturacionapp.Config.DatabaseConnection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
@EnabledIfEnvironmentVariable(named="RUN_DB_TESTS",matches="true")
class PostgresCrudTest {
 @Test void cicloCompletoConRestricciones() throws Exception {
  assertTrue(DatabaseConnection.probarConexion());
  CategoriaDao categorias=new CategoriaDao();ProductoDao productos=new ProductoDao();ClienteDao clientes=new ClienteDao();
  String sufijo=UUID.randomUUID().toString();
  Categoria c=new Categoria(null,"Prueba "+sufijo,true);
  Producto p=new Producto(null,sufijo,"Producto de prueba",c,new BigDecimal("12.34"),3,"",true);
  Cliente cl=new Cliente(null,"O'Connor",sufijo,"8888","demo@example.com","Managua",true);
  try {
   categorias.guardar(c);productos.guardar(p);clientes.guardar(cl);
   assertNotNull(c.getId());assertNotNull(p.getId());assertNotNull(cl.getId());
   assertTrue(categorias.listar().stream().anyMatch(x->x.getId().equals(c.getId())));
   assertTrue(productos.listar().stream().anyMatch(x->x.getId().equals(p.getId())));
   assertTrue(clientes.listar().stream().anyMatch(x->x.getId().equals(cl.getId())));
   assertEquals("O'Connor",clientes.buscar(cl.getId()).orElseThrow().getNombre());
   assertEquals(c.getId(),productos.buscar(p.getId()).orElseThrow().getCategoria().getId());
   assertEquals(new BigDecimal("12.34"),productos.buscar(p.getId()).orElseThrow().getPrecioVenta());
   assertEquals("23503",assertThrows(SQLException.class,()->categorias.eliminar(c.getId())).getSQLState());
   assertEquals("23505",assertThrows(SQLException.class,()->productos.guardar(p)).getSQLState());
   assertEquals("23505",assertThrows(SQLException.class,()->clientes.guardar(cl)).getSQLState());
   c.setNombre("Categoría editada");c.setActiva(false);categorias.actualizar(c);
   p.setNombre("Producto editado");p.setExistencia(7);p.setActivo(false);productos.actualizar(p);
   cl.setNombre("Cliente editado");cl.setActivo(false);clientes.actualizar(cl);
   assertFalse(categorias.buscar(c.getId()).orElseThrow().isActiva());
   assertEquals(7,productos.buscar(p.getId()).orElseThrow().getExistencia());
   assertFalse(clientes.buscar(cl.getId()).orElseThrow().isActivo());
   assertTrue(new ResumenDao().obtener().productos()>=1);
  } finally {
   if(p.getId()!=null)productos.eliminar(p.getId());
   if(cl.getId()!=null)clientes.eliminar(cl.getId());
   if(c.getId()!=null)categorias.eliminar(c.getId());
  }
  assertTrue(productos.buscar(p.getId()).isEmpty());assertTrue(categorias.buscar(c.getId()).isEmpty());assertTrue(clientes.buscar(cl.getId()).isEmpty());
 }
}
