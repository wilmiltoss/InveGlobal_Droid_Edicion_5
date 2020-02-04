package com.example.wmiltos.inveglobal_droid.utilidades;

public class Utilidades {

    //CONSTANTES DE LOS CAMPOS DE LA TABLA USUARIO
    public static String TABLA_USUARIO="USUARIOS";
    public static String CAMPO_ID_USUARIO="ID_USUARIO";
    public static String CAMPO_NOMBRE_USUARIO="NOMBRE_USUARIO";
    public static String CAMPO_NIVEL_ACCESO="NIVEL_ACCESO";
    public static String CAMPO_CONTRASENA="CONTRASENA";
    //TALA LOCACIONES
    public static String TABLA_LOCACION="LOCACIONES";
    public static String CAMPO_ID_LOCACION="ID_LOCACION";
    public static String CAMPO_DESCRIPCION="DESCRIPCION";
    //TABLA SOPORTE
    public static String TABLA_SOPORTE="SOPORTES";
    public static String CAMPO_ID_SOPORTE="ID_SOPORTE";
    public static String CAMPO_DESCRIPCION_SOPORTE="DESCRIPCION";
    public static String CAMPO_SUBDIVISIBLE="SUBDIVISIBLE";

    //TABLA MAESTRO
    public static String TABLA_MAESTRO="MAESTRO";
    public static String CAMPO_SCANNING="SCANNING";
    public static String CAMPO_DESCRIPCION_SCANNING="DESCRIPCION";
    public static String CAMPO_DETALLE="DETALLE";
    public static String CAMPO_PESABLE="PESABLE";
    public static String CAMPO_COSTO="COSTO";
    public static String CAMPO_ID_SECTOR="ID_SECTOR";
    //TABLA SECTOR
    public static String TABLA_SECTORES="SECTORES";
    public static String CAMPO_IDSECTOR="ID_SECTOR";
    public static String CAMPO_DESCRIPCION_SECTOR="DESCRIPCION";

    //TABLA LECTURAS
    public static String TABLA_LECTURAS="LECTURAS";
    public static String CAMPO_ID_LOCACION_L="ID_LOCACION";
    public static String CAMPO_NRO_CONTEO="NRO_CONTEO";
    public static String CAMPO_ID_SOPORTE_L="ID_SOPORTE";
    public static String CAMPO_NRO_SOPORTE_L="NRO_SOPORTE";
    public static String CAMPO_LETRA_SOPORTE_L="ID_LETRA_SOPORTE";
    public static String CAMPO_NIVEL="NIVEL";
    public static String CAMPO_METRO="METRO";
    public static String CAMPO_SCANNING_L="SCANNING";
    public static String CAMPO_CANTIDAD="CANTIDAD";
    public static String CAMPO_ID_USUARIO_L="ID_USUARIO";
    public static String CAMPO_ID_INVENTARIO_L="ID_INVENTARIO";



    //TABLA LETRAS SOPORTE
    public static String TABLA_LETRAS_SOPORTE="LETRAS_SOPORTES";
    public static String CAMPO_ID_LETRA_SOPORTE="ID_LETRA_SOPORTE";
    public static String CAMPO_LETRA="LETRA";

    //TABLA CONFIGURACIONES
    public static String TABLA_CONFIGURACIONES="CONFIGURACIONES";
    public static String CAMPO_CANTIDAD_MAXIMA_CONTEO="CANTIDAD_MAXIMA_CONTEO";
    public static String CAMPO_CANTIDAD_CONTEO_CON_DECIMALES="CANTIDAD_CONTEO_CON_DECIMALES";
    public static String CAMPO_ID_INVENTARIO="ID_INVENTARIO";
    public static String CAMPO_IP_SQL_SERVER="IP_SQL_SERVER";

    //TABLA INVENTARIO_SOPORTE
    public static String TABLA_INVENTARIO_SOPORTE="INVENTARIO_SOPORTE";
    public static String CAMPO_ID_INVENTARIO_SOPORTE="ID_INVENTARIO_SOPORTE";
    public static String CAMPO_ID_INVENTARIO_IS="ID_INVENTARIO";
    public static String CAMPO_ID_CLAVE="ID_CLAVE";
    public static String CAMPO_ID_SOPORTE_IS="ID_SOPORTE";
    public static String CAMPO_NRO_SOPORTE="NRO_SOPORTE";
    public static String CAMPO_NRO_TIPO_SOPORTE="NRO_TIPO_SOPORTE";
    public static String CAMPO_DESCRIPCION_IS="DESCRIPCION_SOPORTE";
    public static String CAMPO_CONTEO_1="CONTEO1";
    public static String CAMPO_CONTEO_2="CONTEO2";
    public static String CAMPO_CONTEO_3="CONTEO3";
    public static String CAMPO_ID_LOCACIONES="ID_LOCACIONES";





    //final son las Sentencias  SQL dadas
    public static final String CREAR_TABLA_USUARIO="CREATE TABLE IF NOT EXISTS "+TABLA_USUARIO+" ("+CAMPO_ID_USUARIO+" INT NOT NULL PRIMARY KEY ASC, "
                                                                                     +CAMPO_NOMBRE_USUARIO+" VARCHAR NOT NULL, "
                                                                                     +CAMPO_NIVEL_ACCESO+" INT NOT NULL)";

    public static final String ELIMINAR_TABLA_USUARIO="DROP TABLE IF EXISTS USUARIOS";
    public static final String BORRAR_DATOS_LECTURA="DELETE FROM LECTURAS";
    public static final String INSERTAR_TABLA_USUARIO="INSERT INTO"+TABLA_USUARIO+"VALUES(01,'admin',01)";

    public static final String BUSCAR_USUARIOS="SELECT * FROM "+TABLA_USUARIO;

}
