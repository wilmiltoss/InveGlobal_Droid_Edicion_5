package com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV_Objetos;

public class Ubicaciones {

    private String id_ubicacion;
    private String descr_ubicacion;

    public Ubicaciones(String id_ubicacion, String descr_ubicacion) {
        this.id_ubicacion = id_ubicacion;
        this.descr_ubicacion = descr_ubicacion;
    }
    public Ubicaciones (){

    }

    public String getId_ubicacion() {
        return id_ubicacion;
    }

    public void setId_ubicacion(String id_ubicacion) {
        this.id_ubicacion = id_ubicacion;
    }

    public String getDescr_ubicacion() {
        return descr_ubicacion;
    }

    public void setDescr_ubicacion(String descr_ubicacion) {
        this.descr_ubicacion = descr_ubicacion;
    }
}
