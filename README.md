# Sistema de Gestión de Productos

Aplicación de escritorio en **Java** que permite registrar, consultar, actualizar y eliminar productos de un pequeño establecimiento comercial. La información se guarda en **MySQL** mediante **JDBC**, por lo que los datos se conservan después de cerrar y volver a abrir el programa.

Proyecto académico – Taller de persistencia de datos en Java.

---

## ▶ Ejecutar con un clic (sin instalar nada)

1. Ir a **Releases**: https://github.com/Gabriel-Nieves-source/sistema-gestion-productos/releases/latest
2. Descargar `SistemaGestionProductos-Windows.zip` y descomprimirlo.
3. Doble clic en `SistemaGestionProductos.exe`.
   Si Windows muestra *"Windows protegió su PC"* → **Más información → Ejecutar de todas formas**.

El ejecutable incluye Java y se conecta a una base de datos MySQL en la nube, por lo que no requiere instalar Java ni MySQL. También se publica `SistemaGestionProductos.jar` (requiere Java 17+).

## Tecnologías

| Tecnología | Uso |
|---|---|
| Java 17 o superior | Lenguaje de programación |
| Swing | Interfaz gráfica |
| MySQL 8.x | Sistema gestor de base de datos |
| JDBC + MySQL Connector/J 8.4.0 | Conexión entre Java y MySQL |
| Maven | Gestión de dependencias y ejecución |
| Git / GitHub | Control de versiones |

## Requisitos

- JDK 17 o superior (`java -version`)
- MySQL Server 8.x en ejecución (y, opcionalmente, MySQL Workbench)
- Maven 3.8+ (o el Maven incluido en NetBeans / IntelliJ IDEA / Eclipse)
- Conexión a Internet la primera vez, para que Maven descargue el driver de MySQL

## Instalación

```bash
git clone https://github.com/Gabriel-Nieves-source/sistema-gestion-productos
cd sistema-gestion-productos
```

## Configuración de MySQL

1. Inicie el servidor MySQL.
2. Ejecute el script `database/script.sql`:
   - **MySQL Workbench:** *File → Open SQL Script…* → seleccione `database/script.sql` → botón del rayo (*Execute*).
   - **Consola:**
     ```bash
     mysql -u root -p < database/script.sql
     ```
3. Configure la conexión en dos lugares:

   **a) Servidor, puerto, base de datos y usuario:** en `src/main/java/com/umb/productos/conexion/ConexionBD.java`:

```java
// ---------------- DATOS DE CONEXION (EDITAR AQUI) ----------------
private static final String HOST = "localhost";              // servidor MySQL
private static final String PUERTO = "3306";                 // puerto de MySQL
private static final String BASE_DATOS = "sistema_productos"; // nombre de la BD
private static final String USUARIO = "root";                // su usuario
// La contrasena se lee del archivo conexion.properties
private static final String CONTRASENA = leerContrasena();
// -----------------------------------------------------------------
```

   **b) Contraseña:** copie `src/main/resources/conexion.properties.ejemplo` como `src/main/resources/conexion.properties` y escriba su contraseña:

```properties
db.password=su_contraseña
```

`conexion.properties` está en `.gitignore`, por lo que la contraseña **no se publica en GitHub**. Para el ejecutable de *Releases*, GitHub Actions crea ese archivo a partir del secreto `DB_PASSWORD` del repositorio (*Settings → Secrets and variables → Actions*).

## Ejecución

**Generar el JAR ejecutable** (queda en `target/SistemaGestionProductos.jar`, incluye el driver):

```bash
mvn clean package
java -jar target/SistemaGestionProductos.jar
```

**Publicar una nueva versión en Releases:** al subir una etiqueta, GitHub Actions construye el `.exe` y el `.jar` automáticamente:

```bash
git tag v1.0
git push origin v1.0
```

**Con Maven (terminal):**

```bash
mvn clean compile exec:java
```

**Con un IDE:**

- **IntelliJ IDEA:** *File → Open…* → seleccione la carpeta del proyecto (contiene `pom.xml`) → espere que cargue Maven → abra `Main.java` → botón ▶ *Run*.
- **NetBeans:** *File → Open Project…* → seleccione la carpeta → clic derecho en el proyecto → *Run* (si pregunta la clase principal, elija `com.umb.productos.Main`).
- **Eclipse:** *File → Import… → Maven → Existing Maven Projects* → seleccione la carpeta → clic derecho en `Main.java` → *Run As → Java Application*.

## Funcionalidades

- **Registrar** productos con código, nombre, descripción, precio y cantidad.
- **Consultar** todos los productos en una tabla.
- **Buscar** por código (exacto) o por nombre (contiene el texto).
- **Actualizar** precio, descripción y cantidad de un producto existente.
- **Eliminar** un producto por su código, con confirmación previa.
- Al **seleccionar una fila** de la tabla, sus datos se cargan en el formulario.
- **Validaciones:** código y nombre obligatorios, precio numérico no negativo, cantidad entera no negativa, códigos repetidos y registros inexistentes.
- **Manejo de errores** de conexión, credenciales, base de datos o tabla inexistente, con mensajes claros.
- **Persistencia** real en MySQL.

## Estructura del proyecto

```
sistema-gestion-productos/
├── pom.xml
├── README.md
├── .gitignore
├── .github/workflows/
│   └── publicar-ejecutable.yml
├── database/
│   └── script.sql
├── docs/
│   ├── casos_de_prueba.md
│   ├── diagrama_clases.puml
│   ├── documento_academico.md
│   └── guion_video_en.md
├── src/main/resources/
│   └── conexion.properties.ejemplo   (plantilla de la contraseña)
└── src/main/java/com/umb/productos/
    ├── Main.java
    ├── conexion/
    │   └── ConexionBD.java
    ├── dao/
    │   └── ProductoDAO.java
    ├── modelo/
    │   └── Producto.java
    └── vista/
        └── ProductoFrame.java
```

| Clase | Responsabilidad |
|---|---|
| `Producto` | Modelo: representa un producto (una fila de la tabla). |
| `ConexionBD` | Guarda la configuración y abre conexiones JDBC con `DriverManager`. |
| `ProductoDAO` | Contiene todas las consultas SQL (CRUD) con `PreparedStatement`. |
| `ProductoFrame` | Ventana Swing: formulario, botones, tabla, validaciones y mensajes. |
| `Main` | Inicia la aplicación. |

## Video

[PEGAR AQUÍ URL DEL VIDEO]

## Autor

[NOMBRE DEL ESTUDIANTE] – [PROGRAMA ACADÉMICO], [UNIVERSIDAD]
