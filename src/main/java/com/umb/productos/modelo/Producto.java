package com.umb.productos.modelo;

/**
 * Modelo que representa un producto del establecimiento.
 * Cada atributo corresponde a una columna de la tabla "productos".
 */
public class Producto {

    private int id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private double precio;
    private int cantidad;

    /** Constructor vacio. */
    public Producto() {
    }

    /** Constructor sin id (se usa al registrar, porque MySQL genera el id). */
    public Producto(String codigo, String nombre, String descripcion, double precio, int cantidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    /** Constructor completo (se usa al leer un producto desde la base de datos). */
    public Producto(int id, String codigo, String nombre, String descripcion, double precio, int cantidad) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Producto{id=" + id + ", codigo='" + codigo + "', nombre='" + nombre
                + "', descripcion='" + descripcion + "', precio=" + precio
                + ", cantidad=" + cantidad + "}";
    }
}
