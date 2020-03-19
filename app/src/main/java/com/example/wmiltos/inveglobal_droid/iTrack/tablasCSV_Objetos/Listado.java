package com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV_Objetos;

public class Listado {

    private String scanning;
    private String descripcion;
    private String cantidad;

    public Listado(String scanning, String descripcion, String cantidad) {
        this.scanning = scanning;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }

    public Listado() {

    }

    public String getScanning() {
        return scanning;
    }

    public void setScanning(String scanning) {
        this.scanning = scanning;
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
