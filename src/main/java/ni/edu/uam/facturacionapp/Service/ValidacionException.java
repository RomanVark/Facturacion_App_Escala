package ni.edu.uam.facturacionapp.Service;

/** Error esperado con el identificador FXML del campo que requiere corrección. */
public class ValidacionException extends IllegalArgumentException {
 private final String campo;
 public ValidacionException(String campo,String mensaje) { super(mensaje);this.campo=campo; }
 public String getCampo() { return campo; }
}
