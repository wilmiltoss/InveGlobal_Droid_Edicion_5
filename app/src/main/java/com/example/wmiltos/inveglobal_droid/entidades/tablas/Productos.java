package com.example.wmiltos.inveglobal_droid.entidades.tablas;

public class Productos {
    private Integer id;
    private String codigo_barra;
    private String descripcion;
    private String categoria;
    private Integer integer;

    public Productos(Integer id, String codigo_barra, String descripcion, String categoria, Integer integer) {
        this.id = id;
        this.codigo_barra = codigo_barra;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.integer = integer;
    }

    public Productos() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }
}
