---
title: "Sistema de Gestión de Productos"
subtitle: "Persistencia de datos en Java mediante JDBC y MySQL"
---

# Portada

**SISTEMA DE GESTIÓN DE PRODUCTOS**
Persistencia de datos en Java mediante JDBC y MySQL

Presentado por: [NOMBRE COMPLETO DEL ESTUDIANTE]
Código estudiantil: [CÓDIGO]

Presentado a: [NOMBRE DEL DOCENTE]

Asignatura: [NOMBRE DE LA ASIGNATURA]
Programa: [PROGRAMA ACADÉMICO]
[NOMBRE DE LA UNIVERSIDAD]
[CIUDAD], [MES] de [AÑO]

# Introducción

La persistencia de datos es un componente fundamental del desarrollo de software, porque permite que la información que maneja una aplicación se conserve después de cerrar el programa. Sin persistencia, todos los datos registrados durante una ejecución se pierden al terminarla, lo que hace inútil una aplicación de gestión.

En Java, una de las formas más utilizadas para persistir información es mediante una base de datos relacional y la API JDBC (Java Database Connectivity), que permite que el programa envíe consultas SQL a un sistema gestor de bases de datos y reciba sus resultados.

Este documento describe el diseño, la implementación y las pruebas del **Sistema de Gestión de Productos**, una aplicación de escritorio desarrollada en Java con interfaz gráfica Swing, que se conecta a una base de datos MySQL mediante JDBC y permite realizar las operaciones CRUD (crear, consultar, actualizar y eliminar) sobre los productos de un pequeño establecimiento comercial.

# Objetivo general

Desarrollar una aplicación en Java que implemente mecanismos de persistencia de datos mediante la conexión con una base de datos MySQL, aplicando operaciones CRUD y buenas prácticas de programación, y comunicar en inglés las características y funcionalidades de la solución desarrollada.

# Objetivos específicos

1. Comprender los conceptos fundamentales de persistencia de datos y su importancia en el desarrollo de software.
2. Implementar las operaciones CRUD (crear, consultar, actualizar y eliminar) sobre productos mediante Java.
3. Establecer una conexión entre Java y MySQL utilizando JDBC y consultas parametrizadas con PreparedStatement.
4. Desarrollar una aplicación funcional y documentar su ejecución mediante un video explicativo en inglés.

# Marco conceptual

| Concepto | Descripción |
|---|---|
| Persistencia de datos | Conservación de la información para utilizarla posteriormente, incluso después de cerrar la aplicación. |
| JDBC | API estándar de Java (paquete `java.sql`) para conectarse e interactuar con bases de datos relacionales mediante un driver. |
| Base de datos relacional | Sistema que organiza la información en tablas formadas por filas y columnas, que pueden relacionarse entre sí. |
| MySQL | Sistema gestor de bases de datos relacional de código abierto que utiliza el lenguaje SQL. |
| CRUD | Operaciones básicas sobre registros: *Create* (crear), *Read* (consultar), *Update* (actualizar) y *Delete* (eliminar). |
| PreparedStatement | Interfaz de JDBC para ejecutar consultas parametrizadas (con `?`), lo que separa el SQL de los datos y reduce el riesgo de inyección SQL. |
| DAO | Patrón de diseño *Data Access Object* que separa el acceso a los datos de la lógica de negocio y de la presentación. |
| Swing | Biblioteca de Java para construir interfaces gráficas de escritorio (ventanas, botones, tablas). |
| Maven | Herramienta que gestiona la construcción del proyecto y descarga automáticamente sus dependencias. |

# Planteamiento del problema

Un pequeño establecimiento comercial necesita llevar el registro de sus productos: código, nombre, descripción, precio y cantidad disponible. Si este registro se hace en papel o en una aplicación que solo guarda los datos en memoria, la información se pierde, se duplica o se vuelve difícil de consultar y actualizar.

Se requiere una aplicación sencilla que permita registrar y administrar los productos y que almacene la información en una base de datos, de modo que siga disponible cuando el usuario vuelva a ejecutar el programa.

# Requerimientos funcionales

| ID | Requerimiento | Descripción | Implementación |
|---|---|---|---|
| RF1 | Registrar productos | Ingresar código, nombre, descripción, precio y cantidad disponible. | Botón **Registrar** → `ProductoDAO.registrar()` |
| RF2 | Consultar productos | Mostrar todos los productos y buscar por código o por nombre. | Botones **Consultar / Actualizar tabla** y **Buscar** → `listarTodos()`, `buscarPorCodigo()`, `buscarPorNombre()` |
| RF3 | Actualizar productos | Modificar precio, descripción o cantidad de un producto existente. | Botón **Actualizar** → `ProductoDAO.actualizar()` |
| RF4 | Eliminar productos | Eliminar un producto por su código, previa confirmación. | Botón **Eliminar** + `JOptionPane.showConfirmDialog` → `ProductoDAO.eliminar()` |
| RF5 | Persistir la información | Los registros permanecen después de cerrar y reiniciar la aplicación. | Almacenamiento en la tabla `productos` de MySQL |

# Requerimientos técnicos

| Elemento | Selección |
|---|---|
| Lenguaje | Java 17 o superior |
| Entorno de desarrollo | IntelliJ IDEA, NetBeans o Eclipse |
| Base de datos | MySQL 8.x |
| Conectividad | JDBC con el driver oficial MySQL Connector/J 8.4.0 |
| Interfaz | Swing |
| Gestión de dependencias | Maven |
| Control de versiones | Git y GitHub |

Condiciones técnicas cumplidas:

- Se implementan las cuatro operaciones CRUD.
- Se crea la tabla `productos` para almacenar los datos.
- Todas las consultas utilizan `PreparedStatement`.
- Se valida que el precio no sea negativo y que la cantidad sea un entero no negativo.
- Se gestionan los errores de conexión y las excepciones de base de datos.
- Las conexiones y demás recursos se cierran con *try-with-resources*.
- Se separan las clases de modelo, acceso a datos, conexión y presentación.

# Diseño de la solución

La solución se diseñó en cuatro capas sencillas, cada una con una responsabilidad clara:

1. **Modelo** (`Producto`): representa los datos de un producto.
2. **Conexión** (`ConexionBD`): concentra la configuración de MySQL y crea las conexiones.
3. **Acceso a datos** (`ProductoDAO`): contiene todas las sentencias SQL.
4. **Vista** (`ProductoFrame`): interfaz gráfica que recibe los datos, los valida y muestra los resultados.

El flujo de una operación es: el usuario pulsa un botón en la ventana → la vista valida los datos → la vista llama a un método del DAO → el DAO solicita una conexión a `ConexionBD` → ejecuta el SQL con `PreparedStatement` → devuelve el resultado a la vista → la vista actualiza la tabla y muestra un mensaje con `JOptionPane`.

**Atributos del producto:**

| Atributo | Tipo en Java | Descripción |
|---|---|---|
| id | `int` | Identificador generado por MySQL |
| codigo | `String` | Código único del producto (obligatorio) |
| nombre | `String` | Nombre del producto (obligatorio) |
| descripcion | `String` | Descripción (opcional) |
| precio | `double` | Precio unitario (≥ 0) |
| cantidad | `int` | Unidades disponibles (entero ≥ 0) |

**Diagrama de clases.** El diagrama se encuentra en el archivo `docs/diagrama_clases.puml` (formato PlantUML).

[INSERTAR IMAGEN: DIAGRAMA DE CLASES]

# Arquitectura del proyecto

```
sistema-gestion-productos/
├── pom.xml
├── database/script.sql
├── docs/
└── src/main/java/com/umb/productos/
    ├── Main.java
    ├── conexion/ConexionBD.java
    ├── dao/ProductoDAO.java
    ├── modelo/Producto.java
    └── vista/ProductoFrame.java
```

| Clase | Paquete | Responsabilidad |
|---|---|---|
| `Main` | `com.umb.productos` | Punto de entrada: aplica la apariencia del sistema y abre la ventana en el hilo de eventos de Swing. |
| `Producto` | `modelo` | Clase modelo con atributos, constructores, *getters* y *setters*. |
| `ConexionBD` | `conexion` | Concentra la configuración de conexión (host, puerto, base de datos y usuario; la contraseña la lee de `conexion.properties`) y devuelve una `Connection` mediante `DriverManager`. |
| `ProductoDAO` | `dao` | Implementa el CRUD con `PreparedStatement` y `ResultSet`; no contiene código de interfaz. |
| `ProductoFrame` | `vista` | Ventana Swing con campos, botones, `JTable`, validaciones y mensajes; no contiene SQL. |

# Diseño de la base de datos

Base de datos: `sistema_productos`. Tabla: `productos`.

| Columna | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | INT | PRIMARY KEY, AUTO_INCREMENT | Identificador interno |
| codigo | VARCHAR(20) | NOT NULL, UNIQUE | Código del producto |
| nombre | VARCHAR(100) | NOT NULL | Nombre del producto |
| descripcion | VARCHAR(255) | NULL | Descripción |
| precio | DECIMAL(10,2) | NOT NULL, CHECK (precio >= 0) | Precio unitario |
| cantidad | INT | NOT NULL, CHECK (cantidad >= 0) | Unidades disponibles |

Script de creación (`database/script.sql`):

```sql
CREATE DATABASE IF NOT EXISTS sistema_productos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE sistema_productos;

CREATE TABLE IF NOT EXISTS productos (
    id          INT            NOT NULL AUTO_INCREMENT,
    codigo      VARCHAR(20)    NOT NULL,
    nombre      VARCHAR(100)   NOT NULL,
    descripcion VARCHAR(255)   NULL,
    precio      DECIMAL(10,2)  NOT NULL,
    cantidad    INT            NOT NULL,
    CONSTRAINT pk_productos PRIMARY KEY (id),
    CONSTRAINT uq_productos_codigo UNIQUE (codigo),
    CONSTRAINT ck_productos_precio CHECK (precio >= 0),
    CONSTRAINT ck_productos_cantidad CHECK (cantidad >= 0)
);
```

Las restricciones `CHECK` y `UNIQUE` refuerzan en la base de datos las mismas reglas que valida la aplicación, de modo que los datos permanecen correctos aunque se inserten por otro medio.

# Explicación de JDBC

JDBC es la API estándar de Java para trabajar con bases de datos relacionales. La aplicación no habla directamente con MySQL, sino con las interfaces de JDBC; el driver **MySQL Connector/J** (declarado como dependencia en el `pom.xml`) traduce esas llamadas al protocolo de MySQL.

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.4.0</version>
</dependency>
```

Elementos de JDBC usados en el proyecto:

| Elemento | Uso en el proyecto |
|---|---|
| `DriverManager` | Crea la conexión a partir de la URL, el usuario y la contraseña (`ConexionBD.obtenerConexion()`). |
| `Connection` | Representa la sesión abierta con MySQL. |
| `PreparedStatement` | Ejecuta las sentencias SQL con parámetros `?`. |
| `ResultSet` | Recorre las filas devueltas por un `SELECT`. |
| `SQLException` | Informa los errores de conexión o de SQL. |

La URL de conexión tiene la forma:

```
jdbc:mysql://HOST:PUERTO/sistema_productos?sslMode=PREFERRED&allowPublicKeyRetrieval=true&serverTimezone=America/Bogota&connectTimeout=10000
```

Fragmento de `ConexionBD.java`:

```java
private static final String HOST = "mysql-36eb12f4-taller1-persistencia-de-datos.h.aivencloud.com";
private static final String PUERTO = "10441";
private static final String BASE_DATOS = "sistema_productos";
private static final String USUARIO = "avnadmin";
// La contrasena se lee del archivo conexion.properties
private static final String CONTRASENA = leerContrasena();

public static Connection obtenerConexion() throws SQLException {
    return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
}
```

Por seguridad, la contraseña no se escribe en el código fuente. Se guarda en el archivo `src/main/resources/conexion.properties`, que está excluido del repositorio mediante `.gitignore`; el repositorio solo incluye la plantilla `conexion.properties.ejemplo`. Al construir el ejecutable, GitHub Actions genera ese archivo a partir de un secreto cifrado del repositorio (`DB_PASSWORD`). Esta decisión surgió durante el desarrollo: al intentar subir el código con la contraseña escrita en `ConexionBD.java`, la protección de secretos de GitHub (*push protection*) bloqueó la publicación.

# Implementación

Cada operación del DAO sigue el mismo patrón: abrir la conexión, preparar la sentencia, asignar los parámetros, ejecutar y cerrar los recursos automáticamente con *try-with-resources*. Por ejemplo, el registro:

```java
public void registrar(Producto producto) throws SQLException {
    try (Connection con = ConexionBD.obtenerConexion();
         PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {
        ps.setString(1, producto.getCodigo());
        ps.setString(2, producto.getNombre());
        ps.setString(3, producto.getDescripcion());
        ps.setDouble(4, producto.getPrecio());
        ps.setInt(5, producto.getCantidad());
        ps.executeUpdate();
    }
}
```

La interfaz gráfica (`ProductoFrame`) está formada por un panel de formulario con cinco campos de texto, una fila de seis botones (Registrar, Consultar / Actualizar tabla, Buscar, Actualizar, Eliminar, Limpiar) y una `JTable` no editable. Al seleccionar una fila de la tabla, un `ListSelectionListener` copia sus datos a los campos para facilitar la actualización y la eliminación.

El código fuente completo se encuentra en el repositorio de GitHub indicado al final del documento.

# Operaciones CRUD

| Operación | Botón | Método del DAO | Sentencia SQL |
|---|---|---|---|
| Crear | Registrar | `registrar(Producto)` | `INSERT INTO productos (codigo, nombre, descripcion, precio, cantidad) VALUES (?, ?, ?, ?, ?)` |
| Consultar todo | Consultar / Actualizar tabla | `listarTodos()` | `SELECT id, codigo, nombre, descripcion, precio, cantidad FROM productos ORDER BY codigo` |
| Buscar por código | Buscar (con código) | `buscarPorCodigo(String)` | `SELECT … FROM productos WHERE codigo = ?` |
| Buscar por nombre | Buscar (sin código) | `buscarPorNombre(String)` | `SELECT … FROM productos WHERE nombre LIKE ? ORDER BY nombre` |
| Actualizar | Actualizar | `actualizar(Producto)` | `UPDATE productos SET descripcion = ?, precio = ?, cantidad = ? WHERE codigo = ?` |
| Eliminar | Eliminar | `eliminar(String)` | `DELETE FROM productos WHERE codigo = ?` |

Los métodos `actualizar` y `eliminar` devuelven `true` solo si `executeUpdate()` afectó al menos una fila; así la interfaz sabe si el producto existía. En la búsqueda por nombre, los comodines `%` se agregan al valor del parámetro y no al texto SQL, de modo que la consulta sigue siendo parametrizada.

# Validaciones

Las validaciones se realizan en la vista antes de llamar al DAO:

| Regla | Mensaje mostrado |
|---|---|
| El código no puede estar vacío | "El código no puede estar vacío." |
| El nombre no puede estar vacío (al registrar) | "El nombre no puede estar vacío." |
| El precio es obligatorio y numérico | "El precio no puede estar vacío." / "El precio debe ser un valor numérico (ejemplo: 2500 o 2500.50)." |
| El precio no puede ser negativo | "El precio no puede ser negativo." |
| La cantidad es obligatoria y entera | "La cantidad no puede estar vacía." / "La cantidad debe ser un número entero (sin letras ni decimales)." |
| La cantidad no puede ser negativa | "La cantidad no puede ser negativa." |
| El código no puede repetirse | "Ya existe un producto con el código …" |
| El registro debe existir para buscar, actualizar o eliminar | "No existe un producto con el código …" |
| Longitudes máximas (20, 100 y 255 caracteres) | "El código/nombre/descripción no puede tener más de … caracteres." |

El precio acepta punto o coma como separador decimal (por ejemplo, 2500.50 o 2500,50).

# Manejo de excepciones

- **Cierre de recursos:** `Connection`, `PreparedStatement` y `ResultSet` se declaran en bloques *try-with-resources*, por lo que se cierran automáticamente incluso si ocurre un error.
- **Propagación controlada:** el DAO no muestra mensajes; declara `throws SQLException` y la vista captura la excepción.
- **Mensajes claros:** el método `mostrarErrorBD` de la vista traduce los errores más comunes:

| Situación | Cómo se detecta | Mensaje al usuario |
|---|---|---|
| MySQL apagado o host/puerto incorrectos | SQLState que empieza por `08` | "No se pudo conectar con MySQL…" |
| Driver no disponible | Mensaje "No suitable driver" | "No se encontró el driver JDBC de MySQL…" |
| Usuario o contraseña incorrectos | Código de error 1045 | "Acceso denegado a MySQL…" |
| Base de datos inexistente | Código de error 1049 | "La base de datos 'sistema_productos' no existe…" |
| Tabla inexistente | Código de error 1146 | "La tabla 'productos' no existe…" |
| Código duplicado | Código de error 1062 | "Ya existe un producto con ese código." |

- **Entradas numéricas:** las `NumberFormatException` producidas al convertir precio y cantidad se capturan y se muestran como advertencias de validación.
- La aplicación **no se cierra** ante un error de base de datos; el usuario puede corregir el problema y volver a intentar.

# Pruebas

La siguiente tabla presenta los casos de prueba ejecutados con la aplicación conectada a MySQL. La columna *Resultado esperado* indica el comportamiento definido antes de la prueba, y *Resultado obtenido* registra lo observado durante la ejecución.

| ID | Caso | Datos de entrada | Resultado esperado | Resultado obtenido | Estado |
|---|---|---|---|---|---|
| CP01 | Registro correcto | P001, Arroz 500 g, Bolsa, 2500, 40; p002, frijol 500g, paquete, 2500, 80 | "Producto registrado correctamente." y los productos en la tabla | Se mostró el mensaje de éxito y ambos productos aparecieron en la tabla con ID 1 y 2. | Aprobado |
| CP02 | Registro con campos vacíos | Código vacío; luego nombre vacío | "El código no puede estar vacío." / "El nombre no puede estar vacío." | Se mostró el aviso correspondiente y no se registró ningún producto. | Aprobado |
| CP03 | Precio negativo | Precio: -100 | "El precio no puede ser negativo." | Se mostró el aviso y no se registró el producto. | Aprobado |
| CP04 | Cantidad negativa | Cantidad: -5 | "La cantidad no puede ser negativa." | Se mostró el aviso y no se registró el producto. | Aprobado |
| CP05 | Cantidad con texto | Cantidad: diez | "La cantidad debe ser un número entero…" | Se mostró el aviso y no se registró el producto. | Aprobado |
| CP06 | Búsqueda por código | Código: p002 → Buscar | La tabla muestra solo p002 y se cargan sus datos | La tabla mostró solo el producto buscado y sus datos se cargaron en los campos. | Aprobado |
| CP07 | Búsqueda por nombre | Nombre: arroz → Buscar | Se muestran los productos que contienen "arroz" | Se mostró "Se encontraron 1 producto(s)." y la tabla mostró Arroz 500 g. | Aprobado |
| CP08 | Actualización correcta | Seleccionar P001; Bolsa 500 g, 2700, 35 → Actualizar | "Producto actualizado correctamente." y nuevos valores | Al seleccionar la fila se cargaron los datos; se mostró el mensaje de éxito y la tabla reflejó los nuevos valores. | Aprobado |
| CP09 | Actualización de inexistente | P999, 100, 1 → Actualizar | "No existe un producto con el código "P999"…" | Se mostró el aviso y no se modificó ningún registro. | Aprobado |
| CP10 | Eliminación correcta | p002 → Eliminar → Sí | Confirmación y "Producto eliminado correctamente." | Se mostró la confirmación; al aceptar, p002 se eliminó de la tabla y de la base de datos. | Aprobado |
| CP11 | Cancelación de eliminación | p002 → Eliminar → No | "Eliminación cancelada…"; el producto permanece | Se mostró el mensaje de cancelación y p002 permaneció en la tabla. | Aprobado |
| CP12 | Persistencia | Cerrar y reabrir la aplicación | P001 sigue en la tabla y en MySQL | Al reabrir, P001 apareció con los datos actualizados; la misma fila se observó con SELECT en MySQL. | Aprobado |
| CP13 | Error de conexión | Detener el servicio MySQL80 y abrir la aplicación | "No se pudo conectar con MySQL…"; la app no se cierra | Se mostró el mensaje de error de conexión y la aplicación siguió abierta. Al reiniciar MySQL, los datos volvieron a cargarse. | Aprobado |
| CP14 | Código duplicado | Código: p001 (ya existía P001) → Registrar | "Ya existe un producto con el código…" | Se mostró "Ya existe un producto con el código "p001"." MySQL compara códigos sin distinguir mayúsculas, por lo que p001 y P001 se consideran el mismo código. | Aprobado |

Resultado general: los 14 casos de prueba ejecutados fueron aprobados.

# Evidencias de ejecución

A continuación se presentan las evidencias del funcionamiento de la aplicación.

**Ventana principal**

[INSERTAR CAPTURA: VENTANA PRINCIPAL]

**Registro de producto (CP01)**

[INSERTAR CAPTURA: REGISTRO DE PRODUCTO]

**Validaciones (CP02 a CP05)**

[INSERTAR CAPTURA: VALIDACIÓN DE CAMPOS VACÍOS]

[INSERTAR CAPTURA: VALIDACIÓN DE PRECIO NEGATIVO]

[INSERTAR CAPTURA: VALIDACIÓN DE CANTIDAD NEGATIVA O CON TEXTO]

**Consulta de productos**

[INSERTAR CAPTURA: CONSULTA DE PRODUCTOS]

**Búsqueda por código y por nombre (CP06 y CP07)**

[INSERTAR CAPTURA: BÚSQUEDA POR CÓDIGO]

[INSERTAR CAPTURA: BÚSQUEDA POR NOMBRE]

**Actualización (CP08 y CP09)**

[INSERTAR CAPTURA: ACTUALIZACIÓN]

[INSERTAR CAPTURA: ACTUALIZACIÓN DE PRODUCTO INEXISTENTE]

**Eliminación (CP10 y CP11)**

[INSERTAR CAPTURA: CONFIRMACIÓN DE ELIMINACIÓN]

[INSERTAR CAPTURA: ELIMINACIÓN]

**Base de datos en MySQL**

[INSERTAR CAPTURA: BASE DE DATOS EN MYSQL]

**Error de conexión (CP13)**

[INSERTAR CAPTURA: ERROR DE CONEXIÓN]

# Persistencia de datos

La persistencia se logra porque cada operación de la interfaz se ejecuta directamente sobre la tabla `productos` de MySQL a través del DAO. La aplicación no guarda los productos en listas que se pierdan al cerrar el programa: la `JTable` solo muestra lo que devuelve la consulta `SELECT`.

Al iniciar, el método `Main.main` abre la ventana y llama a `cargarTabla()`, que consulta la base de datos y muestra los registros guardados en ejecuciones anteriores.

Procedimiento de comprobación:

1. Registrar uno o varios productos.
2. Cerrar completamente la aplicación.
3. Volver a ejecutarla.
4. Verificar que los productos aparecen en la tabla.
5. Ejecutar `SELECT * FROM productos;` en MySQL y verificar que los mismos registros están almacenados.

[INSERTAR CAPTURA: PRUEBA DE PERSISTENCIA – ANTES DE CERRAR]

[INSERTAR CAPTURA: PRUEBA DE PERSISTENCIA – DESPUÉS DE REABRIR]

# Conclusiones

- La API JDBC permite conectar una aplicación Java con MySQL de forma estándar; el driver MySQL Connector/J, gestionado con Maven, se incorpora al proyecto sin configuraciones manuales.
- El uso de `PreparedStatement` separa las sentencias SQL de los datos del usuario, lo que hace las consultas más seguras frente a la inyección SQL y más fáciles de leer.
- El patrón DAO mantiene todo el SQL en una sola clase, de modo que la interfaz gráfica queda libre de código de base de datos y cada clase tiene una responsabilidad clara.
- Las instrucciones *try-with-resources* garantizan el cierre de conexiones y recursos aun cuando ocurren errores.
- Las 14 pruebas ejecutadas fueron aprobadas, incluida la verificación de persistencia al cerrar y reabrir la aplicación y el manejo controlado de la caída del servidor MySQL.
- Validar los datos en la aplicación y, además, definir restricciones (`UNIQUE`, `CHECK`, `NOT NULL`) en la base de datos protege la calidad de la información.
- Almacenar los productos en MySQL asegura que la información permanezca disponible después de cerrar y reiniciar el programa, cumpliendo el requerimiento de persistencia.

# Referencias bibliográficas

Alur, D., Crupi, J., & Malks, D. (2003). *Core J2EE patterns: Best practices and design strategies* (2.ª ed.). Prentice Hall.

Apache Software Foundation. (s.f.). *Maven getting started guide*. https://maven.apache.org/guides/getting-started/

Deitel, P., & Deitel, H. (2017). *Java how to program, early objects* (11.ª ed.). Pearson.

Oracle. (s.f.-a). *Core J2EE patterns – Data access object*. https://www.oracle.com/java/technologies/dataaccessobject.html

Oracle. (s.f.-b). *MySQL Connector/J developer guide*. https://dev.mysql.com/doc/connector-j/en/

Oracle. (s.f.-c). *MySQL 8.4 reference manual*. https://dev.mysql.com/doc/refman/8.4/en/

Oracle. (s.f.-d). *Trail: Creating a GUI with Swing*. The Java Tutorials. https://docs.oracle.com/javase/tutorial/uiswing/

Oracle. (s.f.-e). *Trail: JDBC database access*. The Java Tutorials. https://docs.oracle.com/javase/tutorial/jdbc/

Oracle. (s.f.-f). *Using prepared statements*. The Java Tutorials. https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html

Oracle. (2021). *Java Platform, Standard Edition 17 API specification: Module java.sql*. https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/module-summary.html

OWASP Foundation. (s.f.). *SQL injection prevention cheat sheet*. https://cheatsheetseries.owasp.org/cheatsheets/SQL_Injection_Prevention_Cheat_Sheet.html

# Ejecución con un clic

Para que la aplicación pueda ejecutarse sin instalar herramientas de desarrollo, se tomaron dos decisiones:

1. **Base de datos en la nube.** La base de datos `sistema_productos` se aloja en un servidor MySQL administrado en la nube (Aiven for MySQL, plan gratuito). La aplicación se conecta a él mediante JDBC con conexión cifrada (`sslMode=PREFERRED`), por lo que cualquier equipo con Internet accede a los mismos datos sin instalar MySQL.
2. **Ejecutable publicado en GitHub Releases.** Un flujo de GitHub Actions (`.github/workflows/publicar-ejecutable.yml`) compila el proyecto con Maven, genera un JAR que incluye el driver de MySQL (`maven-shade-plugin`) y, con la herramienta `jpackage` del JDK, crea una versión para Windows que incluye su propio entorno de Java.

**Instrucciones para ejecutar:**

1. Abrir el enlace de descarga: [PEGAR AQUÍ URL DE LA RELEASE]
2. Descargar `SistemaGestionProductos-Windows.zip` y descomprimirlo.
3. Hacer doble clic en `SistemaGestionProductos.exe`. Si Windows muestra "Windows protegió su PC", elegir **Más información → Ejecutar de todas formas** (el aviso aparece porque el programa no tiene firma digital comercial).

Alternativa: descargar `SistemaGestionProductos.jar` y abrirlo con doble clic (requiere Java 17 o superior instalado).

# Enlace al repositorio de GitHub

[PEGAR AQUÍ URL DEL REPOSITORIO]

# Enlace al video

[PEGAR AQUÍ URL DEL VIDEO]
