package com.example.wmiltos.inveglobal_droid.iTrack;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Productos;
import com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV_Objetos.Product;
import com.example.wmiltos.inveglobal_droid.iTrack.utils_csv.AdaptadorLista;
import com.example.wmiltos.inveglobal_droid.iTrack.utils_csv.CapturaListViewActivity;
import com.example.wmiltos.inveglobal_droid.iTrack.utils_csv.ObjetoList;
import com.example.wmiltos.inveglobal_droid.principal.login.LoginActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuiebreActivity extends AppCompatActivity {

    EditText etScanning;
    Button btnBuscar;
    TextView textCadena,textTipoNegocio,textLocal,textTipoSoporte,textUsuario;
    ConexionSQLiteHelper conn;
    private ListView textViewPanel;
    private ListAdapter adapter;
    private ArrayList <Product>Pro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiebre);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        variables();
        llenaListView();
        validarRecepcionDatos();

        /*final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.mylistado, cargar());//myListado =cambio manual del tamaño list
        textViewPanel.setAdapter(adapter);*/



        //del icono correo
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        //SELECCIONANDO UN ITEMS DE LA LISTA PASA A LA SIGUIENTE VENTANA
        textViewPanel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String extraer = (textViewPanel.getItemAtPosition (position)).toString();
                    //String textItemList = (String) textViewPanel.getItemAtPosition(position);
                    //Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "posicion"+ extraer, Toast.LENGTH_SHORT).show();
                    //envioDatosAgregar();//enviar a la sgte ventan
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    //boton fisico atraz
    @Override
    public void onBackPressed() {
        dialogo();
    }

    //dialogo antes de salir
    public void dialogo(){
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(QuiebreActivity.this);
        dialogo.setMessage("Desea cancelar la carga actual?").setTitle("Cancelar Carga")
                .setIcon(R.drawable.alert_dialogo);
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                borrarDatosProductos();
                QuiebreActivity.this.finish();//finaliza la ventana
            }
        });
        dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    private void variables() {
        conn = new ConexionSQLiteHelper(getApplicationContext(), "InveStock.sqlite", null, 1);
        btnBuscar = findViewById(R.id.btn_buscar);
        etScanning = findViewById(R.id.et_scan_lectura_quiebre);
        textViewPanel = findViewById(R.id.listViewPanel);
        textCadena = findViewById(R.id.text_cadena);
        textTipoNegocio = findViewById(R.id.text_tipo_negocio);
        textLocal = findViewById(R.id.text_local);
        textTipoSoporte = findViewById(R.id.text_tipo_soporte);
        textUsuario = findViewById(R.id.text_usuarios);

    }

    //busqueda de articulos en el maestro para guardarlos en la tabla productos
    public void onClickBuscar(View view) {
            //si el campo scanning esta cargado envia los datos
            if (etScanning.getText().length() != 0) {//si el campo esta vacio, mostrar mensaje
                switch (view.getId()) {
                    case R.id.btn_buscar:envioDatosAgregar();//enviar a la sgte ventana
                }
            } else {
                Toast.makeText(getApplicationContext(), "Busqueda sin resultado", Toast.LENGTH_SHORT).show();
            }
    }

    public void envioDatosAgregar(){//A-E1
        SQLiteDatabase db = conn.getReadableDatabase();
        //comprobamos 1ro si el codigo existe
        String Query = Utilidades.CONSULTA_TABLA_MAESTRO;
        String parametro = etScanning.getText().toString();
        Cursor cursorExistCodigo = db.rawQuery(Query+" WHERE "+Utilidades.CAMPO_SCANNING+" = "+parametro, null);
        //si existe el enviamos los datos
        if (cursorExistCodigo.getCount() > 0) {
            Intent miIntent = null;
            String msjScanning = etScanning.getText().toString();
            String msjTipoNegocio = textTipoNegocio.getText().toString();
            String msjCadena = textCadena.getText().toString();
            String msjLocal = textLocal.getText().toString();
            String msjTipoSoporte = textTipoSoporte.getText().toString();
            String msjUsuario = textUsuario.getText().toString();

            miIntent = new Intent(QuiebreActivity.this, AgregarActivity.class);
            Bundle miBundle = new Bundle();
            miBundle.putString("msjScanning", etScanning.getText().toString());
            miBundle.putString("msjTipoNegocio", textTipoNegocio.getText().toString());
            miBundle.putString("msjCadena", textCadena.getText().toString());
            miBundle.putString("msjLocal", textLocal.getText().toString());
            miBundle.putString("msjTipoSoporte", textTipoSoporte.getText().toString());
            miBundle.putString("msjUsuario", textUsuario.getText().toString());

            miIntent.putExtras(miBundle);
            startActivity(miIntent);
            QuiebreActivity.this.finish();

        }else{
            Toast.makeText(getApplicationContext(), "El codigo no existe", Toast.LENGTH_LONG).show();
            limpiar();
        }
    }

    private void limpiar() {
        etScanning.setText("");
    }

    public void recepcionDatosAgregarRetorno(){//A-R2

        Bundle miBundle = this.getIntent().getExtras();
        if (miBundle != null) {
            String tipoNegocio = miBundle.getString("msjTipoNegocioR");
            textTipoNegocio.setText(tipoNegocio);

            String cadena = miBundle.getString("msjCadenaR");
            textCadena.setText(cadena);

            String local = miBundle.getString("msjLocalR");
            textLocal.setText(local);

            String tipoSoporte = miBundle.getString("msjTipoSoporteR");
            textTipoSoporte.setText(tipoSoporte);

            String usuario = miBundle.getString("msjUsuarioR");
            textUsuario.setText(usuario);
        }
    }

    public void validarRecepcionDatos(){
        SQLiteDatabase db = conn.getWritableDatabase();
            String Query = Utilidades.CONSULTA_TABLA_PRODUCTOS;//contar registros
            Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() > 0) {
            recepcionDatosAgregarRetorno();
           // Toast.makeText(getApplicationContext(), "si hay registros", Toast.LENGTH_LONG).show();
        }else{
            recepcionDatosConfiguracion();
           // Toast.makeText(getApplicationContext(), "no hay registros", Toast.LENGTH_LONG).show();
        }
        cursor.close();
    }

    private void recepcionDatosConfiguracion() {
        Bundle miBundle = this.getIntent().getExtras();
        if (miBundle != null) {
            String tipoNegocio = miBundle.getString("msjTipoNegocio");
            textTipoNegocio.setText(tipoNegocio);

            String cadena = miBundle.getString("msjCadena");
            textCadena.setText(cadena);

            String local = miBundle.getString("msjLocal");
            textLocal.setText(local);

            String tipoSoporte = miBundle.getString("msjTipoSoporte");
            textTipoSoporte.setText(tipoSoporte);

            String usuario = miBundle.getString("msjUsuario");
            textUsuario.setText(usuario);
        }
    }
    //muestra la lista de la tabla productos
    public void llenaListView(){
        try {
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.mylistado, cargar());//myListado =cambio manual del tamaño list
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


    //genera un archivo productos.csv y borra la tabla productos
    public void onClickFinaliza(View view) {
        validarRegistroBD();
    }

    //valida si existe registro en la tabla Lectura para generar archivo
    public void validarRegistroBD () {
        SQLiteDatabase db = conn.getWritableDatabase();
        Productos productos = null;
        try{
            String Query = "Select count(CODIGO_BARRA) as codigoBarra from " +Utilidades.TABLA_PRODUCTOS;//contar registros
            Cursor cursor = db.rawQuery(Query, null);
            //mostrar en el log la consulta select
            while (cursor.moveToNext()) {
                productos = new Productos();//llamamos a la tabla Lecturas.java
                productos.setCodigo_barra(cursor.getString(0));//la 1ra columna
                Log.i("cantidad", productos.getCodigo_barra());//mostrar en el log
            }
            if(productos.getCodigo_barra().equals("0"))//si no hay registro enviar mensaje
            {
                Toast.makeText(getApplicationContext(), "No Hay Datos de Product", Toast.LENGTH_LONG).show();
            }else {  //sino guardar en las carpetas destinadas
                //Copia los datos de lectura a un nuevo directorio "InveGlobal"
                File origen = new File("sdcard/Download/ProductosIG.csv");//de origen
                File destino = new File("sdcard/Inve_Back/ProductosIG.csv");//copia en destino
                copiarDirectorio(origen, destino);
                //escribe de la bd al archivo csv
                escribirLecturas2();
                Toast.makeText(getApplicationContext(), "Archivo ProductosIG.csv Generado!", Toast.LENGTH_LONG).show();
                borrarDatosProductos();//vacia la tabla productos
                QuiebreActivity.this.finish();//genera el archivo y luego finaliza la ventana
            }
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Error al leer fichero en la memoria interna", Toast.LENGTH_LONG).show();
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
                    +Utilidades.CAMPO_LOCAL+","
                    +Utilidades.CAMPO_CADENA_P+","
                    +Utilidades.CAMPO_TIPO_NEGOCIO+","
                    +Utilidades.CAMPO_TIPO_SOPORTE+
                    " FROM "+Utilidades.TABLA_PRODUCTOS, null);
            // Toast.makeText(getApplicationContext(), "respuesta1"+cursor.toString(), Toast.LENGTH_LONG).show();
            while(cursor.moveToNext())//recorrido del cursor
                //escribe las lineas del cursor en lLineas y lo formatea con puntos y coma
                lLineas.add(String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s",
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8)));
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
                flujoSalida.write("ID;CODIGO_BARRA;DESCRIPCION;CANTIDAD;CATEGORIA;LOCAL;CADENA;TIPO_NEGOCIO;SOPORTE \r\n");//CABECERA
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

    //borra los datos de la tabla productos
    private void borrarDatosProductos() {
        try {
            SQLiteDatabase db = conn.getReadableDatabase();
            //SENTENCIA SQL
            db.delete(Utilidades.TABLA_PRODUCTOS, null, null );
            //Toast.makeText(getApplicationContext(), "Lectura Eliminadas correctamente", Toast.LENGTH_SHORT).show();
            db.close();
        }catch (Exception e){

            Toast.makeText(getApplicationContext(), "Error al eliminar los datos", Toast.LENGTH_SHORT).show();
        }
    }


    //***************************pruebas no se usa***************************************************
    //comprueba si hay registro en la tabla
    public void compruebaRegistroTabla(String nombreTabla, String campo) {
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("select COUNT(campo) from nombreTabla" , null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
            }
            cursor.close();
        }
    }
    //consultamos la tabla por medio del metodo generado en la clase ConexionSQLiteHelper
    public boolean consultaTabla(String tabla){
        SQLiteDatabase db = conn.getReadableDatabase();
        conn.consultaTabla(tabla, db);//consulta tabla(sql generico) query de la conexion
        return false;
    }



}
