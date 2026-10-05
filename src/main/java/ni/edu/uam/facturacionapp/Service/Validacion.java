package ni.edu.uam.facturacionapp.Service;

import java.math.BigDecimal;
public final class Validacion {
 private Validacion() {}
 public static String texto(String valor,String campo,int max,boolean requerido) {
  String limpio=valor==null?"":valor.strip();
  if(requerido && limpio.isBlank()) throw new IllegalArgumentException(campo+" es obligatorio.");
  if(limpio.length()>max) throw new IllegalArgumentException(campo+": máximo "+max+" caracteres.");
  return limpio;
 }
 public static String campo(String valor,String etiqueta,String id,int max) {
  try {return texto(valor,etiqueta,max,true);}
  catch(IllegalArgumentException e) {throw new ValidacionException(id,e.getMessage());}
 }
 public static void seleccion(Integer id) {
  if(id==null || id<=0) throw new IllegalArgumentException("Debe seleccionar un registro.");
 }
 public static BigDecimal precio(String texto) {
  BigDecimal n;
  try { n=new BigDecimal(texto==null?"":texto.strip().replace(',', '.')); }
  catch(NumberFormatException ex) {throw new ValidacionException("precioVenta","El precio debe ser un valor numérico.");}
  if(n.signum()<=0) throw new ValidacionException("precioVenta","El precio de venta debe ser mayor que cero.");
  if(n.compareTo(new BigDecimal("9999999999.99"))>0 || n.stripTrailingZeros().scale()>2)
   throw new ValidacionException("precioVenta","El precio admite hasta 10 enteros y 2 decimales.");
  return n.setScale(2);
 }
 public static int existencia(String texto) {
  int n;
  try { n=Integer.parseInt(texto==null?"":texto.strip()); }
  catch(NumberFormatException ex) {throw new ValidacionException("existencia","La existencia debe ser un número entero entre 0 y 2147483647.");}
  if(n<0) throw new ValidacionException("existencia","La existencia no puede ser negativa.");
  return n;
 }
}
