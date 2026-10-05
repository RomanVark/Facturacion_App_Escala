package ni.edu.uam.facturacionapp;
import ni.edu.uam.facturacionapp.Service.Validacion;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
class ValidacionTest {
 @Test void precioDecimalExacto() { assertEquals(new BigDecimal("85.50"),Validacion.precio("85,50")); }
 @Test void rechazaPrecioInvalido() { for(String s:new String[]{"0","-1","NaN","1.234","10000000000",""}) assertThrows(IllegalArgumentException.class,()->Validacion.precio(s)); }
 @Test void existenciaEnteraNoNegativa() { assertEquals(0,Validacion.existencia("0")); for(String s:new String[]{"-1","2.5","2147483648",""}) assertThrows(IllegalArgumentException.class,()->Validacion.existencia(s)); }
 @Test void textoObligatorioYLongitud() { assertEquals("P001",Validacion.texto(" P001 ","Código",50,true));assertThrows(IllegalArgumentException.class,()->Validacion.texto("  ","Nombre",100,true));assertThrows(IllegalArgumentException.class,()->Validacion.texto("abcd","Nombre",3,true)); }
}
