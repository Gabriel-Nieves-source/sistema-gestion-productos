package com.umb.productos;

import com.umb.productos.vista.ProductoFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada de la aplicacion "Sistema de Gestion de Productos".
 */
public class Main {

    public static void main(String[] args) {
        // Usa la apariencia del sistema operativo (Windows, macOS o Linux).
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, Swing usa su apariencia por defecto; la aplicacion sigue funcionando.
        }

        // Las ventanas Swing se crean en el hilo de eventos de Swing (EDT).
        SwingUtilities.invokeLater(() -> {
            ProductoFrame ventana = new ProductoFrame();
            ventana.setVisible(true);
            ventana.cargarTabla();
        });
    }
}
