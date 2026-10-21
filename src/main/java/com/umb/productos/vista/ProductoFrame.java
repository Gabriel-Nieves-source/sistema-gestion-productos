package com.umb.productos.vista;

import com.umb.productos.dao.ProductoDAO;
import com.umb.productos.modelo.Producto;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * Ventana principal (interfaz grafica Swing).
 * Recibe los datos del usuario, los valida y llama al ProductoDAO.
 * No contiene consultas SQL.
 */
public class ProductoFrame extends JFrame {

    // Limites de longitud: coinciden con las columnas de la tabla "productos".
    private static final int MAX_CODIGO = 20;
    private static final int MAX_NOMBRE = 100;
    private static final int MAX_DESCRIPCION = 255;

    private final ProductoDAO productoDAO = new ProductoDAO();

    // Campos de texto
    private final JTextField txtCodigo = new JTextField(20);
    private final JTextField txtNombre = new JTextField(20);
    private final JTextField txtDescripcion = new JTextField(20);
    private final JTextField txtPrecio = new JTextField(20);
    private final JTextField txtCantidad = new JTextField(20);

    // Botones
    private final JButton btnRegistrar = new JButton("Registrar");
    private final JButton btnConsultar = new JButton("Consultar / Actualizar tabla");
    private final JButton btnBuscar = new JButton("Buscar");
    private final JButton btnActualizar = new JButton("Actualizar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnLimpiar = new JButton("Limpiar");

    // Tabla
    private final DefaultTableModel modeloTabla;
    private final JTable tablaProductos;

    public ProductoFrame() {
        super("Sistema de Gestión de Productos");

        // Modelo de tabla no editable (los cambios se hacen con los botones)
        String[] columnas = {"ID", "Código", "Nombre", "Descripción", "Precio", "Cantidad"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.getTableHeader().setReorderingAllowed(false);

        construirInterfaz();
        registrarEventos();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    // ===================== CONSTRUCCION DE LA INTERFAZ =====================

    private void construirInterfaz() {
        // Panel del formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del producto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        agregarFila(panelFormulario, gbc, 0, "Código *:", txtCodigo);
        agregarFila(panelFormulario, gbc, 1, "Nombre *:", txtNombre);
        agregarFila(panelFormulario, gbc, 2, "Descripción:", txtDescripcion);
        agregarFila(panelFormulario, gbc, 3, "Precio *:", txtPrecio);
        agregarFila(panelFormulario, gbc, 4, "Cantidad *:", txtCantidad);

        JLabel lblAyuda = new JLabel("Buscar: use el campo Código (búsqueda exacta) o, si está vacío, el campo Nombre.");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panelFormulario.add(lblAyuda, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnConsultar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // Tabla con barra de desplazamiento
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Productos registrados"));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelSuperior, BorderLayout.NORTH);
        getContentPane().add(scrollTabla, BorderLayout.CENTER);
    }

    private void agregarFila(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, JTextField campo) {
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        panel.add(campo, gbc);
    }

    private void registrarEventos() {
        btnRegistrar.addActionListener(e -> registrarProducto());
        btnConsultar.addActionListener(e -> cargarTabla());
        btnBuscar.addActionListener(e -> buscarProducto());
        btnActualizar.addActionListener(e -> actualizarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        // Al seleccionar una fila, sus datos se cargan en los campos.
        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarFilaSeleccionada();
            }
        });
    }

    // ============================ OPERACIONES ============================

    /** CREATE: valida los campos y registra el producto. */
    private void registrarProducto() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (!validarCodigo(codigo)) {
            return;
        }
        if (nombre.isEmpty()) {
            mostrarAdvertencia("El nombre no puede estar vacío.");
            txtNombre.requestFocus();
            return;
        }
        if (nombre.length() > MAX_NOMBRE) {
            mostrarAdvertencia("El nombre no puede tener más de " + MAX_NOMBRE + " caracteres.");
            txtNombre.requestFocus();
            return;
        }
        if (!validarDescripcion(descripcion)) {
            return;
        }
        Double precio = leerPrecio();
        if (precio == null) {
            return;
        }
        Integer cantidad = leerCantidad();
        if (cantidad == null) {
            return;
        }

        try {
            if (productoDAO.buscarPorCodigo(codigo) != null) {
                mostrarAdvertencia("Ya existe un producto con el código \"" + codigo + "\".");
                txtCodigo.requestFocus();
                return;
            }
            Producto producto = new Producto(codigo, nombre, descripcion, precio, cantidad);
            productoDAO.registrar(producto);
            mostrarInformacion("Producto registrado correctamente.");
            limpiarCampos();
            cargarTabla();
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
        }
    }

    /** READ: muestra todos los productos de la base de datos en la tabla. */
    public void cargarTabla() {
        try {
            mostrarEnTabla(productoDAO.listarTodos());
        } catch (SQLException ex) {
            modeloTabla.setRowCount(0);
            mostrarErrorBD(ex);
        }
    }

    /** READ: busca por código (si se escribió) o, si no, por nombre. */
    private void buscarProducto() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (codigo.isEmpty() && nombre.isEmpty()) {
            mostrarAdvertencia("Escriba un código o un nombre para buscar.");
            txtCodigo.requestFocus();
            return;
        }

        try {
            if (!codigo.isEmpty()) {
                Producto producto = productoDAO.buscarPorCodigo(codigo);
                if (producto == null) {
                    modeloTabla.setRowCount(0);
                    mostrarAdvertencia("No existe un producto con el código \"" + codigo + "\".");
                } else {
                    mostrarEnTabla(List.of(producto));
                    tablaProductos.setRowSelectionInterval(0, 0);
                }
            } else {
                List<Producto> resultados = productoDAO.buscarPorNombre(nombre);
                mostrarEnTabla(resultados);
                if (resultados.isEmpty()) {
                    mostrarAdvertencia("No se encontraron productos cuyo nombre contenga \"" + nombre + "\".");
                } else {
                    mostrarInformacion("Se encontraron " + resultados.size() + " producto(s).");
                }
            }
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
        }
    }

    /** UPDATE: modifica descripción, precio y cantidad de un producto existente. */
    private void actualizarProducto() {
        String codigo = txtCodigo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (!validarCodigo(codigo)) {
            return;
        }
        if (!validarDescripcion(descripcion)) {
            return;
        }
        Double precio = leerPrecio();
        if (precio == null) {
            return;
        }
        Integer cantidad = leerCantidad();
        if (cantidad == null) {
            return;
        }

        try {
            Producto producto = new Producto();
            producto.setCodigo(codigo);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setCantidad(cantidad);

            if (productoDAO.actualizar(producto)) {
                mostrarInformacion("Producto actualizado correctamente.");
                limpiarCampos();
                cargarTabla();
            } else {
                mostrarAdvertencia("No existe un producto con el código \"" + codigo + "\". No se actualizó nada.");
            }
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
        }
    }

    /** DELETE: elimina un producto por su código después de pedir confirmación. */
    private void eliminarProducto() {
        String codigo = txtCodigo.getText().trim();
        if (!validarCodigo(codigo)) {
            return;
        }

        try {
            Producto producto = productoDAO.buscarPorCodigo(codigo);
            if (producto == null) {
                mostrarAdvertencia("No existe un producto con el código \"" + codigo + "\".");
                return;
            }

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro de eliminar el producto \"" + producto.getNombre()
                            + "\" (código " + codigo + ")?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (respuesta != JOptionPane.YES_OPTION) {
                mostrarInformacion("Eliminación cancelada. El producto no fue eliminado.");
                return;
            }

            if (productoDAO.eliminar(codigo)) {
                mostrarInformacion("Producto eliminado correctamente.");
                limpiarCampos();
                cargarTabla();
            } else {
                mostrarAdvertencia("El producto ya no existe en la base de datos.");
                cargarTabla();
            }
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
        }
    }

    /** Deja todos los campos vacíos y quita la selección de la tabla. */
    private void limpiarCampos() {
        tablaProductos.clearSelection();
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        txtCodigo.requestFocus();
    }

    // ============================ AUXILIARES ============================

    /** Llena la tabla con la lista de productos recibida. */
    private void mostrarEnTabla(List<Producto> productos) {
        modeloTabla.setRowCount(0);
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getCodigo(),
                    p.getNombre(),
                    p.getDescripcion(),
                    String.format(Locale.US, "%.2f", p.getPrecio()),
                    p.getCantidad()
            });
        }
    }

    /** Copia los datos de la fila seleccionada a los campos de texto. */
    private void cargarFilaSeleccionada() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) {
            return;
        }
        txtCodigo.setText(valorCelda(fila, 1));
        txtNombre.setText(valorCelda(fila, 2));
        txtDescripcion.setText(valorCelda(fila, 3));
        txtPrecio.setText(valorCelda(fila, 4));
        txtCantidad.setText(valorCelda(fila, 5));
    }

    private String valorCelda(int fila, int columna) {
        Object valor = modeloTabla.getValueAt(fila, columna);
        return valor == null ? "" : valor.toString();
    }

    /** Valida que el código no esté vacío y no supere la longitud permitida. */
    private boolean validarCodigo(String codigo) {
        if (codigo.isEmpty()) {
            mostrarAdvertencia("El código no puede estar vacío.");
            txtCodigo.requestFocus();
            return false;
        }
        if (codigo.length() > MAX_CODIGO) {
            mostrarAdvertencia("El código no puede tener más de " + MAX_CODIGO + " caracteres.");
            txtCodigo.requestFocus();
            return false;
        }
        return true;
    }

    /** La descripción es opcional, pero no puede superar la longitud de la columna. */
    private boolean validarDescripcion(String descripcion) {
        if (descripcion.length() > MAX_DESCRIPCION) {
            mostrarAdvertencia("La descripción no puede tener más de " + MAX_DESCRIPCION + " caracteres.");
            txtDescripcion.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Lee y valida el precio: obligatorio, numérico y no negativo.
     * Acepta punto o coma como separador decimal (ej. 2500.50 o 2500,50).
     *
     * @return el precio, o null si no es válido (ya se mostró el mensaje)
     */
    private Double leerPrecio() {
        String texto = txtPrecio.getText().trim().replace(',', '.');
        if (texto.isEmpty()) {
            mostrarAdvertencia("El precio no puede estar vacío.");
            txtPrecio.requestFocus();
            return null;
        }
        double precio;
        try {
            precio = Double.parseDouble(texto);
        } catch (NumberFormatException ex) {
            mostrarAdvertencia("El precio debe ser un valor numérico (ejemplo: 2500 o 2500.50).");
            txtPrecio.requestFocus();
            return null;
        }
        if (Double.isNaN(precio) || Double.isInfinite(precio)) {
            mostrarAdvertencia("El precio debe ser un valor numérico válido.");
            txtPrecio.requestFocus();
            return null;
        }
        if (precio < 0) {
            mostrarAdvertencia("El precio no puede ser negativo.");
            txtPrecio.requestFocus();
            return null;
        }
        if (precio > 99999999.99) {
            mostrarAdvertencia("El precio no puede ser mayor a 99,999,999.99.");
            txtPrecio.requestFocus();
            return null;
        }
        return precio;
    }

    /**
     * Lee y valida la cantidad: obligatoria, número entero y no negativa.
     *
     * @return la cantidad, o null si no es válida (ya se mostró el mensaje)
     */
    private Integer leerCantidad() {
        String texto = txtCantidad.getText().trim();
        if (texto.isEmpty()) {
            mostrarAdvertencia("La cantidad no puede estar vacía.");
            txtCantidad.requestFocus();
            return null;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            mostrarAdvertencia("La cantidad debe ser un número entero (sin letras ni decimales).");
            txtCantidad.requestFocus();
            return null;
        }
        if (cantidad < 0) {
            mostrarAdvertencia("La cantidad no puede ser negativa.");
            txtCantidad.requestFocus();
            return null;
        }
        return cantidad;
    }

    /** Traduce los errores de MySQL más comunes a mensajes claros para el usuario. */
    private void mostrarErrorBD(SQLException ex) {
        String estado = ex.getSQLState() == null ? "" : ex.getSQLState();
        int codigoError = ex.getErrorCode();
        String detalle = ex.getMessage() == null ? "" : ex.getMessage();
        String mensaje;

        if (detalle.startsWith("No suitable driver")) {
            mensaje = "No se encontró el driver JDBC de MySQL (mysql-connector-j).\n"
                    + "Ejecute el proyecto como proyecto Maven para que se descargue la dependencia.";
        } else if (estado.startsWith("08")) {
            mensaje = "No se pudo conectar con MySQL.\n"
                    + "Verifique que el servidor MySQL esté encendido y que el host y el puerto\n"
                    + "configurados en ConexionBD.java sean correctos.";
        } else if (codigoError == 1045) {
            mensaje = "Acceso denegado a MySQL.\n"
                    + "Revise el usuario en ConexionBD.java y la contraseña en conexion.properties.";
        } else if (codigoError == 1049) {
            mensaje = "La base de datos 'sistema_productos' no existe.\n"
                    + "Ejecute el archivo database/script.sql en MySQL.";
        } else if (codigoError == 1146) {
            mensaje = "La tabla 'productos' no existe.\n"
                    + "Ejecute el archivo database/script.sql en MySQL.";
        } else if (codigoError == 1062) {
            mensaje = "Ya existe un producto con ese código.";
        } else {
            mensaje = "Ocurrió un error con la base de datos.";
        }

        // El detalle técnico se recorta para que el cuadro de diálogo no sea demasiado ancho.
        if (detalle.length() > 120) {
            detalle = detalle.substring(0, 120) + "...";
        }

        JOptionPane.showMessageDialog(this,
                mensaje + "\n\nDetalle técnico: " + detalle,
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarInformacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Validación", JOptionPane.WARNING_MESSAGE);
    }
}
