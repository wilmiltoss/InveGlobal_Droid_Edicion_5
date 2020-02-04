package com.example.wmiltos.inveglobal_droid.entidades.tablas;

public class Lecturas {
    private Integer id_locacion;
    private Integer nro_conteo;
    private Integer id_soporte;
    private Integer nro_soporte;
    private Integer id_letra_soporte;
    private Integer nivel;
    private Integer metro;
    private String scanning;
    private Integer cantidad;
    private Integer id_usuario;
    private Integer id_inventario;


    public Lecturas() {
        this.id_locacion = id_locacion;
        this.nro_conteo = nro_conteo;
        this.id_soporte = id_soporte;
        this.nro_soporte = nro_soporte;
        this.id_letra_soporte = id_letra_soporte;
        this.nivel = nivel;
        this.metro = metro;
        this.scanning = scanning;
        this.cantidad = cantidad;
        this.id_usuario = id_usuario;
        this.id_inventario = id_inventario;
    }

    public Integer getId_locacion() {
        return id_locacion;
    }

    public void setId_locacion(Integer id_locacion) {
        this.id_locacion = id_locacion;
    }

    public Integer getNro_conteo() {
        return nro_conteo;
    }

    public void setNro_conteo(Integer nro_conteo) {
        this.nro_conteo = nro_conteo;
    }

    public Integer getId_soporte() {
        return id_soporte;
    }

    public void setId_soporte(Integer id_soporte) {
        this.id_soporte = id_soporte;
    }

    public Integer getNro_soporte() {
        return nro_soporte;
    }

    public void setNro_soporte(Integer nro_soporte) {
        this.nro_soporte = nro_soporte;
    }

    public Integer getId_letra_soporte() {
        return id_letra_soporte;
    }

    public void setId_letra_soporte(Integer id_letra_soporte) {
        this.id_letra_soporte = id_letra_soporte;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Integer getMetro() {
        return metro;
    }

    public void setMetro(Integer metro) {
        this.metro = metro;
    }

    public String getScanning() {
        return scanning;
    }

    public void setScanning(String scanning) {
        this.scanning = scanning;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Integer getId_inventario() {
        return id_inventario;
    }

    public void setId_inventario(Integer id_inventario) {
        this.id_inventario = id_inventario;
    }
}
