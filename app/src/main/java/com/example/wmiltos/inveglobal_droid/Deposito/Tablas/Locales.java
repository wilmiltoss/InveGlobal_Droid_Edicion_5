package com.example.wmiltos.inveglobal_droid.Deposito.Tablas;

public class Locales {
    private String codigo;
    private String descripcion;

    public Locales(String codigo, String descripcion, String cadena) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public Locales() {

    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
