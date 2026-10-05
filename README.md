# FacturaciónAPP — Gestión de catálogos

Aplicación de escritorio JavaFX para administrar **categorías, productos y clientes**. Incluye un menú principal con resumen de las tres tablas y ventanas independientes para cada catálogo.

## Requisitos

- JDK 17 o superior (configurar `JAVA_HOME`).
- PostgreSQL y una base local `tienda_javafx`.
- Acceso a Internet la primera vez para descargar las dependencias Maven.
- Scene Builder compatible con JavaFX 21 para editar las vistas (opcional para ejecutar).

## 1. Crear la base y las tablas

En pgAdmin, abre Query Tool conectado a `postgres` y ejecuta `database/01-crear-base.sql` una sola vez. Luego abre Query Tool conectado a **tienda_javafx** y ejecuta `database/02-tablas.sql`. El documento SQL `database/03-datos-ejemplo.sql` incluye **5 categorías, 10 productos (dos por categoría) y 10 clientes ficticios**, con instrucciones y consultas para verificar las cantidades. Puedes volver a ejecutarlo: omite nombres de categoría, códigos y documentos existentes sin modificar sus datos. En una base inicialmente vacía deja 25 registros en total.

Alternativa desde terminal:

```sh
psql -U postgres -d postgres -f database/01-crear-base.sql
psql -U postgres -d tienda_javafx -f database/02-tablas.sql
psql -U postgres -d tienda_javafx -f database/03-datos-ejemplo.sql
```

El esquema respeta los campos de categoría y producto de la guía y agrega clientes con nombre, documento, teléfono, correo, dirección y estado. Estos campos de cliente se propusieron porque las imágenes no definen su modelo. La clave foránea impide eliminar categorías con productos. Los códigos de producto y documentos de cliente son únicos; el precio debe ser mayor que cero y la existencia no puede ser negativa.

## 2. Configurar y ejecutar

Desde la raíz del proyecto, en **PowerShell (Windows)**:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/tienda_javafx"
$env:DB_USER = "postgres"
$env:DB_PASSWORD = "TU_CONTRASEÑA"
.\mvnw.cmd clean javafx:run
```

En Linux o macOS:

```sh
export DB_URL='jdbc:postgresql://localhost:5432/tienda_javafx'
export DB_USER='postgres'
export DB_PASSWORD='TU_CONTRASEÑA'
bash mvnw clean javafx:run
```

También puedes abrir `pom.xml` como proyecto Maven en IntelliJ IDEA, configurar las variables de entorno en la ejecución y lanzar `Launcher`. No hay que instalar Maven por separado: se incluye Maven Wrapper.

La aplicación no crea tablas automáticamente. El botón **Probar conexión** comprueba JDBC; el resumen informa si faltan tablas o hay problemas de conexión. Las consultas corren fuera del hilo gráfico.

## 3. Usar los catálogos

1. Abre **Categorías**, escribe un nombre y pulsa **Guardar nuevo**.
2. Abre **Productos**, selecciona una categoría, completa código, nombre, precio y existencia. La ruta de imagen es opcional y **Examinar** permite elegir un archivo local.
3. Abre **Clientes** y completa nombre y documento, además de los datos opcionales.
4. Selecciona una fila para editar y pulsa **Actualizar seleccionado**. **Nuevo / Limpiar** prepara un registro nuevo.
5. Usa el buscador para filtrar o **Eliminar seleccionado** para borrar con confirmación.
6. **Actualizar tabla** vuelve a consultar los datos y, en Productos, actualiza el ComboBox de categorías. Las categorías inactivas se conservan disponibles para editar registros existentes.

El resumen se actualiza al recuperar el foco, al cerrar una ventana secundaria o al pulsar **Actualizar resumen**. Una imagen se guarda como ruta local, no como archivo en PostgreSQL; debe seguir existiendo en la computadora donde se abre la aplicación. Los estados activo/inactivo son editables; el botón eliminar realiza un borrado físico.

## 4. Editar con Scene Builder

Abre directamente cualquiera de estos archivos:

- `src/main/resources/ni/edu/uam/facturacionapp/Main.fxml`
- `src/main/resources/ni/edu/uam/facturacionapp/Categoria.fxml`
- `src/main/resources/ni/edu/uam/facturacionapp/Producto.fxml`
- `src/main/resources/ni/edu/uam/facturacionapp/Cliente.fxml`

El estilo está en `styles.css`, junto a los FXML, y se referencia mediante `@styles.css`. Se usan controles estándar de JavaFX. Conserva `fx:controller`, los `fx:id` y los métodos `onAction` al editar. La vista previa de Scene Builder muestra el diseño; los datos se cargan al ejecutar Java.

## Estructura conservada

```text
src/main/java/ni/edu/uam/facturacionapp/
  Config/       Conexión JDBC mediante variables de entorno
  Model/        Categoria, Producto y Cliente
  Crud/         Contrato CRUD genérico
  DAO/          SQL parametrizado, mapeo y resumen
  Service/      Validaciones y operaciones de negocio
  Controller/   Eventos, tablas, formularios y ventanas
  Main.java     Aplicación JavaFX
  Launcher.java Punto de entrada alternativo
src/main/resources/ni/edu/uam/facturacionapp/
  Main.fxml, Categoria.fxml, Producto.fxml, Cliente.fxml, styles.css
database/       Scripts PostgreSQL
src/test/java/  Pruebas de validación, DAO y carga de FXML
```

## Correspondencia con la guía

| Requisito | Implementación |
|---|---|
| Base PostgreSQL y tablas | `database/01-crear-base.sql`, `02-tablas.sql` |
| JavaFX y Maven | `pom.xml`, `Main`, `Launcher` |
| Modelos y relación categoría-producto | `Model/Categoria`, `Model/Producto`, FK `categoria_id` |
| JDBC y prueba de conexión | `Config/DatabaseConnection`, botón del menú principal |
| DAO guardar, listar, buscar, actualizar, eliminar | `CategoriaDao`, `ProductoDao`, `ClienteDao` |
| PreparedStatement | Todas las operaciones SQL con parámetros para valores de usuario |
| Formulario de categorías y productos | `Categoria.fxml`, `Producto.fxml` |
| Categorías en ComboBox | `ProductoController` |
| Productos en TableView | `Producto.fxml` y `ProductoController` |
| Ventanas de clientes y resumen | `Cliente.fxml`, `Main.fxml`, `ResumenDao` |
| Diseño editable y CSS | Cuatro FXML estándar y `styles.css` |

## Pruebas

`bash mvnw test` (Windows: `.\mvnw.cmd test`) ejecuta validaciones. Las pruebas de integración se habilitan únicamente con `RUN_DB_TESTS=true`, y las pruebas FXML con `RUN_UI_TESTS=true`. Usa **una base de pruebas** con las tablas ya creadas y configura `DB_URL`, `DB_USER`, `DB_PASSWORD` apuntando a ella. Las pruebas DAO crean registros temporales y los eliminan al terminar.

El workflow `.github/workflows/java.yml` prepara PostgreSQL 16, ejecuta las pruebas y carga las cuatro vistas bajo Xvfb. Las pruebas FXML comprueban carga, controladores y CSS; no sustituyen una revisión visual manual en Scene Builder.

## Validaciones de la práctica

Para una base de datos ya existente, ejecuta `database/04-validaciones.sql` después de revisar que no haya categorías repetidas ni productos con precio cero. El script informa los conflictos y revierte la transacción sin cambiar registros. Agrega unicidad de categorías ignorando mayúsculas y espacios y exige precio positivo.

Se consultan duplicados antes de INSERT y UPDATE, excluyendo el ID que se está editando. La base mantiene las restricciones UNIQUE y la clave foránea para proteger frente a cambios concurrentes. Antes de eliminar una categoría se consulta si tiene productos asociados.

**Guardar nuevo** crea un registro; **Actualizar seleccionado** exige selección y nunca inserta. **Actualizar tabla** solo consulta datos. Los errores de validación muestran una advertencia y devuelven el foco al campo; los errores SQL muestran un mensaje comprensible. El formulario sigue disponible para corregir y reintentar. Precio acepta coma o punto decimal, debe ser mayor que cero y tener hasta dos decimales. Existencia acepta cero o enteros positivos.

Las pruebas cubren nombres vacíos, categorías duplicadas sin distinguir mayúsculas, códigos duplicados, exclusión del propio ID al editar, precios cero/negativos/no numéricos, existencias negativas/decimales/no numéricas, categoría inexistente, eliminación con productos, actualización de un registro eliminado y acciones sin selección. También se simula un fallo de conexión y una operación posterior para comprobar que la interfaz vuelve a habilitarse. Las menciones a SQL Server en la guía se aplican aquí al motor PostgreSQL ya utilizado por el proyecto.
