package com.example.wmiltos.inveglobal_droid.iTrack;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Lecturas;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Productos;
import com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV.Locales;
import com.example.wmiltos.inveglobal_droid.iTrack.utils_csv.AdaptadorLocales;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.GeneraArchivoActivity;
import com.example.wmiltos.inveglobal_droid.principal.ventanas.LecturasActivity;
import com.example.wmiltos.inveglobal_droid.principal.ventanas.StockActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileLockInterruptionException;
import java.util.ArrayList;
import java.util.List;

public class QuiebreActivity extends AppCompatActivity {

    EditText etScanning;
    Button btnBuscar;
    ConexionSQLiteHelper conn;
    private ListView textViewPanel;
    List<Locales> listaLocales = new ArrayList<>();//previas configuraciones del adaptador y la tablaCSV
    AdaptadorLocales adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiebre);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pedirPermisos();
        variables();
        llenaListView();
        crearTablas();


        //del icono correo
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void variables() {
        conn = new ConexionSQLiteHelper(getApplicationContext(), "InveStock.sqlite", null, 1);
        btnBuscar = findViewById(R.id.btn_buscar);
        etScanning = findViewById(R.id.et_scan_lectura_quiebre);
        textViewPanel = findViewById(R.id.textViewPanel);

    }

    //busqueda de articulos en el maestro para guardarlos en la tabla productos
    public void onClickBuscar(View view) {
        //SQLiteDatabase db = conn.getReadableDatabase();
        //db.execSQL(Utilidades.ELIMINAR_TABLA_LOCALES);
        importarCSV();
        if (etScanning.getText().length() != 0) {//si el campo esta vacio, mostrar mensaje
            switch (view.getId()) {
                case R.id.btn_buscar:
                    envioDatosAgregar();//enviar a la sgte ventana
            }
        } else {
            Toast.makeText(getApplicationContext(), "Busqueda sin resultado", Toast.LENGTH_SHORT).show();
        }
    }

    public void envioDatosAgregar(){
        Intent miIntent = null;
        String msjScanning = etScanning.getText().toString();
        miIntent = new Intent(QuiebreActivity.this, AgregarActivity.class);
        Bundle miBundle = new Bundle();
        miBundle.putString("msjScanning", etScanning.getText().toString());
        miIntent.putExtras(miBundle);
        startActivity(miIntent);
        QuiebreActivity.this.finish();
    }

    //muestra la lista de la tabla productos
    public void llenaListView(){
        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.mylistado, cargar());//myListado =cambio manual del tamaño list
            textViewPanel.setAdapter(adapter);
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "no hay registros", Toast.LENGTH_LONG).show();
        }
    }

    //cargar la vista preeliminar del listview
    public String []cargar() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = null;

            cursor = db.rawQuery("SELECT " + Utilidades.CAMPO_ID + "," +
                    Utilidades.CAMPO_CODIGO_BARRA + "," +
                    Utilidades.CAMPO_DESCRIP + "," +
                    Utilidades.CAMPO_CANT_PROD
                    + " FROM " + Utilidades.TABLA_PRODUCTOS, null);
            String[] listado = new String[cursor.getCount()];//arreglo string que trae el listado
            int post = 0;
            if (cursor.moveToFirst()) {//si tenemo al menos 1 reg lo recorremos
                do {
                    String id = cursor.getString(0);
                    String codigo = cursor.getString(1);
                    String Descripcion = cursor.getString(2);
                    String Cantidad = cursor.getString(3);
                    listado[post] = id + " -" + codigo + "-" + Descripcion + "  " + Cantidad;
                    post++;

                } while (cursor.moveToNext());//mientras se mueve al sgte registro
                if (cursor.getCount() <= 0) {
                    cursor.close();
                    Toast.makeText(getApplicationContext(), "No se registró ningun articulo", Toast.LENGTH_LONG).show();
                }
            }
            return listado;
    }

    //DE LA CREACION DE LA TABLA
    private void crearTablas() {
        try {
        SQLiteDatabase db = conn.getReadableDatabase();
        // Comprobar si existe las tablas
        if (isTableExists("PRODUCTOS") && isTableExists("LOCALES")){
           // Toast.makeText(getApplicationContext(), "la tabla ya existe", Toast.LENGTH_SHORT).show();
        }else {//sino existe crearla
            db.execSQL(Utilidades.CREAR_TABLA_PRODUCTOS);
            db.execSQL(Utilidades.CREAR_TABLA_LOCALES);
            Toast.makeText(getApplicationContext(), "tablas creadas correctamente", Toast.LENGTH_SHORT).show();
        }
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "error en crear las tablas", Toast.LENGTH_SHORT).show();
        }
    }

    //comprueba si existe la tabla en la bd sqlite
    public boolean isTableExists(String nombreTabla) {
        SQLiteDatabase db = conn.getReadableDatabase();
        boolean isExist = false;
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + nombreTabla + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                isExist = true;
               // Toast.makeText(getApplicationContext(), "la tabla existe", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "la/s tabla/s no existe/n", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }
        return isExist;
    }

    //genera un archivo productos.csv y borra la tabla productos
    public void onClickFinalizar(View view) {
        validarRegistroBD();
    }

    //valida si existe registro en la tabla Lectura para generar archivo
    public void validarRegistroBD () {
        SQLiteDatabase db = conn.getWritableDatabase();
        Productos productos = null;
        try{
            String Query = "Select count(CODIGO_BARRA) as codigoBarra from " +Utilidades.TABLA_PRODUCTOS;
            Cursor cursor = db.rawQuery(Query, null);
            //mostrar en el log la consulta select
            while (cursor.moveToNext()) {
                productos = new Productos();//llamamos a la tabla Lecturas.java
                productos.setCodigo_barra(cursor.getString(0));//la 1ra columna
                Log.i("cantidad", productos.getCodigo_barra());//mostrar en el log
            }
            if(productos.getCodigo_barra().equals("0"))//si no hay registro enviar mensaje
            {
                Toast.makeText(getApplicationContext(), "No Hay Datos de Productos", Toast.LENGTH_LONG).show();
            }else {  //sino guardar en las carpetas destinadas
                //Copia los datos de lectura a un nuevo directorio "InveGlobal"
                File origen = new File("sdcard/Download/ProductosIG.csv");//de origen
                File destino = new File("sdcard/Inve_Back/ProductosIG.csv");//copia en destino
                copiarDirectorio(origen, destino);
                //escribe de la bd al archivo csv
                escribirLecturas2();
                Toast.makeText(getApplicationContext(), "Archivo ProductosIG.csv Generado!", Toast.LENGTH_LONG).show();
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
        String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;//modelo del equipo
        try{
            conn= new ConexionSQLiteHelper(getApplicationContext(),"InveStock.sqlite",null,1);
            db = conn.getWritableDatabase();
            cursor = db.rawQuery("SELECT " +Utilidades.CAMPO_ID+","
                                                +Utilidades.CAMPO_CODIGO_BARRA+","
                                                +Utilidades.CAMPO_DESCRIP+","
                                                +Utilidades.CAMPO_CANT_PROD+","
                                                +Utilidades.CAMPO_CATEGORIA+","
                                                +Utilidades.CAMPO_LOCAL+
                    " FROM "+Utilidades.TABLA_PRODUCTOS, null);
           // Toast.makeText(getApplicationContext(), "respuesta1"+cursor.toString(), Toast.LENGTH_LONG).show();
            while(cursor.moveToNext())//recorrido del cursor
                //escribe las lineas del cursor en lLineas y lo formatea con puntos y coma
                lLineas.add(String.format("%s;%s;%s;%s;%s;%s",
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)));
           // Toast.makeText(getApplicationContext(), "respuesta2"+lLineas, Toast.LENGTH_LONG).show();
            cursor.close();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }finally {//en caso de respuestas vacias
            if(null != db && db.isOpen())//si no habre la bd
                db.close();//lo cierra
            if(null != cursor)//si la consulta sql no tiene registros
                cursor.close();//cierra la consulta
            if(null != conn)//si no hay conexion
                conn.close();//cierra la conexion
        }
        if(!lLineas.isEmpty()) {//si lLineas es distinto a vacio
            OutputStreamWriter flujoSalida = null;//escribe la salida en la variable flujoSalida
            File  file = null;
            File ruta_divice = null;
            String pathDir = null;
            //Toast.makeText(getApplicationContext(), "respuesta3"+lLineas, Toast.LENGTH_LONG).show();
            try {
                //guarda el archivo en la memoria externa  de la aplicacion
                ruta_divice = new File (Environment.getExternalStorageDirectory()+"/Download");//guarda en la carpeta /download/
                pathDir = ruta_divice+ "";
                //Toast.makeText(getApplicationContext(), "respuesta4"+ruta_divice.toString(), Toast.LENGTH_LONG).show();
                //_____________________________________________________________________________
                //almacenamiento externo **Dar permiso en el aplicacion del dispositivo
                file = new File(pathDir, "ProductosIG.csv");//se puede decirle la ruta aca
                flujoSalida = new OutputStreamWriter(new FileOutputStream(file));
                //proceso de lectura_________________________________________________________________
                flujoSalida.write("Nombre del Dispositivo: "+deviceName+"\r\n");//nombre del equipo
                flujoSalida.write("ID;CODIGO_BARRA;DESCRIPCION;CANTIDAD;CATEGORIA;LOCAL \r\n");//titulos
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


    //proceso de generacion de copia de archivo y generacion de directorio
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

    //*****************************************************************************************************************
    public void importarCSV() {
        //limpiarTablas("usuarios");
        File carpeta = new File(Environment.getExternalStorageDirectory() + "/Download");
        String archivoAgenda = carpeta.toString() + "/" + "locales.csv";
        boolean isCreate = false;
        if(!carpeta.exists()) {
            Toast.makeText(this, "NO EXISTE LA CARPETA", Toast.LENGTH_SHORT).show();
        } else {
            String cadena;
            String[] arreglo;
            try {
                FileReader fileReader = new FileReader(archivoAgenda);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
               // Toast.makeText(this, "respuesta2"+bufferedReader, Toast.LENGTH_SHORT).show();
                while((cadena = bufferedReader.readLine()) != null) {
                    arreglo = cadena.split(",");
                    //AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(MainActivity.this, "dbSistema", null, 1);
                    SQLiteDatabase db = conn.getWritableDatabase();
                    ContentValues registro = new ContentValues();

                    registro.put("CODIGO", arreglo[0]);
                    registro.put("DESCRIPCION", arreglo[1]);
                    registro.put("CADENA", arreglo[2]);

                    listaLocales.add(
                            new Locales(
                                    arreglo[0],
                                    arreglo[1],
                                    arreglo[2]
                            )
                    );

                    // los inserto en la base de datos
                    db.insert("LOCALES", null, registro);
                    db.close();
                    Toast.makeText(QuiebreActivity.this, "SE IMPORTO EXITOSAMENTE", Toast.LENGTH_SHORT).show();
                    //llama el adaptador en QuiebreActivity
                    //adaptador = new AdaptadorLocales(QuiebreActivity.this, listaLocales);
                    //rvLo.setAdapter(adaptador);
                }
            } catch(Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void pedirPermisos() {
        // PERMISOS PARA ANDROID 6 O SUPERIOR
        if(ContextCompat.checkSelfPermission(QuiebreActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(QuiebreActivity.this, new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);

        }
    }
}
