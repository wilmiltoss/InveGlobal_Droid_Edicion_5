package com.example.wmiltos.inveglobal_droid.Deposito;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.Limpiar2Activity;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.RedActivity;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.VisualizarRegistroActivity;
import com.example.wmiltos.inveglobal_droid.principal.ventanas.StockActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

public class StockDeposito extends AppCompatActivity {

    TextView txScanning, txDescripcion, txDetalle, txSector, txCantidad, txIScanning,
            txSumaCantidad,txResulSuma, nroLecturas, tvCantidadConteo,txSectorDescripcion,
    txSLocal,txSUbicacion,txSmanipulacion ;
    EditText campoCantidad;
    ConexionSQLiteHelper conn;
    Button btnGrabar;
    BottomNavigationView navigation;
    private Cursor fila;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_deposito);

        variables();
        recepcionDatosLecturas();
        consultar();
        mostrarCantidad();
        muestraSectorDescripcion();


        //si no tenemos el registro agregamos,osino actualizamos los campos
        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cantidad = campoCantidad.getText().toString();
                //si la cantidad ingresada es mayor a la cantidad permitida o tiene mas de 5 digitos, mostrar el dialogo
                if (cantidad.length() >= 5) {//si es mayor a la cantidad cargada
                    dialogoGuardarRegistro();

                }else{//si no va a comprobarRegistro luego lo guarda  directamente
                    comprobarRegistro();
                }
            }
        });
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
                    Intent intent2 = new Intent(StockDeposito.this, GenerarArchivoDeposito.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_notifications:
                    Intent intent3 = new Intent(StockDeposito.this, LimpiarDepo.class);
                    startActivity(intent3);
                    return true;
            }
            return false;
        }
    };

    private void dialogo() {
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(StockDeposito.this);
        dialogo.setMessage("Desea salir del Stock?").setTitle("InveGlobal")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Intent intent = new Intent(StockActivity.this, LoginActivity.class);
                //startActivity(intent);
                StockDeposito.this.finish();//finaliza la ventana
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


    private void consultar() {
        SQLiteDatabase db = conn.getReadableDatabase();
        String str = txScanning.getText().toString();
        String pri_digito=str.substring(0,1);//extrae el 1er digito
        String[] campos = {Utilidades.CAMPO_DESCRIPCION_SCANNING, Utilidades.CAMPO_DETALLE, Utilidades.CAMPO_ID_SECTOR};

        try {
            if (pri_digito.equals("0")) {//si el 1er digito es 0
                String scanning = str.substring(1);//lo eliminamos y realizamos la consulta
                String[] parametros = {scanning};//guardamos en un array scanning ya con el 1er digito eliminado

                Cursor cursor = db.query(Utilidades.TABLA_MAESTRO, campos, Utilidades.CAMPO_SCANNING + "=?", parametros, null, null, null);
                cursor.moveToFirst();
                txDescripcion.setText(cursor.getString(0));//muestra los campos
                txDetalle.setText(cursor.getString(1));
                txSector.setText(cursor.getString(2));
                cursor.close();

            }else{
                String[] parametros = {str};//guarmados en un array el str con todos sus digitos

                Cursor cursor = db.query(Utilidades.TABLA_MAESTRO, campos, Utilidades.CAMPO_SCANNING + "=?", parametros, null, null, null);
                cursor.moveToFirst();
                txDescripcion.setText(cursor.getString(0));//muestra los campos
                txDetalle.setText(cursor.getString(1));
                txSector.setText(cursor.getString(2));
                cursor.close();
            }

        } catch (Exception e) {
           // Toast.makeText(getApplicationContext(), "El codigo no existe", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            StockDeposito.this.finish();
           // limpiar();
        }
    }

    private void limpiar() {
        txIScanning.setText("");
        txCantidad.setText("");
        txScanning.setText("");
        txDescripcion.setText("");
        txSector.setText("");
        txDetalle.setText("");
        txCantidad.setText("");
        txSumaCantidad.setText("");
        campoCantidad.setText("");
    }

    private void recepcionDatosLecturas() {
        Bundle miBundle = this.getIntent().getExtras();
        if(miBundle!=null){

            String local_depo =miBundle.getString("msjL_local");
            txSLocal.setText(local_depo);
            String ubicacion_depo =miBundle.getString("msjL_Ubicacion");
            txSUbicacion.setText(ubicacion_depo);
            String cod_manipulacion =miBundle.getString("msjL_Manipulacion");
            txSmanipulacion.setText(cod_manipulacion);

            // ELIMINAR 0 A LA IZQUIERDA
            //si el 1er digito es igual a 0 lo guardamos en scanning0 sino guardamos en scanning
            String scanning =miBundle.getString("msjScanning");
            String pri_digito=scanning.substring(0,1);//extrae el 1er digito
            if (pri_digito.equals("0")) {//si el 1er digito es 0
                String scanning0 = scanning.substring(1);//lo eliminamos y gurdamos
                txScanning.setText(scanning0);
            }else {
                txScanning.setText(scanning);
            }

        }
    }

    public void dialogoGuardarRegistro(){
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(StockDeposito.this);
        dialogo.setMessage("Desea guardarlo de todas formas.?").setTitle("La cantidad excede a lo permitido..!!")
                .setIcon(R.drawable.alerta_roja);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                comprobarRegistro();

            }
        });
        dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                campoCantidad.setText("");
            }
        });
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    private void comprobarRegistro() {

        if (campoCantidad.getText().length() != 0) {//si el campo esta vacio, mostrar mensaje
            SQLiteDatabase db = conn.getWritableDatabase();
            String[] parametros = { txSLocal.getText().toString(),
                                    txSUbicacion.getText().toString(),
                                    txScanning.getText().toString(),
                                    txSmanipulacion.getText().toString()
                  };
            fila = db.rawQuery("SELECT " + Utilidades.CAMPO_CANTIDAD_DEP +

                            " FROM " + Utilidades.TABLA_DEPOSITO + " WHERE "
                            + Utilidades.CAMPO_LOCAL_DEP + "=? AND "
                            + Utilidades.CAMPO_UBICACION_DEP + "=? AND "
                            + Utilidades.CAMPO_COD_BARRA_DEP + "=? AND "
                            + Utilidades.CAMPO_COD_UND_MANIPULACION + "=?"
                    , parametros, null);
            //si el select tira cantidad va actualizar, sino va registrar
            if (fila.moveToFirst()) {
                if (fila != null) {
                    actualizarDialogo();
                }
            } else {
                registrarDialogo();
            }

        } else {
            Toast.makeText(getApplicationContext(), "El campo esta vacio", Toast.LENGTH_LONG).show();
        }

    }

    private void registrarDialogo() {
        //1 -creamos dialogo
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(StockDeposito.this);
        dialogo.setMessage("Datos Guardados!").setTitle("Datos de Lectura")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                registrarTablaLecturaSQL();
                limpiar();
                volverAmenu();
            }
        });
        //4-crear alertDialogo
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    private void registrarTablaLecturaSQL() {
        try{
            //CONEXION
            SQLiteDatabase db = conn.getWritableDatabase();
            //CON SENTENCIA SQL
            String insert="INSERT INTO "+Utilidades.TABLA_DEPOSITO+"("
                    +Utilidades.CAMPO_LOCAL_DEP+","
                    +Utilidades.CAMPO_UBICACION_DEP+","
                    +Utilidades.CAMPO_COD_BARRA_DEP+","
                    +Utilidades.CAMPO_CANTIDAD_DEP+","
                    +Utilidades.CAMPO_COD_UND_MANIPULACION+")"+
                    "VALUES ('"+txSLocal.getText().toString()+"','"
                                +txSUbicacion.getText().toString()+"','"
                                +txScanning.getText().toString()+"','"
                                +campoCantidad.getText().toString()+"','"
                                +txSmanipulacion.getText().toString()+
                                "')";

            //"Id Registrado" es el mensaje que envia al insertar
            Toast.makeText(getApplicationContext(),"Articulo Registrado OK",Toast.LENGTH_SHORT).show();
            db.execSQL(insert);
            db.close();
            sumaRegistrosSQL();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    private void actualizarDialogo() {

        //1 -creamos dialogo
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(StockDeposito.this);
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

    private void dialogoReemplazar() {
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(StockDeposito.this);
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
            String[] parametros = { txSLocal.getText().toString(),
                                    txSUbicacion.getText().toString(),
                                    txScanning.getText().toString(),
                                    txSmanipulacion.getText().toString()};

            ContentValues valores = new ContentValues();
            valores.put(Utilidades.CAMPO_CANTIDAD_DEP,campoCantidad.getText().toString());
            db.update(Utilidades.TABLA_DEPOSITO,valores, Utilidades.CAMPO_LOCAL_DEP+"=? AND "
                                                                    +Utilidades.CAMPO_UBICACION_DEP+"=? AND "
                                                                    +Utilidades.CAMPO_COD_BARRA_DEP+"=? AND "
                                                                    +Utilidades.CAMPO_COD_UND_MANIPULACION+"=?"
                                                                    ,parametros);
            Toast.makeText(getApplicationContext(),"Cantidad reemplazada",Toast.LENGTH_SHORT).show();
            db.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al reemplazar la cantidad", Toast.LENGTH_LONG).show();
        }
    }

    private void volverAmenu() {
        StockDeposito.this.finish();
    }

    private void actualizarTablaLecturaSQL3() {
        try{
            sumaStock();
            SQLiteDatabase db = conn.getWritableDatabase();
            String[] parametros = { txSLocal.getText().toString(),
                                    txSUbicacion.getText().toString(),
                                    txScanning.getText().toString(),
                                    txSmanipulacion.getText().toString()};

            ContentValues valores = new ContentValues();
            valores.put(Utilidades.CAMPO_CANTIDAD_DEP,txResulSuma.getText().toString());
            db.update(Utilidades.TABLA_DEPOSITO,valores, Utilidades.CAMPO_LOCAL_DEP+"=? AND "
                                                                    +Utilidades.CAMPO_UBICACION_DEP+"=? AND "
                                                                    +Utilidades.CAMPO_COD_BARRA_DEP+"=? AND "
                                                                    +Utilidades.CAMPO_COD_UND_MANIPULACION+"=?"
                                                                    ,parametros);
            Toast.makeText(getApplicationContext(),"Se agrego la cantidad",+Toast.LENGTH_SHORT).show();
            db.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al agregar la cantidad", Toast.LENGTH_LONG).show();
        }

    }

    private void sumaStock() {
        int aux1 = Integer.valueOf(txSumaCantidad.getText().toString());
        int aux2 = Integer.valueOf(campoCantidad.getText().toString());

        int resultado = aux1 + aux2;
        txResulSuma.setText(""+resultado);
    }


    private void variables() {
        conn= new ConexionSQLiteHelper(getApplicationContext(),"InveStock.sqlite",null,1);
        btnGrabar =  findViewById(R.id.btn_grabar);

        txScanning =  findViewById(R.id.tx_Scanning_Stock);
        txDescripcion =  findViewById(R.id.txtDescripcion);
        txDetalle =  findViewById(R.id.txtDetalle);
        txSector =  findViewById(R.id.txtSector);
        txCantidad =  findViewById(R.id.txtCantidad);
        txDescripcion =  findViewById(R.id.etDescripcion);
        txSumaCantidad= findViewById(R.id.tx_sumaCantidad);
        txResulSuma= findViewById(R.id.txResulSuma);
        nroLecturas=  findViewById(R.id.tvNroLecturas);
        txSectorDescripcion= findViewById(R.id.txtSectorDescrip);
        txIScanning =  findViewById(R.id.tx_Scanning_Stock);

        txSLocal = findViewById(R.id.tv_SLocal);
        txSUbicacion = findViewById(R.id.tv_SUbicacion);
        txSmanipulacion = findViewById(R.id.tv_Smanipulacion);

        campoCantidad = findViewById(R.id.etCantidad);
        tvCantidadConteo = findViewById(R.id.tvCantidadConteo);

        navigation = findViewById(R.id.navigation_deposito);
        navigation.setOnNavigationItemSelectedListener(menuNavegacion);

    }

    public void onClickBtn1(View view) {

        try {
            int aux1 = Integer.valueOf(campoCantidad.getText().toString());
            int resultado = aux1 * (-1);
            campoCantidad.setText("" + resultado);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "El campo esta vacio", Toast.LENGTH_LONG).show();
        }
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

    //mostrar la cantidad de existencia del codigo cargado
    public void mostrarCantidad(){
        try{
            SQLiteDatabase db = conn.getReadableDatabase();
            String[] parametros =  {txSLocal.getText().toString(),
                                    txSUbicacion.getText().toString(),
                                    txScanning.getText().toString(),
                                    txSmanipulacion.getText().toString()};
            //Si la tabla tiene registros muestra la cantidad sino no lo muestra y pasa al sgte ventana
            try{
                Cursor cursor = db.rawQuery("SELECT "+Utilidades.CAMPO_CANTIDAD_DEP+
                                " FROM "+Utilidades.TABLA_DEPOSITO+" WHERE "+Utilidades.CAMPO_LOCAL_DEP+"=? AND "
                                                                            +Utilidades.CAMPO_UBICACION_DEP+"=? AND "
                                                                            +Utilidades.CAMPO_COD_BARRA_DEP+"=? AND "
                                                                            +Utilidades.CAMPO_COD_UND_MANIPULACION+"=?"
                                                                            ,parametros);
                cursor.moveToFirst();
                //muestra los campos
                txSumaCantidad.setText(cursor.getString(0));
                cursor.close();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al mostrar la cantidad", Toast.LENGTH_LONG).show();
        }
    }

    public void sumaRegistrosSQL() {
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor c = db.rawQuery("select * from " + Utilidades.TABLA_DEPOSITO, null);
        int contador = c.getCount();
        int suma = contador;

        Toast.makeText(getApplicationContext(), "Nro.Lecturas: "+ suma, Toast.LENGTH_SHORT).show();


       /* if (suma == 0) {
            nroLecturasL.setText("Nro.Lecturas: 0");
        }else {
            nroLecturasL.setText("Nro.Lecturas:   " + suma2);
        }*/
        db.close();
    }
}
