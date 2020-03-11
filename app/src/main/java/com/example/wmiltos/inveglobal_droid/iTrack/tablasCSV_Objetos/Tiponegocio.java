package com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV_Objetos;

public class Tiponegocio {
    private String id;
    private String descripcion;

    public Tiponegocio(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Tiponegocio (){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
