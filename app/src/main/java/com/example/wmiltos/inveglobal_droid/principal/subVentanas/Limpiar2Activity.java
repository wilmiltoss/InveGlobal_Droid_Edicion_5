package com.example.wmiltos.inveglobal_droid.principal.subVentanas;

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
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

public class Limpiar2Activity extends AppCompatActivity {

    Button limpiarDatos;
    TextView cantidadRegistros, parameters;
    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limpiar2);
        conn= new ConexionSQLiteHelper(getApplicationContext(),"InveStock.sqlite",null,1);
        cantidadRegistros= findViewById(R.id.tvCampoCantidadRegistros);
        limpiarDatos = findViewById(R.id.btn_limpiar);
        parameters= findViewById(R.id.tvParametro);
        sumaRegistrosSQL();
    }

    public void sumaRegistrosSQL(){
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+ Utilidades.TABLA_LECTURAS,null);
        cantidadRegistros.setText("Cantidad de Lecturas:   "+c.getCount());
        db.close();
    }

    private void mensajeDialogo() {
        try {
            //1 -creamos dialogo
            android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(Limpiar2Activity.this);
            dialogo.setMessage("Esta Seguro de borrar los datos?").setTitle("Datos de lecturas")
                    .setIcon(R.drawable.alerta);
            //2 -evento click ok
            dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    pasarAmenuLogin();
                }
            });
            dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Limpiar2Activity.this.finish();
                }
            });
            android.support.v7.app.AlertDialog alertDialog = dialogo.create();
            alertDialog.show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error de dialogo limpiar",Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick2(View view) {
        switch (view.getId()){
            case R.id.btn_volverL:Limpiar2Activity.this.finish();
                break;
            case R.id.btn_limpiar:mensajeDialogo();
                break;
        }
    }

    private void pasarAmenuLogin() {
        Intent intent = new Intent(Limpiar2Activity.this, LoginValidacionActivity.class);
        startActivity(intent);
        Limpiar2Activity.this.finish();
    }
}
