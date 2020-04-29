package com.example.wmiltos.inveglobal_droid.iTrack;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Productos;
import com.example.wmiltos.inveglobal_droid.principal.login.LoginValidacionActivity;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.Limpiar2Activity;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.RedActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ImportarCSVActivity extends AppCompatActivity {

    ConexionSQLiteHelper conn;
    List<Productos> listaProducto = new ArrayList<>();
    ProgressBar progressBarHorizontal, progressBarCircular;
    Button btnImportar, btnVolver;
    TextView parametro;
    TextView resultado;

    AsyncTask_load tarea;
    private ProgressDialog pDialog;
    //private MiTareaAsincrona tarea1;
    private ProgressBar pbarProgreso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importar_csv);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "InveStock.sqlite", null, 1);
        progressBarHorizontal = findViewById(R.id.progressbar_Horizontal);
        progressBarHorizontal.setProgress(0);

        pbarProgreso = findViewById(R.id.progressbar_Horizontal);

        btnVolver = findViewById(R.id.btn_volverL);
        btnImportar = findViewById(R.id.btn_importar);

       /* btnImportar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tarea1 = new MiTareaAsincrona();
                tarea1.execute();
                cargarTablaProducto();
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

   /* private class MiTareaAsincrona extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            for(int i=1; i<=10; i++) {
               // tareaLarga();

                publishProgress(i*10);

                if(isCancelled())
                    break;
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            pbarProgreso.setProgress(progreso);
        }

        @Override
        protected void onPreExecute() {
            pbarProgreso.setMax(100);
            pbarProgreso.setProgress(0);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
                Toast.makeText(ImportarCSVActivity.this, "Tarea finalizada!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(ImportarCSVActivity.this, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
        }
    }*/

    //boton fisico atraz
    @Override
    public void onBackPressed() {
        ImportarCSVActivity.this.finish();
    }

    //barra de progreso
    public class AsyncTask_load extends AsyncTask<Void, Integer, Void> {
        int progreso;

        @Override
        protected void onPreExecute() {
            Toast.makeText(ImportarCSVActivity.this,"Aguarde un momento...",Toast.LENGTH_LONG).show();
            progreso = 0;
            progressBarHorizontal.setVisibility(View.VISIBLE);
            //bloquear los botones y cambiar de color
            btnImportar.setEnabled(false);
            btnImportar.setTextColor(Color.parseColor("#9E9E9E"));
            btnVolver.setEnabled(false);
            btnVolver.setTextColor(Color.parseColor("#9E9E9E"));
        }

        @Override //mientras ejecuta la tarea asincrona, la tarea de 2do plano
        protected Void doInBackground(Void... params) {
            while (progreso < 100){
                progreso++;
                publishProgress(progreso);//publicar progreso
                SystemClock.sleep(100);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {//muestra el progreso
            progressBarHorizontal.setProgress(values[0]);
            if (progreso == 85){
                cargarTablaProducto();
            }
        }

        @Override
        protected void onPostExecute(Void result) {//termino del proceso

            if(progreso == 100) {
                Toast.makeText(ImportarCSVActivity.this, "Importación Completada.!", Toast.LENGTH_LONG).show();
                btnImportar.setClickable(true);
                btnVolver.setClickable(true);
                progressBarHorizontal.setVisibility(View.INVISIBLE);

                //habilitar botones y cambiar de color
                btnImportar.setEnabled(true);
                btnImportar.setTextColor(Color.parseColor("#F44336"));
                btnVolver.setEnabled(true);
                btnVolver.setTextColor(Color.parseColor("#389136"));
            }
        }
    }


    public void onClick2(View view) {
        switch (view.getId()){
            case R.id.btn_volverL:ImportarCSVActivity.this.finish();
                break;
            case R.id.btn_importar: new AsyncTask_load().execute();//tarea asincrona
                break;
        }
    }

    //DE LA CARGA DE LAS TABLAS
    public void cargarTablaProducto(){
        try {
            SQLiteDatabase db = conn.getReadableDatabase();
            Cursor _producto = db.rawQuery(Utilidades.CONSULTA_TABLA_PRODUCTOS, null);//consulta si existe, luego
            if (_producto.getCount() < 1)   //2do comprueba si la tabla esta cargada
            {//sino importar datos del csv
                //cargar las tablas directo
                importarCSV_listado();
            } else {
                //Toast.makeText(getApplicationContext(), "No se pudo cargar las tablas", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    //LISTADO
    public void importarCSV_listado() {
        File carpeta = new File(Environment.getExternalStorageDirectory() + "/Download");
        String archivoAgenda = carpeta.toString() + "/" + "lista.csv";
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
                    arreglo = cadena.split(";");

                    SQLiteDatabase db = conn.getWritableDatabase();
                    ContentValues registro = new ContentValues();

                    registro.put("CODIGO_BARRA", arreglo[0]);
                    registro.put("DESCRIPCION", arreglo[1]);
                    registro.put("CANTIDAD", arreglo[2]);


                    listaProducto.add(
                            new Productos(
                                    arreglo[0],
                                    arreglo[1],
                                    arreglo[2]
                            )
                    );
                    // los inserto en la base de datos
                    db.insert(Utilidades.TABLA_PRODUCTOS, "0", registro);//INSERTA A LA TABLA
                    db.close();
                }
                Toast.makeText(getApplicationContext(), "CSV lista - Importada correctamente", Toast.LENGTH_SHORT).show();
            } catch(Exception e) {
                Toast.makeText(this, "No se encontró la planilla en la carpeta", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //Pruebas---------------------
    public void progreso(){

        try {
         /*   new Thread(new Runnable() {
                public void run() {
                    progressBarHorizontal.post(new Runnable() {
                        public void run() {
                            progressBarHorizontal.setProgress(0);
                        }
                    });
                    for(int i=1; i<=100; i++) {
                       cargarTablaProducto();
                        progressBarHorizontal.post(new Runnable() {
                            public void run() {
                                progressBarHorizontal.incrementProgressBy(100);
                            }
                        });
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ImportarCSVActivity.this, "Tarea finalizada!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();*/



            tarea = new AsyncTask_load();
            tarea.execute();
            //new AsyncTask_load().execute();  // cargarTablaProducto();//tarea asincrona
            //Toast.makeText(getApplicationContext(), "progreso", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void tareaLarga() {
        try {
            Thread.sleep(1000);
            Toast.makeText(getApplicationContext(), "ejecutando tarea larga", Toast.LENGTH_SHORT).show();
        } catch(InterruptedException e) {

        }
    }

}
