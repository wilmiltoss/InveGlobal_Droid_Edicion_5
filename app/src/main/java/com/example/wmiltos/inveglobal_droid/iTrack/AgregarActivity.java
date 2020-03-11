package com.example.wmiltos.inveglobal_droid.iTrack;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.principal.ventanas.StockActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

public class AgregarActivity extends AppCompatActivity {

    TextView codigoBarra, txDescripcion, txDetalle, txSector,txSectorDescripcion, txResulSuma,txSumaCantidad;
    TextView campoTipoNegocio, campoCadena, campoLocal, campoTipoSoporte, campoUsuario;
    EditText campoCantidad;
    ConexionSQLiteHelper conn;
    Button btnAgregar;
    private Cursor fila;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        variables();
        recepcionDatosQuiebre();
        consultar();
        muestraSectorDescripcion();
        mostrarCantidad();
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    comprobarRegistro();
            }
        });
    }

    public void comprobarRegistro(){
        try {
            if (campoCantidad.getText().length() != 0) {//si el campo esta vacio, mostrar mensaje
                SQLiteDatabase db = conn.getWritableDatabase();
                String[] parametros = { codigoBarra.getText().toString(),
                                        txDescripcion.getText().toString(),
                                        txSectorDescripcion.getText().toString()};
                fila = db.rawQuery("SELECT " + Utilidades.CAMPO_CANT_PROD +
                                        " FROM "  + Utilidades.TABLA_PRODUCTOS +
                                        " WHERE " + Utilidades.CAMPO_CODIGO_BARRA + " =? AND "
                                                   + Utilidades.CAMPO_DESCRIP + "=? AND "
                                                   + Utilidades.CAMPO_CATEGORIA + "=? "
                                                  , parametros, null);
                //si el select tira cantidad va actualizar, sino va registrar
                if (fila.moveToFirst()) {//si trae registros = true
                    if (fila != null) {//si la fila existe
                        Toast.makeText(getApplicationContext(), "El codigo ya existe en la lista", Toast.LENGTH_LONG).show();
                        actualizarDialogo();//lo actualiza
                    }
                } else {//si no trae registros = false
                    registrarDialogo();//lo registra
                }
            } else {
                Toast.makeText(getApplicationContext(), "El campo cantidad esta vacio", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }


    private void registrarDialogo() {
        //1 -creamos dialogo
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(AgregarActivity.this);
        dialogo.setMessage("Datos Guardados!").setTitle("Datos de Lectura")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                registrarTablaProducto();
                limpiar();
                volverAmenu();
            }
        });
        //4-crear alertDialogo
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    //registro SQL
    private void registrarTablaProducto() {
        try{
            //CONEXION
            SQLiteDatabase db = conn.getWritableDatabase();
            //CON SENTENCIA SQL
            String insert="INSERT INTO "+ Utilidades.TABLA_PRODUCTOS +"("+Utilidades.CAMPO_CODIGO_BARRA+","
                                                                         +Utilidades.CAMPO_DESCRIP+","
                                                                         +Utilidades.CAMPO_CANTIDAD+","
                                                                         +Utilidades.CAMPO_CATEGORIA+","
                                                                         +Utilidades.CAMPO_LOCAL+","
                                                                         +Utilidades.CAMPO_CADENA_P+","
                                                                         +Utilidades.CAMPO_TIPO_NEGOCIO+","
                                                                         +Utilidades.CAMPO_TIPO_SOPORTE+")"+
                                                                         "VALUES ('"+codigoBarra.getText().toString()+"','"
                                                                                    +txDescripcion.getText().toString()+"',"
                                                                                    +campoCantidad.getText().toString()+",'"
                                                                                    +txSectorDescripcion.getText().toString()+"','"
                                                                                    +campoLocal.getText().toString()+"','"
                                                                                    +campoCadena.getText().toString()+"','"
                                                                                    +campoTipoNegocio.getText().toString()+"','"
                                                                                    +campoTipoSoporte.getText().toString()+"')";

            //"Id Registrado" es el mensaje que envia al insertar
            Toast.makeText(getApplicationContext(),"Lectura Registrada",Toast.LENGTH_SHORT).show();
            db.execSQL(insert);
            db.close();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void actualizarDialogo() {
        //1 -creamos dialogo
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(AgregarActivity.this);
        dialogo.setMessage("¿Se Agregar a esta cantidad a la ya antes Cargada?").setTitle("Datos de Lectura")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                actualizarTablaLecturaSQL3();
                limpiar();
                volverAmenu();
            }
        });

        dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogoReemplazar();
            }
        });
        //4-crear alertDialogo
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    public void dialogoReemplazar(){
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(AgregarActivity.this);
        dialogo.setMessage("Esta seguro de Reemplazar la Cantidad antes Cargada?").setTitle("Datos de Lectura")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reemplazarTablaLecturaSQL();
                volverAmenu();
            }
        });
        dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                volverAmenu();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    private void reemplazarTablaLecturaSQL() {
        try{
            sumaStock();
            SQLiteDatabase db = conn.getWritableDatabase();
            String[] parametros = { "1",
                    "1",
                    "1",
                    "1",
                    "1",
                    "1",
                    codigoBarra.getText().toString()};

            ContentValues valores = new ContentValues();
            valores.put(Utilidades.CAMPO_CANT_PROD,campoCantidad.getText().toString());
            db.update(Utilidades.TABLA_PRODUCTOS,valores, Utilidades.CAMPO_ID+"=? AND "
                            +Utilidades.CAMPO_CODIGO_BARRA+"=? AND "
                            +Utilidades.CAMPO_DESCRIP+"=? AND "
                            +Utilidades.CAMPO_CANT_PROD+"=? AND "
                            +Utilidades.CAMPO_CATEGORIA+"=? AND "
                            +Utilidades.CAMPO_LOCAL+"=?"
                    ,parametros);
            Toast.makeText(getApplicationContext(),"Cantidad reemplazada",Toast.LENGTH_SHORT).show();
            db.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al reemplazar la cantidad", Toast.LENGTH_LONG).show();
        }

    }

    private void volverAmenu() {
       envioDatosQuiebreRetorno();
    }

    private void actualizarTablaLecturaSQL3() {
        try{
            sumaStock();
            SQLiteDatabase db = conn.getWritableDatabase();
            String[] parametros = { codigoBarra.getText().toString(),
                                    txDescripcion.getText().toString(),
                                    txSectorDescripcion.getText().toString()};

            ContentValues valores = new ContentValues();
            valores.put(Utilidades.CAMPO_CANTIDAD,txResulSuma.getText().toString());
            db.update(Utilidades.TABLA_PRODUCTOS,valores,    Utilidades.CAMPO_CODIGO_BARRA+"=? AND "
                                                                        +Utilidades.CAMPO_DESCRIP+"=? AND "
                                                                        +Utilidades.CAMPO_CATEGORIA+"=?"
                                                                         ,parametros);
            Toast.makeText(getApplicationContext(),"Se agrego la cantidad",Toast.LENGTH_SHORT).show();
            db.close();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void variables() {
        conn = new ConexionSQLiteHelper(getApplicationContext(), "InveStock.sqlite", null, 1);
        codigoBarra = findViewById(R.id.txtScanning);
        txDescripcion =  findViewById(R.id.tx_Descripcion);
        txDetalle =  findViewById(R.id.txtDetalle);
        txSector =  findViewById(R.id.txtSector);
        txSectorDescripcion=findViewById(R.id.txtSectorDescrip);
        campoCantidad = findViewById(R.id.etCantidad);
        btnAgregar = findViewById(R.id.btn_agregar);
        txResulSuma=findViewById(R.id.txResulSuma);
        txSumaCantidad=findViewById(R.id.tx_sumaCantidad);

        campoCadena=findViewById(R.id.txtcadena);
        campoLocal=findViewById(R.id.txtlocal);
        campoTipoNegocio=findViewById(R.id.txttipoNegocio);
        campoTipoSoporte=findViewById(R.id.txttipoSoporte);
        campoUsuario=findViewById(R.id.txtusuario);
    }

    private void recepcionDatosQuiebre() {//A-R1
        //recibirScannerCamara();
        Bundle miBundle = this.getIntent().getExtras();
        if(miBundle!=null){
            String tipoNegocio = miBundle.getString("msjTipoNegocio");
            campoTipoNegocio.setText(tipoNegocio);

            String cadena = miBundle.getString("msjCadena");
            campoCadena.setText(cadena);

            String local = miBundle.getString("msjLocal");
            campoLocal.setText(local);

            String tipoSoporte = miBundle.getString("msjTipoSoporte");
            campoTipoSoporte.setText(tipoSoporte);

            String usuario = miBundle.getString("msjUsuario");
            campoUsuario.setText(usuario);

            //8 ELIMINAR 0 A LA IZQUIERDA
            //si el 1er digito es igual a 0 lo guardamos en scanning0 sino guardamos en scanning
            String scanning =miBundle.getString("msjScanning");
            String pri_digito=scanning.substring(0,1);//extrae el 1er digito
            if (pri_digito.equals("0")) {//si el 1er digito es 0
                String scanning0 = scanning.substring(1);//lo eliminamos y gurdamos
                codigoBarra.setText(scanning0);
            }else {
                codigoBarra.setText(scanning);
            }
        }
    }
    private void envioDatosQuiebreRetorno(){//A-E2
        Intent miIntent = null;
        String msjTipoNegocioR = campoTipoNegocio.getText().toString();
        String msjCadenaR = campoCadena.getText().toString();
        String msjLocalR = campoLocal.getText().toString();
        String msjTipoSoporteR = campoTipoSoporte.getText().toString();
        String msjUsuarioR = campoUsuario.getText().toString();

        miIntent = new Intent(AgregarActivity.this, QuiebreActivity.class);
        Bundle miBundle = new Bundle();

        miBundle.putString("msjTipoNegocioR",campoTipoNegocio.getText().toString());
        miBundle.putString("msjCadenaR",campoCadena.getText().toString());
        miBundle.putString("msjLocalR",campoLocal.getText().toString());
        miBundle.putString("msjTipoSoporteR",campoTipoSoporte.getText().toString());
        miBundle.putString("msjUsuarioR",campoUsuario.getText().toString());

        miIntent.putExtras(miBundle);
        startActivity(miIntent);
        AgregarActivity.this.finish();

    }

    //muestra los datos del scanning segun el tx que capturamos de la ventana anterior
    private void consultar() {
        try {
            SQLiteDatabase db = conn.getReadableDatabase();
                String str = codigoBarra.getText().toString();
                String pri_digito = str.substring(0, 1);//extrae el 1er
                String[] campos = {Utilidades.CAMPO_DESCRIPCION_SCANNING, Utilidades.CAMPO_DETALLE, Utilidades.CAMPO_ID_SECTOR};

                if (pri_digito.equals("0")) {//si el 1er digito es 0
                    String scanning = str.substring(1);//lo eliminamos y realizamos la consulta
                    String[] parametros = {scanning};//guarmados en un array scanning ya con el 1er digito eliminado
                    Cursor cursor = db.query(Utilidades.TABLA_MAESTRO, campos, Utilidades.CAMPO_SCANNING + "=?", parametros, null, null, null);
                    cursor.moveToFirst();
                    txDescripcion.setText(cursor.getString(0));//muestra los campos
                    txDetalle.setText(cursor.getString(1));
                    txSector.setText(cursor.getString(2));
                    cursor.close();
                } else {
                    String[] parametros = {str};//guarmados en un array el str con todos sus digitos
                    Cursor cursor = db.query(Utilidades.TABLA_MAESTRO, campos, Utilidades.CAMPO_SCANNING + "=?", parametros, null, null, null);
                    cursor.moveToFirst();
                    txDescripcion.setText(cursor.getString(0));//muestra los campos
                    txDetalle.setText(cursor.getString(1));
                    txSector.setText(cursor.getString(2));
                    cursor.close();
                }

        } catch (Exception e) {
             Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            envioDatosQuiebreRetorno();//funciona si es ya fue cargado un codigo
            limpiar();
        }
    }

    private void limpiar() {
        txDescripcion.setText("");
        txSector.setText("");
        txDetalle.setText("");
    }

    public void muestraSectorDescripcion (){
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parametros = {txSector.getText().toString()};
        String[] campos = {Utilidades.CAMPO_ID_SECTOR, Utilidades.CAMPO_DESCRIPCION_SECTOR};
        try {
            Cursor cursor = db.query(Utilidades.TABLA_SECTORES, campos, Utilidades.CAMPO_ID_SECTOR + "=?", parametros, null, null, null);
            cursor.moveToFirst();
            txSectorDescripcion.setText(cursor.getString(1));//muestra los campos
            cursor.close();

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "Error en mostrar sector", Toast.LENGTH_LONG).show();
            limpiar();
        }
    }

    public void botonResta(){
        try {
            int aux1 = Integer.valueOf(campoCantidad.getText().toString());
            int resultado = aux1 * (-1);
            campoCantidad.setText("" + resultado);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "El campo esta vacio", Toast.LENGTH_LONG).show();
        }

    }
    public void onClickResta(View view) {
        botonResta();
    }

    public void sumaStock(){
        int aux1 = Integer.valueOf(txSumaCantidad.getText().toString());
        int aux2 = Integer.valueOf(campoCantidad.getText().toString());

        int resultado = aux1 + aux2;
        txResulSuma.setText(""+resultado);
    }

    public void mostrarCantidad(){
        try {
                SQLiteDatabase db = conn.getWritableDatabase();
                String[] parametros = { codigoBarra.getText().toString(),
                        txDescripcion.getText().toString(),
                        txSectorDescripcion.getText().toString()};
                fila = db.rawQuery("SELECT " + Utilidades.CAMPO_CANT_PROD +
                                " FROM "  + Utilidades.TABLA_PRODUCTOS +
                                " WHERE " + Utilidades.CAMPO_CODIGO_BARRA + " =? AND "
                                + Utilidades.CAMPO_DESCRIP + "=? AND "
                                + Utilidades.CAMPO_CATEGORIA + "=? "
                        , parametros, null);
                //mostrar la cantidad
                fila.moveToFirst();
                txSumaCantidad.setText(fila.getString(0));
        }catch (Exception ex){
           // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
}

