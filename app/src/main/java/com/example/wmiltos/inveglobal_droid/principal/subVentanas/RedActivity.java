package com.example.wmiltos.inveglobal_droid.principal.subVentanas;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Configuraciones;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Inventario_Soporte;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Lecturas;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RedActivity extends AppCompatActivity {

    TextView parametro;
    TextView resultado;
    ConexionSQLiteHelper conn;
    ProgressBar progressBarHorizontal, progressBarCircular;
    Button enviarDatos, volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red);

        conn= new ConexionSQLiteHelper(getApplicationContext(),"InveStock.sqlite",null,1);
        parametro =  findViewById(R.id.etParametro);
        resultado =  findViewById(R.id.tvResultado);

        progressBarHorizontal = findViewById(R.id.progressbar_Horizontal);
        progressBarHorizontal.setProgress(0);
        progressBarCircular = findViewById(R.id.progressBarCircular);
        progressBarCircular.setProgress(0);

        //de los botones
        enviarDatos = findViewById(R.id.btn_enviar_datos);
        volver = findViewById(R.id.btn_volver);


       /* enviarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo();
                new AsyncTask_load().execute();//tarea asincrona
                enviarDatos.setClickable(false);//mientras se realiza la tarea asincrona bloquea el boton
                volver.setClickable(false);
            }
        });*/
    }

    //barra de progreso
    public class AsyncTask_load extends AsyncTask<Void, Integer, Void> {
        int progreso;

        @Override
        protected void onPreExecute() {//antes de ejecutar la tarea asincrona
            Toast.makeText(RedActivity.this,"Envio de datos en Progreso",Toast.LENGTH_LONG).show();
            progreso = 0;
            progressBarHorizontal.setVisibility(View.VISIBLE);

            //bloquear los botones y cambiar de color
            enviarDatos.setEnabled(false);
            enviarDatos.setTextColor(Color.parseColor("#9E9E9E"));
            volver.setEnabled(false);
            volver.setTextColor(Color.parseColor("#9E9E9E"));

            //metodos para migrar datos al servidor
            transfer_Inventario_Soporte_LiteServer();//actualiza el avance del inventario
            delete_Entradas_Colectoras_LiteServer();//elimina el contenido de la tabla ENTRADAS_COLECTORAS, limpia
            transfer_Entradas_Colectoras_LiteServer();//tranfiere los datos a la tabal ENTRADAS_COLECTORAS que es temporal hasta la ejecucion del pr
            ejecutarPrTomarEntradas(); //ultimo paso ejecutar procedimiento
        }
        @Override //mientras ejecuta la tarea asincrona, la tarea de 2do plano
        protected Void doInBackground(Void... params) {
            while (progreso < 100){
                progreso++;
                publishProgress(progreso);//publicar progreso
                SystemClock.sleep(300);
            }
            return null;
        }
            @Override
            protected void onProgressUpdate(Integer... values) {//muestra el progreso

                progressBarHorizontal.setProgress(values[0]);
                progressBarHorizontal.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(Void result) {//termino del proceso

            Toast.makeText(RedActivity.this,"Envio de datos Completado.!",Toast.LENGTH_LONG).show();
            enviarDatos.setClickable(true);
            volver.setClickable(true);
            progressBarHorizontal.setVisibility(View.INVISIBLE);

            //habilitar botones y cambiar de color
            enviarDatos.setEnabled(true);
            enviarDatos.setTextColor(Color.parseColor("#F44336"));
            volver.setEnabled(true);
            volver.setTextColor(Color.parseColor("#389136"));
        }
    }

    public void onClickEnviar(View view) {
       dialogo();
    }

    public void dialogo(){
        try {
            android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(RedActivity.this);
            dialogo.setMessage("Confirmar Envio?").setTitle("Envio Datos")
                    .setIcon(R.drawable.alert_dialogo);
            //2 -evento click ok
            dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(),"Por favor aguarde....",Toast.LENGTH_LONG).show();
                    try {
                        //comprobar conexion para tranferir datos al servidor
                        Statement consulta = conexionBD().createStatement();
                        ResultSet rsc = consulta.executeQuery("SELECT * FROM SOPORTES");
                        rsc.next();
                        Log.i("conexion", rsc.toString());
                        Toast.makeText(getApplicationContext(),"Equipo conectado a la red",Toast.LENGTH_LONG).show();

                        new AsyncTask_load().execute();//tarea asincrona
                        enviarDatos.setClickable(false);//mientras se realiza la tarea asincrona bloquea el boton
                        volver.setClickable(false);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),"Compruebe su conexión con la red",Toast.LENGTH_LONG).show();
                    }
                    //Toast.makeText(getApplicationContext(),"SIN CONEXION CON EL SERVIDOR",Toast.LENGTH_LONG).show();


                }
            });

            dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            android.support.v7.app.AlertDialog alertDialog = dialogo.create();
            alertDialog.show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Envio fallido", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_volver:
                RedActivity.this.finish();
                break;
            case R.id.btn_enviar_datos:dialogo();
                break;
        }
    }

    //Conexion a la BD SqlServer
    public Connection conexionBD () {
        Connection conexion = null;
        try {
            //politica de conexion
            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:jtds:sqlserver://RETSVRDB10/INVESTOCK_GENERAL;user=stock;password=12345;");
            //Toast.makeText(getApplicationContext(),"Conexion exitosa",Toast.LENGTH_LONG).show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"SIN CONEXION CON EL SERVIDOR",Toast.LENGTH_LONG).show();
        }
        return conexion;
    }

    //Envio de los datos de INVENTARIO_SOPORTE a la tabla del servidor
    public void actualizarInventarioSoporte(int idInventarioSoporte, int conteo1, int conteo2, int conteo3 ){
        try {
            Statement consulta = conexionBD().createStatement();
            ResultSet rsc = consulta.executeQuery("UPDATE INVENTARIO_SOPORTE SET CONTEO1 = "+ conteo1 +
                                                                                   ", CONTEO2 = "+ conteo2 +
                                                                                   ", CONTEO3 = "+ conteo3 +
                                                       " WHERE ID_INVENTARIO_SOPORTES = "+idInventarioSoporte);
            rsc.next();
                Toast.makeText(getApplicationContext(),"actualizacion exitosa"+idInventarioSoporte,Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public boolean transfer_Inventario_Soporte_LiteServer (){
        try{
            SQLiteDatabase db = conn.getReadableDatabase();
            try{
                Cursor cursor = db.rawQuery("SELECT * FROM INVENTARIO_SOPORTE", null);
                Inventario_Soporte inventario_soporte = null;

                while (cursor.moveToNext()) {
                    inventario_soporte = new Inventario_Soporte();
                    inventario_soporte.setID_INVENTARIO_SOPORTE(cursor.getInt(0));
                    inventario_soporte.setID_INVENTARIO(cursor.getInt(1));
                    inventario_soporte.setID_CLAVE(cursor.getInt(2));
                    inventario_soporte.setCONTEO1(cursor.getInt(7));
                    inventario_soporte.setCONTEO2(cursor.getInt(8));
                    inventario_soporte.setCONTEO3(cursor.getInt(9));
                    //mostrar en el log para prueba
                    Log.i("id_inventario_soporte", inventario_soporte.getID_INVENTARIO_SOPORTE().toString());
                    Log.i("id_inventario", inventario_soporte.getID_INVENTARIO().toString());
                    Log.i("id_clave", inventario_soporte.getID_CLAVE().toString());
                    Log.i("conteo1", inventario_soporte.getCONTEO1().toString());
                    Log.i("conteo2", inventario_soporte.getCONTEO2().toString());
                    Log.i("conteo3", inventario_soporte.getCONTEO3().toString());
                    //almacenar en variables el recorrido de la consulta
                    int idInvSoport = Integer.parseInt(inventario_soporte.getID_INVENTARIO_SOPORTE().toString());
                    int idInve       = Integer.parseInt(inventario_soporte.getID_INVENTARIO().toString());
                    int idClave       = Integer.parseInt(inventario_soporte.getID_CLAVE().toString());
                    int cont1       = Integer.parseInt(inventario_soporte.getCONTEO1().toString());
                    int cont2       = Integer.parseInt(inventario_soporte.getCONTEO2().toString());
                    int cont3       = Integer.parseInt(inventario_soporte.getCONTEO3().toString());
                    //usar los resultados de las variables en el metodo enviarDatos para ir actualizando cada fila
                    actualizarInventarioSoporte(idInvSoport,cont1,cont2,cont3);
                }
                Toast.makeText(getApplicationContext(), "Transfiriendo datos de configuración....", Toast.LENGTH_LONG).show();
                cursor.close();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Sin registros", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al mostrar la cantidad", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    //Envio de los datos de LECTURA a la tabla del servidor ENTRADAS_COLECTORAS
    public void insertarEntradasColectoras(int idInventario, int idLocacion, int nroConteo, int idSoporte, int nroSoporte, int idLetraSoport,
                                           String scanning, int nivel, int metro, int cantidad, int usuario){
        try {
            String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;//nombre del equipo colector
            Statement consulta = conexionBD().createStatement();
            ResultSet rsc = consulta.executeQuery("INSERT INTO ENTRADAS_COLECTORAS (ID_INVENTARIO, ID_LOCACION, NRO_CONTEO, ID_SOPORTE, NRO_SOPORTE, " +
                                                                            "ID_LETRA_SOPORTE, SCANNING, NIVEL, METRO, CANTIDAD, COLECTORA, ID_USUARIO )" +
                                                        " VALUES (" +idInventario+ " ,"+idLocacion+" ,"+nroConteo+" ,"+idSoporte+" ,"+nroSoporte+" ,"+idLetraSoport+
                                                                 " ,"+scanning+" ,"+nivel+","+metro+" ,"+cantidad+" ,'"+deviceName+"' ,"+usuario+ ") ");
            rsc.next();
            //Toast.makeText(getApplicationContext(),"ENTRADAS_COLECTORAS actualizada"+idInventario,Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    //Transfiere los datos de la tabla Lectura Sqlite a la tabla Entradas_Colectoras SqlServer
    public void transfer_Entradas_Colectoras_LiteServer (){
        try{
            SQLiteDatabase db = conn.getReadableDatabase();
            try{
                Cursor cursor = db.rawQuery("SELECT * FROM "+Utilidades.TABLA_LECTURAS, null);
                Lecturas lecturas = null; // Lecturas es la clase creada q contiene los campos de la tabla get set

                while (cursor.moveToNext()) {
                    lecturas = new Lecturas();//llamamos a los metodos de la clase Lectura
                    lecturas.setId_locacion(cursor.getInt(0));
                    lecturas.setNro_conteo(cursor.getInt(1));
                    lecturas.setId_soporte(cursor.getInt(2));
                    lecturas.setNro_soporte(cursor.getInt(3));
                    lecturas.setId_letra_soporte(cursor.getInt(4));
                    lecturas.setNivel(cursor.getInt(5));
                    lecturas.setMetro(cursor.getInt(6));
                    lecturas.setScanning(cursor.getString(7));
                    lecturas.setCantidad(cursor.getInt(8));
                    lecturas.setId_usuario(cursor.getInt(9));
                    lecturas.setId_inventario(cursor.getInt(10));


                    //mostrar en el log para prueba
                    Log.i("id_locacion",  lecturas.getId_locacion().toString());
                    Log.i("nro_conteo", lecturas.getNro_conteo().toString());
                    Log.i("id_soporte", lecturas.getId_soporte().toString());
                    Log.i("nro_soporte", lecturas.getNro_soporte().toString());
                    Log.i("id_letra_soporte", lecturas.getId_letra_soporte().toString());
                    Log.i("nivel", lecturas.getNivel().toString());
                    Log.i("metro", lecturas.getMetro().toString());
                    Log.i("scanning", lecturas.getScanning());
                    Log.i("cantidad", lecturas.getCantidad().toString());
                    Log.i("id_usuario", lecturas.getId_usuario().toString());
                    Log.i("id_inventario", lecturas.getId_inventario().toString());

                    //almacenar en variables el recorrido de la consulta
                    int idLocacion   = Integer.parseInt(lecturas.getId_locacion().toString());
                    int nroConteo    = Integer.parseInt(lecturas.getNro_conteo().toString());
                    int idSoporte    = Integer.parseInt(lecturas.getId_soporte().toString());
                    int nroSoporte   = Integer.parseInt(lecturas.getNro_soporte().toString());
                    int idLetraSopor = Integer.parseInt(lecturas.getId_letra_soporte().toString());
                    int nivel        = Integer.parseInt(lecturas.getNivel().toString());
                    int metro        = Integer.parseInt(lecturas.getMetro().toString());
                    String scanning  = lecturas.getScanning();
                    int cantidad     = Integer.parseInt(lecturas.getCantidad().toString());
                    int idUsuario    = Integer.parseInt(lecturas.getId_usuario().toString());
                    int idInventario = Integer.parseInt(lecturas.getId_inventario().toString());

                    //usar los resultados de las variables en el metodo enviarDatos para ir actualizando cada fila
                    insertarEntradasColectoras(idInventario,idLocacion,nroConteo,idSoporte,nroSoporte,idLetraSopor,
                                                         scanning,nivel,metro,cantidad,idUsuario);
                }
                Toast.makeText(getApplicationContext(), "Actualizando Entradas colectoras... ", Toast.LENGTH_LONG).show();
                cursor.close();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Sin registros", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en la consulta al Servidor SQL", Toast.LENGTH_LONG).show();
        }
    }


    public void eliminarEntradasColectoras(int idInventario, int usuario){
        try {
            String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;//nombre del equipo colector
            Statement consulta = conexionBD().createStatement();
            ResultSet rsc = consulta.executeQuery("DELETE FROM ENTRADAS_COLECTORAS WHERE ID_INVENTARIO = "+idInventario+
                                                                                        "AND COLECTORA = '"+deviceName+
                                                                                        "' OR ID_USUARIO = "+usuario);
            rsc.next();
            //Toast.makeText(getApplicationContext(),"ENTRADAS_COLECTORAS eliminadas"+idInventario,Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    //Elimina los datos de la tabla Entradas_Colectoras de SqlServer
    public void delete_Entradas_Colectoras_LiteServer (){
        try{
            SQLiteDatabase db = conn.getReadableDatabase();
            try{
                Cursor cursor = db.rawQuery("SELECT * FROM "+Utilidades.TABLA_LECTURAS, null);
                Lecturas lecturas = null; // Lecturas es la clase creada q contiene los campos de la tabla get set

                if (cursor.moveToNext()) {
                    lecturas = new Lecturas();//llamamos a los metodos de la clase Lectura
                    lecturas.setId_locacion(cursor.getInt(0));
                    lecturas.setNro_conteo(cursor.getInt(1));
                    lecturas.setId_soporte(cursor.getInt(2));
                    lecturas.setNro_soporte(cursor.getInt(3));
                    lecturas.setId_letra_soporte(cursor.getInt(4));
                    lecturas.setNivel(cursor.getInt(5));
                    lecturas.setMetro(cursor.getInt(6));
                    lecturas.setScanning(cursor.getString(7));
                    lecturas.setCantidad(cursor.getInt(8));
                    lecturas.setId_usuario(cursor.getInt(9));
                    lecturas.setId_inventario(cursor.getInt(10));


                    //mostrar en el log para prueba
                    Log.i("id_usuario", lecturas.getId_usuario().toString());
                    Log.i("id_inventario", lecturas.getId_inventario().toString());

                    //almacenar en variables el recorrido de la consulta
                    int idUsuario    = Integer.parseInt(lecturas.getId_usuario().toString());
                    int idInventario = Integer.parseInt(lecturas.getId_inventario().toString());

                    //usar los resultados de las variables en el metodo eliminarEntradasColectoras para eliminar tabla
                    eliminarEntradasColectoras(idInventario,idUsuario);
                    Toast.makeText(getApplicationContext(), "Tranfiriendo lecturas...."+idInventario, Toast.LENGTH_LONG).show();
                }
                cursor.close();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Sin registros", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en la consulta al Servidor SQL", Toast.LENGTH_LONG).show();
        }
    }

    public void prTomarEntradas(int idInventario){
        try {
            String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;//nombre del equipo colector
            Byte prueba = 0;
            Statement consulta = conexionBD().createStatement();
            ResultSet rsc = consulta.executeQuery("EXECUTE SP_TOMAR_ENTRADAS "+idInventario+", '"+deviceName+ "', "+prueba);
            rsc.next();
            //Toast.makeText(getApplicationContext(),"SP_TOMAR_ENTRADAS ejecutada "+idInventario,Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void ejecutarPrTomarEntradas(){
        String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;//nombre del equipo colector

        try{
            SQLiteDatabase db = conn.getReadableDatabase();
            try{
                Cursor cursor = db.rawQuery("SELECT * FROM "+Utilidades.TABLA_CONFIGURACIONES, null);
                Configuraciones configuraciones = null; // Configuraciones es la clase creada q contiene los campos de la tabla get set

                if (cursor.moveToNext()) {
                    configuraciones = new Configuraciones();//llamamos a los metodos de la clase Lectura
                    configuraciones.setCantidad_maxima_conteo(cursor.getInt(0));
                    configuraciones.setCantidad_conteo_decimal(cursor.getInt(1));
                    configuraciones.setId_inventario(cursor.getInt(2));
                    configuraciones.setIp_sql_server(cursor.getInt(3));

                    //mostrar en el log para prueba
                    Log.i("id_inventario", configuraciones.getId_inventario().toString());
                    Log.i("ip_sql_server", configuraciones.getIp_sql_server().toString());

                    //almacenar en variables el recorrido de la consulta
                    int idInventario    = Integer.parseInt(configuraciones.getId_inventario().toString());

                    //cargamos el idInventario
                    prTomarEntradas(idInventario);
                   Toast.makeText(getApplicationContext(), "Guardando Datos....."+idInventario, Toast.LENGTH_LONG).show();
                }
                cursor.close();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Sin registros", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en la consulta al Servidor SQL", Toast.LENGTH_LONG).show();
        }

    }
    public void procesos(){
        if (transfer_Inventario_Soporte_LiteServer()==true){
            Toast.makeText(getApplicationContext(), "Transfiriendo Configuraciones", Toast.LENGTH_LONG).show();
        }
    }

}
