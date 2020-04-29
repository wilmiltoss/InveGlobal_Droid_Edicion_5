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

    /*public static final String CREAR_TABLA_PRODUCTOS="CREATE TABLE IF NOT EXISTS "+TABLA_PRODUCTOS+" ("+CAMPO_ID_USUARIO2+" INT NOT NULL PRIMARY KEY ASC, "
            +CAMPO_NOMBRE_USUARIO2+" VARCHAR NOT NULL, "
            +CAMPO_NIVEL_ACCESO2+" INT NOT NULL)";*/

    public static final String ELIMINAR_TABLA_USUARIO="DROP TABLE IF EXISTS USUARIOS";
    public static final String BORRAR_DATOS_LECTURA="DELETE FROM LECTURAS";
    public static final String INSERTAR_TABLA_USUARIO="INSERT INTO"+TABLA_USUARIO+"VALUES(01,'admin',01)";
    public static final String BUSCAR_USUARIOS="SELECT * FROM "+TABLA_USUARIO;

    public static final String ELIMINAR_TABLA_LOCALES="DROP TABLE IF EXISTS LOCALES";

    //ITRACK**********************************************************************************************
    //PRODUCTOS
    public static String TABLA_PRODUCTOS="PRODUCTOS";
    public static String CAMPO_CODIGO_BARRA ="CODIGO_BARRA";
    public static String CAMPO_DESCRIP ="DESCRIPCION";
    public static String CAMPO_CANT_PROD ="CANTIDAD";
    public static String CAMPO_CATEGORIA ="CATEGORIA";
    public static String CAMPO_LOCAL ="LOCAL";
    public static String CAMPO_TIPO_NEGOCIO = "TIPO_NEGOCIO";
    public static String CAMPO_TIPO_SOPORTE = "TIPO_SOPORTE";
    public static String CAMPO_CADENA_P ="CADENA";
    public static String CAMPO_UBICACION = "UBICACION";
    public static String CAMPO_AUX ="AUX";
    //UBICACIONES
    public static String TABLA_UBICACIONES="UBICACIONES";
    public static String CAMPO_ID_UBICACION="ID_UBICACION";
    public static String CAMPO_DESCR_UBICACION ="DESCR_UBICACION";
    //TIPO_NEGOCIO
    public static String TABLA_TIPO_NEGOCIO="TIPO_NEGOCIO";
    public static String CAMPO_ID_TN ="ID";
    public static String CAMPO_DESCRIPCION_TN = "DESCRIPCION";
    //CADENAS
    public static String TABLA_CADENAS ="CADENAS";
    public static String CAMPO_ID_CADENA = "IDCADENA";
    public static String CAMPO_DESCR_CADENA = "DESCRCADENA";
    public static String CAMPO_ID_TIPO_NEGOCIO = "IDTIPONEGOCIO";
    //LOCALES
    public static String TABLA_LOCALES="LOCALES";
    public static String CAMPO_CODIGO="CODIGO";
    public static String CAMPO_DESCRIPCION_LOCAL ="DESCRIPCION";
    public static String CAMPO_CADENA ="CADENA";
    //LISTADO
    public static String TABLA_LISTADO="LISTADO";
    public static String CAMPO_SCANNING_LISTA="SCANNING";
    public static String CAMPO_DESCRIP_LISTA ="DESCRIPCION";
    public static String CAMPO_CANTIDAD_LISTA ="CANTIDAD";



    //final son las Sentencias  SQL dadas
    //ITRACK..............CREACION DE TABLAS.....................................
    public static final String CREAR_TABLA_PRODUCTOS="CREATE TABLE IF NOT EXISTS "+TABLA_PRODUCTOS+" ("+CAMPO_CODIGO_BARRA+" VARCHAR NOT NULL, "
                                                                                                         +CAMPO_DESCRIP+" VARCHAR NOT NULL, "
                                                                                                         +CAMPO_CANT_PROD+" FLOAT, "
                                                                                                         +CAMPO_CATEGORIA+" VARCHAR NULL, "
                                                                                                         +CAMPO_LOCAL+" VARCHAR NULL, "
                                                                                                         +CAMPO_CADENA_P+" VARCHAR NULL, "
                                                                                                         +CAMPO_TIPO_NEGOCIO+" VARCHAR NULL, "
                                                                                                         +CAMPO_TIPO_SOPORTE+" VARCHAR NULL, "
                                                                                                         +CAMPO_UBICACION+" VARCHAR NULL, "
                                                                                                         +CAMPO_AUX+" VARCHAR)";

    public static final String CREAR_TABLA_UBICACIONES="CREATE TABLE IF NOT EXISTS "+TABLA_UBICACIONES+" (" +CAMPO_ID_UBICACION+" INTEGER PRIMARY KEY, "
                                                                                                            +CAMPO_DESCR_UBICACION+" VARCHAR)";

    public static final String CREAR_TABLA_TIPO_NEGOCIO="CREATE TABLE IF NOT EXISTS "+TABLA_TIPO_NEGOCIO+" (" +CAMPO_ID_TN+" INTEGER PRIMARY KEY, "
                                                                                                              +CAMPO_DESCRIPCION_TN+" VARCHAR)";

    public static final String CREAR_TABLA_CADENAS="CREATE TABLE IF NOT EXISTS "+TABLA_CADENAS+" (" +CAMPO_ID_CADENA+" INTEGER PRIMARY KEY, "
                                                                                                    +CAMPO_DESCR_CADENA+ " VARCHAR, "
                                                                                                     +CAMPO_ID_TIPO_NEGOCIO+" INTEGER)";

    public static final String CREAR_TABLA_LOCALES="CREATE TABLE IF NOT EXISTS "+TABLA_LOCALES+" (" +CAMPO_CODIGO+" INTEGER PRIMARY KEY, "
                                                                                                    +CAMPO_DESCRIPCION_LOCAL+" VARCHAR, "
                                                                                                    +CAMPO_CADENA+" INT NULL)";
    public static final String CREAR_TABLA_LISTADO="CREATE TABLE IF NOT EXISTS "+TABLA_LISTADO+"  ("+CAMPO_SCANNING_LISTA+" VARCHAR PRIMARY KEY, "
                                                                                                    +CAMPO_DESCRIP_LISTA+" VARCHAR, "
                                                                                                    +CAMPO_CANTIDAD_LISTA+" INTEGER)";

    //ITRACK..............CONSULTA DE TABLAS.....................................
    public static String CONSULTA_TABLA_PRODUCTOS="SELECT * FROM "+TABLA_PRODUCTOS;
    public static String CONSULTA_TABLA_LOCALES="SELECT * FROM "+TABLA_LOCALES;
    public static String CONSULTA_TABLA_UBICACIONES="SELECT * FROM "+TABLA_UBICACIONES;
    public static String CONSULTA_TABLA_TIPO_NEGOCIO="SELECT * FROM "+TABLA_TIPO_NEGOCIO;
    public static String CONSULTA_TABLA_CADENAS="SELECT * FROM "+TABLA_CADENAS;
    public static String CONSULTA_TABLA_SOPORTE="SELECT * FROM "+TABLA_SOPORTE;
    public static String CONSULTA_TABLA_MAESTRO="SELECT * FROM "+TABLA_MAESTRO;
    public static String CONSULTA_TABLA_LISTADO="SELECT * FROM "+TABLA_LISTADO;
}
