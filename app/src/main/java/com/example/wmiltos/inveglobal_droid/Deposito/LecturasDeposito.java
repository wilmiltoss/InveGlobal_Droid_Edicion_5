package com.example.wmiltos.inveglobal_droid.Deposito;

public class LecturasDeposito {
    private Integer id_conteo;
    private Integer local;
    private Integer ubicacion;
    private Integer codigo_barra;
    private Integer cantidad;
    private Integer cod_u_manipulacion;

    public LecturasDeposito() {
        this.id_conteo = id_conteo;
        this.local = local;
        this.ubicacion = ubicacion;
        this.codigo_barra = codigo_barra;
        this.cantidad = cantidad;
        this.cod_u_manipulacion = cod_u_manipulacion;
    }

    public Integer getId_conteo() {
        return id_conteo;
    }

    public Integer getLocal() {
        return local;
    }

    public Integer getUbicacion() {
        return ubicacion;
    }

    public Integer getCodigo_barra() {
        return codigo_barra;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Integer getCod_u_manipulacion() {
        return cod_u_manipulacion;
    }

    public void setId_conteo(Integer id_conteo) {
        this.id_conteo = id_conteo;
    }

    public void setLocal(Integer local) {
        this.local = local;
    }

    public void setUbicacion(Integer ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setCodigo_barra(Integer codigo_barra) {
        this.codigo_barra = codigo_barra;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setCod_u_manipulacion(Integer cod_u_manipulacion) {
        this.cod_u_manipulacion = cod_u_manipulacion;
    }
}
