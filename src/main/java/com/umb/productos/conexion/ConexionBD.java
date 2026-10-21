package com.umb.productos.conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase encargada de crear la conexion con MySQL mediante JDBC.
 *
 * =====================================================================
 *  CONFIGURACION DE LA BASE DE DATOS
 *  - Servidor, puerto, base de datos y usuario: constantes de esta clase.
 *  - Contrasena: archivo src/main/resources/conexion.properties
 *    (linea db.password=...). Ese archivo NO se sube a GitHub
 *    (esta en .gitignore) para no publicar la contrasena.
 *    Use conexion.properties.ejemplo como plantilla.
 * =====================================================================
 */
public class ConexionBD {

    // ---------------- DATOS DE CONEXION (EDITAR AQUI) ----------------
    private static final String HOST = "mysql-36eb12f4-taller1-persistencia-de-datos.h.aivencloud.com";
    private static final String PUERTO = "10441";
    private static final String BASE_DATOS = "sistema_productos";
    private static final String USUARIO = "avnadmin";
    // La contrasena se lee del archivo conexion.properties
    private static final String CONTRASENA = leerContrasena();
    // -----------------------------------------------------------------

    /**
     * URL JDBC de MySQL.
     * sslMode=PREFERRED usa conexion cifrada cuando el servidor la ofrece
     * (obligatorio en MySQL en la nube) y funciona igual con MySQL local.
     * connectTimeout=10000 hace que, si MySQL no responde, el error aparezca en 10 segundos.
     */
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DATOS
            + "?sslMode=PREFERRED"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=America/Bogota"
            + "&connectTimeout=10000";

    /** Constructor privado: esta clase solo se usa de forma estatica. */
    private ConexionBD() {
    }

    /**
     * Abre una nueva conexion con la base de datos.
     * Quien llama a este metodo debe cerrarla (se recomienda try-with-resources).
     *
     * @return conexion abierta con MySQL
     * @throws SQLException si MySQL no esta disponible, las credenciales son
     *                      incorrectas o la base de datos no existe
     */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    /**
     * Lee la contrasena desde el archivo conexion.properties (incluido en el programa).
     * Si el archivo no existe, devuelve una cadena vacia y MySQL respondera
     * "Acceso denegado", que la interfaz muestra con un mensaje claro.
     */
    private static String leerContrasena() {
        try (InputStream entrada = ConexionBD.class.getResourceAsStream("/conexion.properties")) {
            if (entrada == null) {
                return "";
            }
            Properties propiedades = new Properties();
            propiedades.load(entrada);
            return propiedades.getProperty("db.password", "").trim();
        } catch (IOException e) {
            return "";
        }
    }
}
