package com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV_Objetos;

public class Product {

    private String id;
    private String codigo_producto;
    private String descripcion;
    private String cantidad;

    public Product(String id, String codigo_producto, String descripcion, String cantidad) {
        this.id = id;
        this.codigo_producto = codigo_producto;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo_producto() {
        return codigo_producto;
    }

    public void setCodigo_producto(String codigo_producto) {
        this.codigo_producto = codigo_producto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
