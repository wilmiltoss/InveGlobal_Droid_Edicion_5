package com.example.wmiltos.inveglobal_droid.Deposito;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Lecturas;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class GenerarArchivoDeposito extends AppCompatActivity {


    Button generarArchivo;
    ConexionSQLiteHelper conn;
    TextView cantidadRegistros, nombreEquipo;
    boolean sdDisponible = false;
    boolean sdAccesoEscritura = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_archivo_deposito);

        cantidadRegistros = findViewById(R.id.tvCampoCantidadRegistrosG);
        generarArchivo =  findViewById(R.id.btn_generaArchivo);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "InveStock.sqlite", null, 1);
        nombreEquipo =  findViewById(R.id.text_nombreEquipo);
        sumaRegistrosSQL();
        //muestra el nombre del equipo
        String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
        nombreEquipo.setText("Nombre del Equipo: " + deviceName);
        comprobarTarjetaSD();

    }

    private void comprobarTarjetaSD (){

        String estado = Environment.getExternalStorageState();
        if(estado.equals(Environment.MEDIA_MOUNTED)){
            sdDisponible = true;
            sdAccesoEscritura = true;
        }else   if(estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){

            sdDisponible = true;
            sdAccesoEscritura = false;
        }else {
            sdDisponible = false;
            sdAccesoEscritura = false;
        }
    }


    public void sumaRegistrosSQL(){
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor c = db.rawQuery("select * from "+ Utilidades.TABLA_DEPOSITO,null);
        cantidadRegistros.setText("Cantidad de Lecturas:   "+c.getCount());
        db.close();
    }

    public void generarArchivo(View view) {

        validarRegistroBD();
    }

    public void validarRegistroBD () {
        SQLiteDatabase db = conn.getWritableDatabase();
        LecturasDeposito lecturasDep = null;
        try{
            String Query = "Select count(CODIGO_BARRA) as cantidad from " +Utilidades.TABLA_DEPOSITO+" where " +Utilidades.CAMPO_ID_CONTEO;
            Cursor cursor = db.rawQuery(Query, null);
            //mostrar en el log la consulta select
            while (cursor.moveToNext()) {
                lecturasDep = new LecturasDeposito();//llamamos a la tabla Lecturas.java
                lecturasDep.setCodigo_barra(0);//la 1ra columna
            }
            if(lecturasDep.getCodigo_barra().equals("0"))//si no hay registro enviar mensaje
            {
                Toast.makeText(getApplicationContext(), "No Hay Datos de Lecturas", Toast.LENGTH_LONG).show();
                pasarAmenuPrincipal();
            }else {   //sino guardar en las carpetas destinadas
                //Copia los datos de lectura a un nuevo directorio "InveGlobal"
                File origen = new File("sdcard/Download/lecturaDeposito.csv");//de origen
                File destino = new File("sdcard/Inve_Back/lecturaDeposito.csv");//copia en destino
                copiarDirectorio(origen, destino);
                //escribe de la bd al archivo csv
                escribirLecturas2();
                Toast.makeText(getApplicationContext(), "Archivo Generado!", Toast.LENGTH_LONG).show();
                pasarAmenuPrincipal();
            }
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Error al leer fichero en la memoria interna", Toast.LENGTH_LONG).show();
        }
    }

    //genera el archivo csv
    public void escribirLecturas2(){
        ConexionSQLiteHelper conn = null;
        Cursor cursor = null;
        SQLiteDatabase db = null;
        List<String> lLineas= new ArrayList<>();//array
        //String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;//modelo del equipo
        try{
            conn= new ConexionSQLiteHelper(getApplicationContext(),"InveStock.sqlite",null,1);
            db = conn.getWritableDatabase();
            cursor = db.rawQuery("SELECT " +Utilidades.CAMPO_ID_CONTEO+","
                                                +Utilidades.CAMPO_LOCAL_DEP+","
                                                +Utilidades.CAMPO_UBICACION_DEP+","
                                                +Utilidades.CAMPO_COD_BARRA_DEP+","
                                                +Utilidades.CAMPO_CANTIDAD_DEP+","
                                                +Utilidades.CAMPO_COD_UND_MANIPULACION+
                                                " FROM "+Utilidades.TABLA_DEPOSITO, null);
            while(cursor.moveToNext())
                //escribe las lineas del cursor en lLineas y lo formatea con puntos y coma
                lLineas.add(String.format("%s;%s;%s;%s;%s;%s",
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)));
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al generar registros", Toast.LENGTH_LONG).show();
        }finally {
            if(null != db && db.isOpen())//si no habre la bd
                db.close();//lo cierra
            if(null != cursor)//si la consulta sql no tiene registros
                cursor.close();//cierra la consulta
            if(null != conn)//si no hay conexion
                conn.close();//cierra la conexion
        }
        if(!lLineas.isEmpty()) {//si lLineas es distinto a vacio
            OutputStreamWriter flujoSalida = null;//lo va almacenar en fout
            File ruta_sd3 = null;
            File file = null;
            String pathDir = null;
            try {
                //guarda el archivo en la memoria externa  de la aplicacion
                ruta_sd3 = new File (Environment.getExternalStorageDirectory()+"/Download");//guarda en la carpeta /sdcard/
                //guarda en la memoria externa de la aplicacion(sdcard)
                pathDir = ruta_sd3+ "";
                //_____________________________________________________________________________
                //almacenamiento externo **Dar permiso en el aplicacion del dispositivo
                file = new File(pathDir, "lecturaDeposito.csv");//se puede decirle la ruta aca
                flujoSalida = new OutputStreamWriter(new FileOutputStream(file));
                //proceso de lectura_________________________________________________________________
                //flujoSalida.write("Nombre del Equipo: "+deviceName+"\r\n");//nombre del equipo
                flujoSalida.write("ID;LOCAL;UBICACION;CODIGO_BARRA;CANTIDAD;COD_UND_MANIPULACION \r\n");//CABECERA
                for(String cLinea : lLineas)
                    flujoSalida.write(cLinea+"\r\n");//salto de lineas
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(), "Error al generar archivo", Toast.LENGTH_LONG).show();
            }finally {
                if(null != flujoSalida) {
                    try {
                        flujoSalida.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void copiarDirectorio(File sourceLocation , File targetLocation) {
        if (sourceLocation.isDirectory()) {//mapea el directorio
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {//en caso de error
                Log.e("Error", "No puede crear directorio: " + targetLocation.getAbsolutePath());
            }
            String[] children = sourceLocation.list();//guarda en un array "children" la lista
            for (int i=0; i<children.length; i++) {//si ya no hay filas
                //copiamos el archivo de la carpeta "sourceLocation" a "targetLocation"
                copiarDirectorio(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
            }
        } else {  //si no existe el directorio la crea
            File directory = targetLocation.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                Log.e("Error", "No puede crear directorio: " + directory.getAbsolutePath());
            }
            try {
                InputStream in = new FileInputStream(sourceLocation);//carpeta origen
                OutputStream out = new FileOutputStream(targetLocation);//carpeta destino
                //Copiar bits de inputStream a outputStream.
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }catch(IOException ioe){
                Log.e("Error", "Error " + ioe.getMessage());
            }
        }
    }

    private void pasarAmenuPrincipal() {
        GenerarArchivoDeposito.this.finish();//finaliza la ventana anterior
    }

}
