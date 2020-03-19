package com.example.wmiltos.inveglobal_droid.entidades.tablas;

public class Productos {
   // private String id;
    private String codigo_barra;
    private String descripcion;
    private String cantidad;
   // private String categoria;


    public Productos() {
    }

    public Productos(String codigo_barra, String descripcion, String cantidad) {
        this.codigo_barra = codigo_barra;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }

    public String getCodigo_barra() {
        return codigo_barra;
    }

    public void setCodigo_barra(String codigo_barra) {
        this.codigo_barra = codigo_barra;
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
