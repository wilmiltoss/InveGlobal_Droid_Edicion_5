package com.example.wmiltos.inveglobal_droid.entidades.tablas;

public class Inventario_Soporte {

    private Integer ID_INVENTARIO_SOPORTE;
    private Integer ID_INVENTARIO;
    private Integer ID_CLAVE;
    private Integer CONTEO1;
    private Integer CONTEO2;
    private Integer CONTEO3;

    public Inventario_Soporte(Integer id_inventario_soporte, Integer id_inventario, Integer id_clave, Integer conteo1, Integer conteo2, Integer conteo3) {
        this.ID_INVENTARIO_SOPORTE = id_inventario_soporte;
        this.ID_INVENTARIO = id_inventario;
        this.ID_CLAVE = id_clave;
        this.CONTEO1 = conteo1;
        this.CONTEO2 = conteo2;
        this.CONTEO3 = conteo3;
    }
    public Inventario_Soporte(){
    }

    public Integer getID_INVENTARIO_SOPORTE() {
        return ID_INVENTARIO_SOPORTE;
    }

    public void setID_INVENTARIO_SOPORTE(Integer ID_INVENTARIO_SOPORTE) {
        this.ID_INVENTARIO_SOPORTE = ID_INVENTARIO_SOPORTE;
    }

    public Integer getID_INVENTARIO() {
        return ID_INVENTARIO;
    }

    public void setID_INVENTARIO(Integer ID_INVENTARIO) {
        this.ID_INVENTARIO = ID_INVENTARIO;
    }

    public Integer getID_CLAVE() {
        return ID_CLAVE;
    }

    public void setID_CLAVE(Integer ID_CLAVE) {
        this.ID_CLAVE = ID_CLAVE;
    }

    public Integer getCONTEO1() {
        return CONTEO1;
    }

    public void setCONTEO1(Integer CONTEO1) {
        this.CONTEO1 = CONTEO1;
    }
    public Integer getCONTEO2() {
        return CONTEO2;
    }

    public void setCONTEO2(Integer CONTEO2) {
        this.CONTEO2 = CONTEO2;
    }

    public Integer getCONTEO3() {
        return CONTEO3;
    }

    public void setCONTEO3(Integer CONTEO3) {
        this.CONTEO3 = CONTEO3;
    }
}
