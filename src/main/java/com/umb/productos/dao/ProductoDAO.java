package com.umb.productos.dao;

import com.umb.productos.conexion.ConexionBD;
import com.umb.productos.modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) de productos.
 * Contiene TODAS las consultas SQL de la aplicacion. La interfaz grafica
 * nunca escribe SQL: solo llama a los metodos de esta clase.
 *
 * Todos los metodos usan PreparedStatement y try-with-resources, de modo que
 * Connection, PreparedStatement y ResultSet se cierran automaticamente.
 * Los errores de base de datos se propagan como SQLException para que la
 * interfaz muestre un mensaje claro al usuario.
 */
public class ProductoDAO {

    private static final String SQL_INSERTAR =
            "INSERT INTO productos (codigo, nombre, descripcion, precio, cantidad) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_LISTAR =
            "SELECT id, codigo, nombre, descripcion, precio, cantidad FROM productos ORDER BY codigo";

    private static final String SQL_BUSCAR_CODIGO =
            "SELECT id, codigo, nombre, descripcion, precio, cantidad FROM productos WHERE codigo = ?";

    private static final String SQL_BUSCAR_NOMBRE =
            "SELECT id, codigo, nombre, descripcion, precio, cantidad FROM productos WHERE nombre LIKE ? ORDER BY nombre";

    private static final String SQL_ACTUALIZAR =
            "UPDATE productos SET descripcion = ?, precio = ?, cantidad = ? WHERE codigo = ?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM productos WHERE codigo = ?";

    /**
     * CREATE: registra un producto nuevo.
     *
     * @param producto producto con los datos ya validados
     * @throws SQLException si ocurre un error (por ejemplo, codigo duplicado o sin conexion)
     */
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

    /**
     * READ: obtiene todos los productos registrados.
     *
     * @return lista de productos (vacia si no hay registros)
     * @throws SQLException si ocurre un error de base de datos
     */
    public List<Producto> listarTodos() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(crearProducto(rs));
            }
        }
        return lista;
    }

    /**
     * READ: busca un producto por su codigo exacto.
     *
     * @param codigo codigo del producto
     * @return el producto encontrado, o null si no existe
     * @throws SQLException si ocurre un error de base de datos
     */
    public Producto buscarPorCodigo(String codigo) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_CODIGO)) {

            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearProducto(rs);
                }
            }
        }
        return null;
    }

    /**
     * READ: busca productos cuyo nombre contenga el texto indicado.
     *
     * @param nombre texto a buscar dentro del nombre
     * @return lista de coincidencias (vacia si no hay)
     * @throws SQLException si ocurre un error de base de datos
     */
    public List<Producto> buscarPorNombre(String nombre) throws SQLException {
        List<Producto> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_NOMBRE)) {

            // Los comodines % se agregan al parametro, no a la consulta SQL.
            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(crearProducto(rs));
                }
            }
        }
        return lista;
    }

    /**
     * UPDATE: modifica descripcion, precio y cantidad del producto con el codigo indicado.
     *
     * @param producto producto con el codigo a buscar y los nuevos valores
     * @return true si se actualizo; false si no existe un producto con ese codigo
     * @throws SQLException si ocurre un error de base de datos
     */
    public boolean actualizar(Producto producto) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setString(1, producto.getDescripcion());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getCantidad());
            ps.setString(4, producto.getCodigo());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * DELETE: elimina el producto con el codigo indicado.
     *
     * @param codigo codigo del producto a eliminar
     * @return true si se elimino; false si no existe un producto con ese codigo
     * @throws SQLException si ocurre un error de base de datos
     */
    public boolean eliminar(String codigo) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {

            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        }
    }

    /** Convierte la fila actual del ResultSet en un objeto Producto. */
    private Producto crearProducto(ResultSet rs) throws SQLException {
        return new Producto(
                rs.getInt("id"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDouble("precio"),
                rs.getInt("cantidad"));
    }
}
