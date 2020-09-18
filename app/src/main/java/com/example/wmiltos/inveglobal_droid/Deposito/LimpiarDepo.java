package com.example.wmiltos.inveglobal_droid.Deposito;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.principal.login.LoginValidacionActivity;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.Limpiar2Activity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.io.File;

public class LimpiarDepo extends AppCompatActivity {

    Button limpiarDatos;
    TextView cantidadRegistros, parameters;
    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limpiar_depo);

        conn= new ConexionSQLiteHelper(getApplicationContext(),"InveStock.sqlite",null,1);
        cantidadRegistros= findViewById(R.id.tvCampoCantidadRegistros);
        limpiarDatos = findViewById(R.id.btn_limpiar);
        parameters= findViewById(R.id.tvParametro);
        sumaRegistrosSQL();


    }

    public void sumaRegistrosSQL(){
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM "+Utilidades.TABLA_DEPOSITO,null);
        cantidadRegistros.setText("Cantidad de Lecturas:   "+c.getCount());


        db.close();
    }

    private void mensajeDialogo() {
        try {
            //1 -creamos dialogo
            android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(LimpiarDepo.this);
            dialogo.setMessage("Esta Seguro de borrar los datos?").setTitle("Datos de lecturas")
                    .setIcon(R.drawable.alerta);
            //2 -evento click ok
            dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    limpiarLecturas();
                }
            });
            dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LimpiarDepo.this.finish();
                }
            });
            android.support.v7.app.AlertDialog alertDialog = dialogo.create();
            alertDialog.show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error de dialogo limpiar",Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarLecturas() {
        borrarDatosLecturaSQL();
        eliminarArchivo();
        LimpiarDepo.this.finish();
    }



    public void onClick2(View view) {
        switch (view.getId()){
            case R.id.btn_volverL:LimpiarDepo.this.finish();
                break;
            case R.id.btn_limpiar:mensajeDialogo();
                break;
        }
    }

    private void borrarDatosLecturaSQL() {
        try {
            SQLiteDatabase db = conn.getReadableDatabase();
            //SENTENCIA SQL
            db.delete(Utilidades.TABLA_DEPOSITO, null    , null);
            Toast.makeText(getApplicationContext(), "Lectura Eliminadas correctamente", Toast.LENGTH_SHORT).show();
            db.close();
        }catch (Exception e){

            Toast.makeText(getApplicationContext(), "Error al eliminar los datos", Toast.LENGTH_SHORT).show();
        }
    }

    //elimina segun la ubicacion del archivo
    private void eliminarArchivo() {
        File archivo, archivoSdcard = null;

        String direccionExterna = "sdcard/lecturaDeposito.csv";
        String direccionSdcard = "sdcard/sdcard/lecturaDeposito.csv";

        try{
            archivo = new File(direccionExterna);
            archivoSdcard = new File(direccionSdcard);
            //eliminamos ambos archivos de las dos ubicaciones
            boolean estatus = archivo.delete();
            boolean estatus2 = archivoSdcard.delete();
            if (!estatus && estatus2) {
                Toast.makeText(getApplicationContext(), "No se encotro documento", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Documento eliminado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
