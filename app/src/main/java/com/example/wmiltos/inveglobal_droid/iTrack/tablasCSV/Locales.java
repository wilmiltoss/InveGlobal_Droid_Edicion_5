package com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV;

public class Locales {
    private String codigo;
    private String descripcion;
    private String cadena;

    public Locales(String codigo, String descripcion, String cadena) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.cadena = cadena;
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

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }
}
