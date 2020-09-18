package com.example.wmiltos.inveglobal_droid.Deposito;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.principal.login.LoginActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

public class LecturaDeposito extends AppCompatActivity {

    EditText campoScanning;
    TextView txLocal, txUbicacion, txCodManipulacion,nroLecturasL,tvScanning;
    ConexionSQLiteHelper conn;
    private Cursor fila;
    BottomNavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura_deposito);
        variables();
        recepcionDatosUbicacion();
        validacionCampoScanning();

    }

    private void variables() {
        conn = new ConexionSQLiteHelper(getApplicationContext(), "InveStock.sqlite", null, 1);
        txLocal = findViewById(R.id.txt_Local);
        txUbicacion = findViewById(R.id.txt_Ubicacion);
        txCodManipulacion = findViewById(R.id.txt_cod_manipulacion);
        tvScanning = findViewById(R.id.tv_scanning);//auxiliar
        campoScanning = findViewById(R.id.et_scanningLectura);

        //nroLecturasL = findViewById(R.id.tvNroLecturasL);

        navigation = findViewById(R.id.navigation_deposito);
        navigation.setOnNavigationItemSelectedListener(menuNavegacion);

    }


    private BottomNavigationView.OnNavigationItemSelectedListener menuNavegacion
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    dialogo();
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent2 = new Intent(LecturaDeposito.this, GenerarArchivoDeposito.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_notifications:
                    Intent intent3 = new Intent(LecturaDeposito.this, LimpiarDepo.class);
                    startActivity(intent3);
                    return true;
            }
            return false;
        }
    };


    public void dialogo(){
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(LecturaDeposito.this);
        dialogo.setMessage("Desea salir del sistema?").setTitle("InveGlobal")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(LecturaDeposito.this, LoginActivity.class);
                startActivity(intent);
                LecturaDeposito.this.finish();//finaliza la ventana

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

    public void validacionCampoScanning (){
        campoScanning.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }
            //Pasar a la sgte ventana automaticamente si el numero de caracteres son las sgtes
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                comprobarCodigo();
                //validacion de salto
                if(     (s.length() == 15) && (tvScanning.getText().length()!=0) ||//que tenga 15 dig y q el campo sea indistinto a 0
                        (s.length() == 14) && (tvScanning.getText().length()!=0) ||//que tenga 14 dig y q el campo sea indistinto a 0
                        (s.length() == 13) && (tvScanning.getText().length()!=0) ||
                        (s.length() == 12) && (tvScanning.getText().length()!=0) ||
                        (s.length() == 11) && (tvScanning.getText().length()!=0) ||
                        (s.length() == 10) && (tvScanning.getText().length()!=0) ||
                        (s.length() == 9)  && (tvScanning.getText().length()!=0) ||
                        (s.length() == 8)  && (tvScanning.getText().length()!=0) ||
                        (s.length() == 7)  && (tvScanning.getText().length()!=0) ||
                        (s.length() == 6)  && (tvScanning.getText().length()!=0))
                {
                    envioDatosLectura();//si esta correcto, envia los datos a Lectura
                }else if (s.length()==13){
                    envioDatosLectura();//si esta correcto, envia los datos a Lec
                }
            }
        });
    }

    public void comprobarCodigo (){
        try {
            ConexionSQLiteHelper admin = new ConexionSQLiteHelper(this, "InveStock.sqlite", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();
            String str = campoScanning.getText().toString();
            String pri_digito=str.substring(0,1);//extrae el 1er digito

            if (pri_digito.equals("0")) {//si el 1er digito es 0
                String scanning = str.substring(1);//lo eliminamos y realizamos la consulta
                fila = db.rawQuery("SELECT ltrim(" + Utilidades.CAMPO_COD_BARRA_DEP + ", '0') FROM " + Utilidades.TABLA_DEPOSITO +
                        " WHERE " + Utilidades.CAMPO_COD_BARRA_DEP + "='" + scanning + "'", null);
                fila.moveToFirst();
                tvScanning.setText(fila.getString(0));//muestra los campos en la tx oculta

            }else {//sino realizamos la consulta normal
                fila = db.rawQuery("SELECT ltrim(" + Utilidades.CAMPO_COD_BARRA_DEP + ", '0') FROM " + Utilidades.TABLA_DEPOSITO +
                        " WHERE " + Utilidades.CAMPO_SCANNING + "='" + str + "'", null);
                fila.moveToFirst();
                tvScanning.setText(fila.getString(0));//muestra los campos en la tx oculta
            }
        }catch (Exception e){
            tvScanning.setText("");
        }
    }

    private void recepcionDatosUbicacion() {
        //recibe los datos cargado en los campos de UbicacionActivity
        Bundle miBundle = this.getIntent().getExtras();
        if (miBundle != null) {

            String localDepo = miBundle.getString("msjDescLocal");
            txLocal.setText(localDepo);
            String ubicacionDepo = miBundle.getString("msjUbicacion");
            txUbicacion.setText(ubicacionDepo);
            String codManipulacion = miBundle.getString("msjCodManipulacion");
            txCodManipulacion.setText(codManipulacion);


        }
    }


    public void onClickBuscar(View view) {
        String campo =  campoScanning.getText().toString();

        if (!campo.isEmpty()) {//si el campo esta vacio, mostrar mensaje
                 envioDatosLectura();//para conteo normal
        } else {
            Toast.makeText(getApplicationContext(), "El campo esta vacio", Toast.LENGTH_SHORT).show();
        }
    }

    private void envioDatosLectura() {
        Intent miIntent = null;

        String msjScanning = campoScanning.getText().toString();
        String msjL_local = txLocal.getText().toString();
        String msjL_Ubicacion = txUbicacion.getText().toString();
        String msjL_Manipulacion = txCodManipulacion.getText().toString();


        miIntent = new Intent(LecturaDeposito.this, StockDeposito.class);
        Bundle miBundle = new Bundle();
        miBundle.putString("msjScanning", campoScanning.getText().toString());
        miBundle.putString("msjL_local", txLocal.getText().toString());//E1
        miBundle.putString("msjL_Ubicacion", txUbicacion.getText().toString());
        miBundle.putString("msjL_Manipulacion", txCodManipulacion.getText().toString());

        miIntent.putExtras(miBundle);
        startActivity(miIntent);
        campoScanning.setText("");//limpia el campo

    }


}
