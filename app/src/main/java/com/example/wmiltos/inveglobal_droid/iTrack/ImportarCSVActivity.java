package com.example.wmiltos.inveglobal_droid.iTrack;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Productos;
import com.example.wmiltos.inveglobal_droid.principal.login.LoginValidacionActivity;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.Limpiar2Activity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ImportarCSVActivity extends AppCompatActivity {

    ConexionSQLiteHelper conn;
    List<Productos> listaProducto = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importar_csv);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "InveStock.sqlite", null, 1);
    }

    //boton fisico atraz
    @Override
    public void onBackPressed() {
        ImportarCSVActivity.this.finish();
    }

    public void onClick2(View view) {
        switch (view.getId()){
            case R.id.btn_volverL: ImportarCSVActivity.this.finish();
                break;
            case R.id.btn_importar:cargarTablaProducto();
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
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
