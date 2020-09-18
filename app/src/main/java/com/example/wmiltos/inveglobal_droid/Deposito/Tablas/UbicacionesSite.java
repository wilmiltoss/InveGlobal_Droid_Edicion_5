package com.example.wmiltos.inveglobal_droid.Deposito.Tablas;

public class UbicacionesSite {
    //site	Area	Grupo	Código	alto	ancho	prof	Volumen	Vol_libre	MP	MV	ML	Sec_picking	Vacia

    private String site;
    private String area;
    private String grupo;
    private String codigo_ubicacion;
    private String alto;
    private String ancho;
    private String prof;
    private String volumen;
    private String vol_libre;
    private String mp;
    private String mv;
    private String ml;
    private String sec_picking;
    private String vacia;


    public UbicacionesSite() {

    }

    public UbicacionesSite(String site, String area, String grupo, String codigo_ubicacion, String alto, String ancho,
                           String prof, String volumen, String vol_libre, String mp, String mv, String ml, String sec_picking,
                           String vacia) {
        this.site = site;
        this.area = area;
        this.grupo = grupo;
        this.codigo_ubicacion = codigo_ubicacion;
        this.alto = alto;
        this.ancho = ancho;
        this.prof = prof;
        this.volumen = volumen;
        this.vol_libre = vol_libre;
        this.mp = mp;
        this.mv = mv;
        this.ml = ml;
        this.sec_picking = sec_picking;
        this.vacia = vacia;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getCodigo_ubicacion() {
        return codigo_ubicacion;
    }

    public void setCodigo_ubicacion(String codigo_ubicacion) {
        this.codigo_ubicacion = codigo_ubicacion;
    }

    public String getAlto() {
        return alto;
    }

    public void setAlto(String alto) {
        this.alto = alto;
    }

    public String getAncho() {
        return ancho;
    }

    public void setAncho(String ancho) {
        this.ancho = ancho;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getVolumen() {
        return volumen;
    }

    public void setVolumen(String volumen) {
        this.volumen = volumen;
    }

    public String getVol_libre() {
        return vol_libre;
    }

    public void setVol_libre(String vol_libre) {
        this.vol_libre = vol_libre;
    }

    public String getMp() {
        return mp;
    }

    public void setMp(String mp) {
        this.mp = mp;
    }

    public String getMv() {
        return mv;
    }

    public void setMv(String mv) {
        this.mv = mv;
    }

    public String getMl() {
        return ml;
    }

    public void setMl(String ml) {
        this.ml = ml;
    }

    public String getSec_picking() {
        return sec_picking;
    }

    public void setSec_picking(String sec_picking) {
        this.sec_picking = sec_picking;
    }

    public String getVacia() {
        return vacia;
    }

    public void setVacia(String vacia) {
        this.vacia = vacia;
    }
}
