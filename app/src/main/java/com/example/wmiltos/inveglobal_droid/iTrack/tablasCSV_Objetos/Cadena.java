package com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV_Objetos;

public class Cadena {
    private String idcadena;
    private String descrcadena;
    private String idtiponegocio;

    public Cadena(String idcadena, String descrcadena, String idtiponegocio) {
        this.idcadena = idcadena;
        this.descrcadena = descrcadena;
        this.idtiponegocio = idtiponegocio;
    }

    public Cadena(){

    }

    public String getIdcadena() {
        return idcadena;
    }

    public void setIdcadena(String idcadena) {
        this.idcadena = idcadena;
    }

    public String getDescrcadena() {
        return descrcadena;
    }

    public void setDescrcadena(String descrcadena) {
        this.descrcadena = descrcadena;
    }

    public String getIdtiponegocio() {
        return idtiponegocio;
    }

    public void setIdtiponegocio(String idtiponegocio) {
        this.idtiponegocio = idtiponegocio;
    }
}
