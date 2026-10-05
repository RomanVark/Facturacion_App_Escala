package ni.edu.uam.facturacionapp;
import ni.edu.uam.facturacionapp.DAO.*;
import ni.edu.uam.facturacionapp.Model.*;
import ni.edu.uam.facturacionapp.Service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
@EnabledIfEnvironmentVariable(named="RUN_DB_TESTS",matches="true")
class DuplicadosPostgresTest {
 @Test void duplicadosActualizacionesYReferencias() throws Exception {
  CategoriaService cs=new CategoriaService();ProductoService ps=new ProductoService();
  CategoriaDao cd=new CategoriaDao();ProductoDao pd=new ProductoDao();String token=UUID.randomUUID().toString();
  Categoria a=new Categoria(null,"CAT-"+token,true),b=new Categoria(null,"OTRA-"+token,true);
  Producto p=new Producto(null,"A-"+token,"Prueba",a,new BigDecimal("3.50"),0,"",true);
  Producto q=new Producto(null,"B-"+token,"Otro",b,new BigDecimal("4.00"),1,"",false);
  try {
   cs.guardar(a);cs.guardar(b);
   assertThrows(IllegalArgumentException.class,()->cs.guardar(new Categoria(null,"  "+a.getNombre().toLowerCase()+"  ",true)));
   assertEquals("23505",assertThrows(SQLException.class,()->cd.guardar(new Categoria(null,a.getNombre().toLowerCase(),true))).getSQLState());
   cs.actualizar(a); // No se considera duplicado a sí misma.
   String nombreB=b.getNombre();b.setNombre(a.getNombre());assertThrows(IllegalArgumentException.class,()->cs.actualizar(b));b.setNombre(nombreB);
   ps.guardar(p);ps.guardar(q);ps.actualizar(p);
   assertThrows(IllegalArgumentException.class,()->ps.guardar(new Producto(null,p.getCodigo(),"Duplicado",a,BigDecimal.ONE,0,"",true)));
   String codigoQ=q.getCodigo();q.setCodigo(p.getCodigo());assertThrows(IllegalArgumentException.class,()->ps.actualizar(q));q.setCodigo(codigoQ);
   assertEquals("No puede eliminar la categoría porque tiene productos asociados.",assertThrows(IllegalArgumentException.class,()->cs.eliminar(a.getId())).getMessage());
   assertTrue(cd.buscar(a.getId()).isPresent());assertTrue(pd.buscar(p.getId()).isPresent());
   Producto cero=new Producto(null,"C-"+token,"Cero",a,BigDecimal.ZERO,0,"",true);
   assertEquals("23514",assertThrows(SQLException.class,()->pd.guardar(cero)).getSQLState());
   Producto sinCategoria=new Producto(null,"D-"+token,"Referencia",new Categoria(Integer.MAX_VALUE,"Ausente",true),BigDecimal.ONE,0,"",true);
   assertThrows(IllegalArgumentException.class,()->ps.guardar(sinCategoria));
   p.setExistencia(4);ps.actualizar(p);assertEquals(4,pd.buscar(p.getId()).orElseThrow().getExistencia());
   // UPDATE de un registro eliminado no vuelve a insertarlo.
   pd.eliminar(q.getId());assertThrows(IllegalArgumentException.class,()->ps.actualizar(q));assertTrue(pd.buscar(q.getId()).isEmpty());q.setId(null);
  } finally {
   if(q.getId()!=null)pd.eliminar(q.getId());if(p.getId()!=null)pd.eliminar(p.getId());
   if(b.getId()!=null)cd.eliminar(b.getId());if(a.getId()!=null)cd.eliminar(a.getId());
  }
 }
}
