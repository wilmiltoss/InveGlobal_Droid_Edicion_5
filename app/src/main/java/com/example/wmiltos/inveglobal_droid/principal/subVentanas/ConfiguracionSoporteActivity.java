package com.example.wmiltos.inveglobal_droid.principal.subVentanas;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Configuraciones;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Inventario_Soporte;
import com.example.wmiltos.inveglobal_droid.principal.login.LoginActivity;
import com.example.wmiltos.inveglobal_droid.principal.ventanas.UbicacionActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.sql.ResultSet;
import java.sql.Statement;

public class ConfiguracionSoporteActivity extends AppCompatActivity {

    EditText codigoSoporte;
    private Button btnAceptar2;
    ConexionSQLiteHelper conn;
    //tv tabla INVENTARIO_SOPORTE
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7, tvUsuario,tvConteo1,tvConteo2,tvConteo3,claveArreglada, idInvS, idInv, tvNroConteo;
    private Cursor fila;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_soporte);
        variables();
        recepcionDatosUsuario();
        validacionCampoCodSoporte();
    }

    public void variables(){
        conn = new ConexionSQLiteHelper(getApplicationContext(), "InveStock.sqlite", null, 1);
        //se almacenan en las variables tv la consulta
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        tv4 =  findViewById(R.id.tv_4);
        tv5 =  findViewById(R.id.tv_5);
        tv6 = findViewById(R.id.tv_6);
        tv7 =  findViewById(R.id.tv_7);
        tvUsuario = findViewById(R.id.tv_usurioC);
        tvNroConteo = findViewById(R.id.tv_NRO_CONTEO);

        tvConteo1 = findViewById(R.id.tv_conteo1);
        tvConteo2 = findViewById(R.id.tv_conteo2);
        tvConteo3 = findViewById(R.id.tv_conteo3);

        //btnAceptar2 = (Button)findViewById(R.id.btn_aceptar2);

        codigoSoporte = findViewById(R.id.et_codigoSoporte);
        claveArreglada =  findViewById(R.id.et_claveArreglada);

        idInvS = findViewById(R.id.tv_ID_INV_SOPORT);
        idInv = findViewById(R.id.tv_ID_INVENTARIO);
    }
    //Carga de datos segun clave scaneada
    //arregla el numero scanneado del codigo de barra
    public void selectScannerClave(){
        try {
            SQLiteDatabase db = conn.getWritableDatabase();
            String lecturaScanner = codigoSoporte.getText().toString();

            Cursor c = db.rawQuery(" SELECT " +Utilidades.CAMPO_ID_INVENTARIO_SOPORTE+","+
                                                    Utilidades.CAMPO_ID_CLAVE+","+
                                                    Utilidades.CAMPO_ID_SOPORTE_IS+","+
                                                    Utilidades.CAMPO_NRO_SOPORTE+","+
                                                    Utilidades.CAMPO_NRO_TIPO_SOPORTE+","+
                                                    Utilidades.CAMPO_DESCRIPCION_IS+","+
                                                    Utilidades.CAMPO_ID_LOCACIONES+","+
                                                    Utilidades.CAMPO_CONTEO_1+","+
                                                    Utilidades.CAMPO_CONTEO_2+","+
                                                    Utilidades.CAMPO_CONTEO_3+
                    " FROM " + Utilidades.TABLA_INVENTARIO_SOPORTE +" WHERE "
                            + Utilidades.CAMPO_ID_INVENTARIO_SOPORTE +"= substr('"+lecturaScanner+"',8,5) AND "
                            + Utilidades.CAMPO_ID_INVENTARIO_IS +"=substr('"+lecturaScanner+"',3,4)",null);
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {//recorremos todas las filas
                //Recorremos el cursor hasta que no haya más registros
                do {
                    //referenciar la columna en la tabla
                    String columna = c.getString(0);
                    String columna1 = c.getString(1);//clave inventario
                    String columna2 = c.getString(2);
                    String columna3 = c.getString(3);
                    String columna4 = c.getString(4);
                    String columna5 = c.getString(5);
                    String columna6 = c.getString(6);
                    String columna7 = c.getString(7);
                    String columna8 = c.getString(8);
                    String columna9 = c.getString(9);
                    //tx a mostrar
                    tv1.setText(columna);
                    tv2.setText(columna1);//clave inventario
                    tv3.setText(columna2);
                    tv4.setText(columna3);
                    tv5.setText(columna4);
                    tv6.setText(columna5);
                    tv7.setText(columna6);
                    tvConteo1.setText(columna7);
                    tvConteo2.setText(columna8);
                    tvConteo3.setText(columna9);

                } while(c.moveToNext());

            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error al consultar clave inventario soporte", Toast.LENGTH_SHORT).show();
        }
    }


    public void aceptarlo(View view) {
        validacionConteo();// # 1
    }


    //2-salto automatico de ventana
    public void validacionCampoCodSoporte (){
        codigoSoporte.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }
            //Pasar a la sgte ventana automaticamente si el numero de caracteres son las sgtes
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            //validacionConteo();
            }
        });
    }

    //valida si los conteos ya estan cerradas #2
    public void validacionConteo(){
        selectScannerClave();//cargamos los textview
        String conteo1 = tvConteo1.getText().toString();
        String conteo2 = tvConteo2.getText().toString();
        String conteo3 = tvConteo3.getText().toString();
        String descripcionConteo = tv6.getText().toString();

        //Evaluamos los textview; validacion de los estados de los conteos
        if (conteo1.equals("2")) {
            dialogoValidacionConteo();
        }else if (conteo2.equals("2")) {
            dialogoValidacionConteo();
        }else if (conteo3.equals("2")){
           // Toast.makeText(getApplicationContext(), "El conteo "+descripcionConteo+" esta en proceso", Toast.LENGTH_SHORT).show();
            envioMensaje_a_Ubicacion();
        }else if (conteo1.equals("1")){
            //Toast.makeText(getApplicationContext(), "El conteo "+descripcionConteo+" esta en proceso", Toast.LENGTH_SHORT).show();
            envioMensaje_a_Ubicacion();
        }else if (conteo2.equals("1")){
           // Toast.makeText(getApplicationContext(), "El conteo "+descripcionConteo+" esta en proceso", Toast.LENGTH_SHORT).show();
            envioMensaje_a_Ubicacion();
        }else if (conteo3.equals("1")){
            //Toast.makeText(getApplicationContext(), "El conteo "+descripcionConteo+" esta en proceso", Toast.LENGTH_SHORT).show();
            envioMensaje_a_Ubicacion();
        }
        else {
           // Toast.makeText(getApplicationContext(), "Abriendo conteo.", Toast.LENGTH_SHORT).show();
            envioMensaje_a_Ubicacion();
        }
    }

    //en caso de que el conteo este cerrado, abre este dialogo #3
    public void dialogoValidacionConteo(){
        String descripcionConteo = tv6.getText().toString();
        final int nroConteo = Integer.parseInt(tvNroConteo.getText().toString()) ;
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(ConfiguracionSoporteActivity.this);
        dialogo.setMessage("La ubicación "+descripcionConteo+" esta cerrada.. Desea abrirlo nuevamente..?").setTitle("AVISO")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                envioMensaje_a_Ubicacion();//si da ok; envia nuevamente los mensajes a ubicacion p/ abrir el conteo, pasa a la ventana ubicacion
                abrirConteo(nroConteo);

            }
        });
        dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConfiguracionSoporteActivity.this.finish();//sino finaliza la ventana
            }
        });
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    //capturar los datos de los campos EditText para envio a UbicacionActivity #4
    public void envioMensaje_a_Ubicacion (){
        selectScannerClave();
        if (tv1.getText().toString().isEmpty()){//si viene vacio el editext del scanner ubicacion
            dialogoBtnCerrar();//cierra
        }else {
            Intent miIntent = null;
            String msjtv1 = tv1.getText().toString();
            String msjtv2 = tv2.getText().toString();//CLAVE INVENTARIO
            String msjtv3 = tv3.getText().toString();
            String msjtv4 = tv4.getText().toString();
            String msjtv5 = tv5.getText().toString();
            String msjtv6 = tv6.getText().toString();
            String msjtv7 = tv7.getText().toString();
            String msjUsuConfig = tv6.getText().toString();
            String msjcont1 = tvConteo1.getText().toString();
            String msjcont2 = tvConteo2.getText().toString();
            String msjcont3 = tvConteo3.getText().toString();

            miIntent = new Intent(ConfiguracionSoporteActivity.this, UbicacionActivity.class);
            Bundle miBundle = new Bundle();
            miBundle.putString("msjtv1", tv1.getText().toString());
            miBundle.putString("msjtv2", tv2.getText().toString());//CLAVE INVENTARIO
            miBundle.putString("msjtv3", tv3.getText().toString());
            miBundle.putString("msjtv4", tv4.getText().toString());
            miBundle.putString("msjtv5", tv5.getText().toString());
            miBundle.putString("msjtv6", tv6.getText().toString());
            miBundle.putString("msjtv6", tv6.getText().toString());
            miBundle.putString("msjtv7", tv7.getText().toString());
            miBundle.putString("msjUsuConfig", tvUsuario.getText().toString());
            miBundle.putString("msjcont1", tvConteo1.getText().toString());
            miBundle.putString("msjcont2", tvConteo2.getText().toString());
            miBundle.putString("msjcont3", tvConteo3.getText().toString());

            miIntent.putExtras(miBundle);
            startActivity(miIntent);//abre la sgte ventana
            ConfiguracionSoporteActivity.this.finish();//finaliza la ventana anterior
            Toast.makeText(getApplicationContext(), "Descipción: " + msjtv6, Toast.LENGTH_SHORT).show();
        }
    }

    public void dialogoBtnCerrar(){
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(ConfiguracionSoporteActivity.this);
        dialogo.setMessage("Código no válido.!").setTitle("Ubicación")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Intent intent = new Intent(ConfiguracionSoporteActivity.this, LoginActivity.class);
                //startActivity(intent);
                ConfiguracionSoporteActivity.this.finish();//finaliza la ventana

            }
        });
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    private void recepcionDatosUsuario() {
        //recibe los datos cargado en los campos de Login
        Bundle miBundle = this.getIntent().getExtras();
        if (miBundle != null) {
            String usuario = miBundle.getString("msjUsuario");
            tvUsuario.setText(usuario);
            String campoConteotv = miBundle.getString("msjNroSoporteTv");
            tvNroConteo.setText(campoConteotv);
        }
    }


    //abrimos el conteo de acuerdo al parametro nro de CONTEO cargado
    public void actualizarReabrirConteo(String CONTEO){
        String descUbicacion =  tv6.getText().toString();
        SQLiteDatabase db = conn.getWritableDatabase();
        String claveSoporte = tv2.getText().toString();//ID_CLAVE
        try {
            //actualiza los campos a 2 solo si CONTEO es igual a 1
            String update = "UPDATE " + Utilidades.TABLA_INVENTARIO_SOPORTE + " SET " + CONTEO + " = 1 " +
                    "WHERE " + Utilidades.CAMPO_ID_CLAVE + "= " +claveSoporte + " AND "+ CONTEO + " = 2 ";
            db.execSQL(update);
            db.close();
            //"Id Registrado" es el mensaje que envia al insertar
            Toast.makeText(getApplicationContext(), "Abriendo nuevamente la ubicación "+ descUbicacion+ " del "+ CONTEO, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error al abrir el conteo" , Toast.LENGTH_SHORT).show();
        }
    }

    //invoca a actualizarConteoEnProceso de acuerdo al valor de texView conteo obtenido mas arriba
    public void abrirConteo(int nroConteo){
        //Actualiza los campos de acuerdo al nro de conteo
        try{
            if (nroConteo == 1){
                actualizarReabrirConteo("CONTEO1");//CONTEO1 envia a actualizarConteoCerrar(String CONTEO)
            }else if (nroConteo == 2){
                actualizarReabrirConteo("CONTEO2");
            }else if (nroConteo == 3){
                actualizarReabrirConteo("CONTEO3");
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al actualizar nro de conteo", Toast.LENGTH_LONG).show();
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++PRUEBAS NO SE USA+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void selectScanner() {
        try {
            String lecturaScanner = codigoSoporte.getText().toString();
            //mostrar digitos de los campos de la lectura scanner
            String digitoIdInvSoporte = lecturaScanner.substring(2,6);
            Log.i("dig ID_INV_SOPORT", digitoIdInvSoporte);
            idInvS.setText(digitoIdInvSoporte);

            String digitoIdInventario = lecturaScanner.substring(lecturaScanner.length() - 4);
            Log.i("dig ID_INVENTARIO", digitoIdInventario);
            idInv.setText(digitoIdInventario);

            //selectScannerClave(idInvS.getText().toString(),idInv.getText().toString());

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error al mostrar digitos", Toast.LENGTH_SHORT).show();
        }
    }

    public void prueba(View view) {

        selectScannerClave();//cargamos los texview

        String conteo1 = tvConteo1.getText().toString();
        String conteo2 = tvConteo2.getText().toString();
        String conteo3 = tvConteo3.getText().toString();
        if (conteo1.equals("2")) {
            Toast.makeText(getApplicationContext(), "ubicacion cerrada."+conteo1, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "ubicacion abierta."+conteo1, Toast.LENGTH_SHORT).show();
        }

        }


    public void datosScanner() {
            try {
                SQLiteDatabase db = conn.getWritableDatabase();
                String lecturaScanner = codigoSoporte.getText().toString();

                Cursor c = db.rawQuery(" SELECT " +Utilidades.CAMPO_ID_INVENTARIO_SOPORTE+","+
                        Utilidades.CAMPO_ID_CLAVE+","+
                        Utilidades.CAMPO_ID_SOPORTE_IS+","+
                        Utilidades.CAMPO_NRO_SOPORTE+","+
                        Utilidades.CAMPO_NRO_TIPO_SOPORTE+","+
                        Utilidades.CAMPO_DESCRIPCION_IS+","+
                        Utilidades.CAMPO_ID_LOCACIONES+","+
                        Utilidades.CAMPO_CONTEO_1+","+
                        Utilidades.CAMPO_CONTEO_2+","+
                        Utilidades.CAMPO_CONTEO_3+
                        " FROM " + Utilidades.TABLA_INVENTARIO_SOPORTE +" WHERE "
                        + Utilidades.CAMPO_ID_INVENTARIO_SOPORTE +"= substr('"+lecturaScanner+"',10,3) AND "
                        + Utilidades.CAMPO_ID_INVENTARIO_IS +"=substr('"+lecturaScanner+"',3,4)",null);
                //Nos aseguramos de que existe al menos un registro
                if (c.moveToFirst()) {//recorremos todas las filas
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        //referenciar la columna en la tabla
                        String columna = c.getString(0);
                        String columna1 = c.getString(1);//clave inventario
                        String columna2 = c.getString(2);
                        String columna3 = c.getString(3);
                        String columna4 = c.getString(4);
                        String columna5 = c.getString(5);
                        String columna6 = c.getString(6);
                        String columna7 = c.getString(7);
                        String columna8 = c.getString(8);
                        String columna9 = c.getString(9);
                        //tx a mostrar
                        if (columna7.length() == 2){
                            Toast.makeText(getApplicationContext(), "La ubicación esta cerrada..Desea abrirlo nuevamente.?", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getApplicationContext(), "Ubicación abierta.", Toast.LENGTH_SHORT).show();

                    } while(c.moveToNext());
                    //tx a mostrar

            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error al consultar inventario_soporte", Toast.LENGTH_SHORT).show();
        }
    }
}
