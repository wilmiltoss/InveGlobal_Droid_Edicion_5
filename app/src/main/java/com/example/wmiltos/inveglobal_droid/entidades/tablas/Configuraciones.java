package com.example.wmiltos.inveglobal_droid.entidades.tablas;

public class Configuraciones {
    private Integer cantidad_maxima_conteo;
    private Integer cantidad_conteo_decimal;
    private Integer id_inventario;
    private Integer ip_sql_server;

    public Configuraciones() {
        this.cantidad_maxima_conteo = cantidad_maxima_conteo;
        this.cantidad_conteo_decimal = cantidad_conteo_decimal;
        this.id_inventario = id_inventario;
        this.ip_sql_server = ip_sql_server;
    }

    public Integer getCantidad_maxima_conteo() {
        return cantidad_maxima_conteo;
    }

    public void setCantidad_maxima_conteo(Integer cantidad_maxima_conteo) {
        this.cantidad_maxima_conteo = cantidad_maxima_conteo;
    }

    public Integer getCantidad_conteo_decimal() {
        return cantidad_conteo_decimal;
    }

    public void setCantidad_conteo_decimal(Integer cantidad_conteo_decimal) {
        this.cantidad_conteo_decimal = cantidad_conteo_decimal;
    }

    public Integer getId_inventario() {
        return id_inventario;
    }

    public void setId_inventario(Integer id_inventario) {
        this.id_inventario = id_inventario;
    }

    public Integer getIp_sql_server() {
        return ip_sql_server;
    }

    public void setIp_sql_server(Integer ip_sql_server) {
        this.ip_sql_server = ip_sql_server;
    }
}
