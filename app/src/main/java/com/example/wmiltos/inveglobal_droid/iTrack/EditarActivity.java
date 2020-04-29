package com.example.wmiltos.inveglobal_droid.iTrack;

import android.content.ContentValues;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Productos;
import com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV_Objetos.Locales;
import com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV_Objetos.Ubicaciones;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.util.ArrayList;

public class EditarActivity extends AppCompatActivity {

    TextView codigoBarra, txDescripcion, txDetalle, txSector,txSectorDescripcion, txResulSuma,txSumaCantidad;
    TextView campoTipoNegocio, campoCadena, campoLocal, campoTipoSoporte, campoUsuario, campoJoin;
    //de la captura de los datos spinner
    TextView txUbicacion, txDescripcionUbicacion;
    EditText campoCantidad;
    ConexionSQLiteHelper conn;
    Button btnAgregar;
    private Cursor fila;
    String banderaBack = "0";

    Spinner comboUbicaciones;
    ArrayList<String>listaUbicacionesSpinner;
    ArrayList<Ubicaciones>ubicacionLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        variables();
        recepcionDatosQuiebre();
        consultar();
        muestraSectorDescripcion();
        validacionCampoUbicacion();
        consultarListaUbicaciones();
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SQLiteDatabase db = conn.getWritableDatabase();
                    String parametro = codigoBarra.getText().toString();
                    String Query = "Select CANTIDAD from " +Utilidades.TABLA_PRODUCTOS+ " WHERE CODIGO_BARRA = "+parametro;
                    Cursor cursor = db.rawQuery(Query, null);
                    //mostrar
                   cursor.moveToNext();
                   String  product_ = cursor.getString(0);
                    if (product_.equals("0"))//si no hay registro enviar mensaje
                    {
                        comprobarRegistroProducto();
                    } else {
                        comprobarRegistro();
                    }
                }catch (Exception e){
                    comprobarRegistro();
                }
        }
        });

        //Adaptadores de secuencias dentro del onCreate
        ArrayAdapter<CharSequence> adaptadorUbicaciones = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaUbicacionesSpinner);
        //mostrar en el combo el array adaptador
        comboUbicaciones.setAdapter(adaptadorUbicaciones);

        //captura de datos del spinner
        comboUbicaciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int posicion, long l) {
                //accede al elemento seleccionado
                txUbicacion.setText(ubicacionLista.get(posicion).getId_ubicacion());
                txDescripcionUbicacion.setText(ubicacionLista.get(posicion).getDescr_ubicacion());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    //2-salto automatico de ventana
    public void validacionCampoUbicacion (){
        txUbicacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            //Pasar a la sgte ventana automaticamente si el numero de caracteres son las sgtes
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mostrarCantidad();
            }
        });
    }

    //boton fisico atraz
    @Override
    public void onBackPressed() {
        dialogo();
    }

    //spinner Ubicacion
    private void consultarListaUbicaciones() {
        try {
            SQLiteDatabase db = conn.getReadableDatabase();
            Ubicaciones ubicaciones = null;
            ubicacionLista = new ArrayList<>();
            Cursor cursor = db.rawQuery(Utilidades.CONSULTA_TABLA_UBICACIONES, null);
            while (cursor.moveToNext()) {
                ubicaciones = new Ubicaciones();
                ubicaciones.setId_ubicacion(cursor.getString(0));
                ubicaciones.setDescr_ubicacion(cursor.getString(1));

                Log.i("id", ubicaciones.getId_ubicacion());
                Log.i("descripcion", ubicaciones.getDescr_ubicacion());
                ubicacionLista.add(ubicaciones);
            }
            //obtener la lista del array
            listaUbicacionesSpinner = new ArrayList<>();
            for (int i = 0; i < ubicacionLista.size(); i++) {
                listaUbicacionesSpinner.add(ubicacionLista.get(i).getId_ubicacion() + " - " + ubicacionLista.get(i).getDescr_ubicacion());
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //dialogo antes de salir
    public void dialogo(){
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(EditarActivity.this);
        dialogo.setMessage("Desea cancelar la carga actual?").setTitle("Cancelar Carga")
                .setIcon(R.drawable.alert_dialogo);
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                volverAmenu();
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
    public void comprobarRegistro(){
        try {
            if (campoCantidad.getText().length() != 0) {//si el campo esta vacio, mostrar mensaje
                SQLiteDatabase db = conn.getWritableDatabase();
                String[] parametros = { codigoBarra.getText().toString(),
                        txDescripcion.getText().toString(),
                        txSectorDescripcion.getText().toString(),
                        txUbicacion.getText().toString()
                };
                fila = db.rawQuery("SELECT " + Utilidades.CAMPO_CANT_PROD +
                                " FROM "  + Utilidades.TABLA_PRODUCTOS +
                                " WHERE " + Utilidades.CAMPO_CODIGO_BARRA + " =? AND "
                                          + Utilidades.CAMPO_DESCRIP + "=? AND "
                                          + Utilidades.CAMPO_CATEGORIA + "=? AND "
                                          + Utilidades.CAMPO_UBICACION + "=? "
                        , parametros, null);
                //si el select tira cantidad va actualizar, sino va registrar
                if (fila.moveToFirst()) {//si trae registros = true
                    if (fila != null) {//si la fila existe
                        // Toast.makeText(getApplicationContext(), "El codigo ya existe en la lista", Toast.LENGTH_LONG).show();
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

    //comprueba si existe registro en la tabla de productos importados de la lista.csv
    public void comprobarRegistroProducto(){
        try {
            if (campoCantidad.getText().length() != 0) {//si el campo esta vacio, mostrar mensaje
                SQLiteDatabase db = conn.getWritableDatabase();
                String [] parametros = {codigoBarra.getText().toString()};

                fila = db.rawQuery("SELECT " + Utilidades.CAMPO_CANT_PROD +
                                " FROM "  + Utilidades.TABLA_PRODUCTOS +
                                " WHERE " + Utilidades.CAMPO_CODIGO_BARRA + " =? "
                        , parametros, null);
                //si el select tira cantidad va actualizar, sino va registrar
                if (fila.moveToFirst()) {//si trae registros = true
                    if (fila != null) {//si la fila existe
                        actualizarTablaProductos();
                        limpiar();
                        volverAmenu();
                    }
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
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(EditarActivity.this);
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
            //Del campo join = union de los campos codigoBarra, Descripcion y Cantidad
            String union = codigoBarra.getText().toString()+" -"+txDescripcion.getText().toString()+"  "+campoCantidad.getText().toString();
            //CON SENTENCIA SQL
            String insert="INSERT INTO "+ Utilidades.TABLA_PRODUCTOS +"("+Utilidades.CAMPO_CODIGO_BARRA+","
                    +Utilidades.CAMPO_DESCRIP+","
                    +Utilidades.CAMPO_CANTIDAD+","
                    +Utilidades.CAMPO_CATEGORIA+","
                    +Utilidades.CAMPO_LOCAL+","
                    +Utilidades.CAMPO_CADENA_P+","
                    +Utilidades.CAMPO_TIPO_NEGOCIO+","
                    +Utilidades.CAMPO_TIPO_SOPORTE+","
                    +Utilidades.CAMPO_UBICACION+","
                    +Utilidades.CAMPO_AUX+")"+
                    "VALUES ('"+codigoBarra.getText().toString()+"','"
                    +txDescripcion.getText().toString()+"',"
                    +campoCantidad.getText().toString()+",'"
                    +txSectorDescripcion.getText().toString()+"','"
                    +campoLocal.getText().toString()+"','"
                    +campoCadena.getText().toString()+"','"
                    +campoTipoNegocio.getText().toString()+"','"
                    +campoTipoSoporte.getText().toString()+"','"
                    +txUbicacion.getText().toString()+"','"
                    +union+"')";
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
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(EditarActivity.this);
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
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(EditarActivity.this);
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
            String[] parametros = { codigoBarra.getText().toString(),
                                   // txDescripcion.getText().toString(),
                                    //txSectorDescripcion.getText().toString(),
                                   // campoLocal.getText().toString()
                                    };

            ContentValues valores = new ContentValues();
            valores.put(Utilidades.CAMPO_CANT_PROD,campoCantidad.getText().toString());
            db.update(Utilidades.TABLA_PRODUCTOS,valores, Utilidades.CAMPO_CODIGO_BARRA+"=? "
                                                                     // +Utilidades.CAMPO_DESCRIP+"=? AND "
                                                                     // +Utilidades.CAMPO_DESCRIPCION_SECTOR+"=? AND "
                                                                   //   +Utilidades.CAMPO_CATEGORIA+"=? AND "
                                                                     // +Utilidades.CAMPO_LOCAL+"=?"
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
            String[] parametros = {  codigoBarra.getText().toString(),
                                     txDescripcion.getText().toString(),
                                    // txSectorDescripcion.getText().toString(),
                                     txUbicacion.getText().toString()
                                    };

            ContentValues valores = new ContentValues();
            valores.put(Utilidades.CAMPO_CANTIDAD,txResulSuma.getText().toString());
            valores.put(Utilidades.CAMPO_UBICACION,txUbicacion.getText().toString());
            db.update(Utilidades.TABLA_PRODUCTOS,valores,    Utilidades.CAMPO_CODIGO_BARRA+"=? AND "
                                                                        +Utilidades.CAMPO_DESCRIP+"=? AND "
                                                                       // +Utilidades.CAMPO_CATEGORIA+"=? AND "
                                                                        +Utilidades.CAMPO_UBICACION+"=?"
                                                                         ,parametros);
            Toast.makeText(getApplicationContext(),"Se agrego la cantidad",Toast.LENGTH_SHORT).show();
            db.close();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void actualizarTablaProductos() {
        try{
            sumaStock();
            String union = codigoBarra.getText().toString()+" -"+txDescripcion.getText().toString()+"  "+campoCantidad.getText().toString();
            SQLiteDatabase db = conn.getWritableDatabase();
            String[] parametros = {codigoBarra.getText().toString()
                    //txDescripcion.getText().toString(),
                    // txSectorDescripcion.getText().toString(),
                    //txUbicacion.getText().toString()
            };

            ContentValues valores = new ContentValues();
            //seteo de campos de la tabla producto
            valores.put(Utilidades.CAMPO_DESCRIP,txDescripcion.getText().toString());
            valores.put(Utilidades.CAMPO_CANTIDAD,txResulSuma.getText().toString());//seteo de cantidad
            valores.put(Utilidades.CAMPO_CATEGORIA,txSectorDescripcion.getText().toString());
            valores.put(Utilidades.CAMPO_LOCAL,campoLocal.getText().toString());
            valores.put(Utilidades.CAMPO_CADENA_P,campoCadena.getText().toString());
            valores.put(Utilidades.CAMPO_TIPO_NEGOCIO,campoTipoNegocio.getText().toString());
            valores.put(Utilidades.CAMPO_TIPO_SOPORTE,campoTipoSoporte.getText().toString());
            valores.put(Utilidades.CAMPO_UBICACION,txUbicacion.getText().toString());//seteo de ubicacion
            valores.put(Utilidades.CAMPO_AUX,union);

            db.update(Utilidades.TABLA_PRODUCTOS,valores,    Utilidades.CAMPO_CODIGO_BARRA+"=?"
                                                                        // +Utilidades.CAMPO_DESCRIP+"=?"
                                                                        //+Utilidades.CAMPO_CATEGORIA+"=? AND "
                                                                        //+Utilidades.CAMPO_UBICACION+"=?"
                                                                        ,parametros);
            Toast.makeText(getApplicationContext(),"Registrado",Toast.LENGTH_SHORT).show();
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
        campoJoin=findViewById(R.id.tv_join);

        comboUbicaciones = findViewById(R.id.sp_ubicacion);
        txUbicacion = findViewById(R.id.textView_id_ubicacion);
        txDescripcionUbicacion = findViewById(R.id.textView_id_ubicacion);
    }

    //A-R1
    private void recepcionDatosQuiebre() {
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

            Integer bandera = miBundle.getInt("msjbandera");
            banderaBack = bandera.toString();//2)Recibimos la bandera y la guardamos en esta variable


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


        miIntent = new Intent(EditarActivity.this, QuiebreActivity.class);
        Bundle miBundle = new Bundle();

        miBundle.putString("msjTipoNegocioR",campoTipoNegocio.getText().toString());
        miBundle.putString("msjCadenaR",campoCadena.getText().toString());
        miBundle.putString("msjLocalR",campoLocal.getText().toString());
        miBundle.putString("msjTipoSoporteR",campoTipoSoporte.getText().toString());
        miBundle.putString("msjUsuarioR",campoUsuario.getText().toString());
        miBundle.putString("msjbanderaR", banderaBack);//3)Enviamos de retorno la bandera con el valor de la variable
        // Toast.makeText(getApplicationContext(),"bandera envio= "+banderaBack,Toast.LENGTH_SHORT).show();

        miIntent.putExtras(miBundle);
        startActivity(miIntent);
        EditarActivity.this.finish();

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
                String[] parametros = {scanning};//guardamos en un array scanning ya con el 1er digito eliminado
                Cursor cursor = db.query(Utilidades.TABLA_MAESTRO, campos, Utilidades.CAMPO_SCANNING + "=?", parametros, null, null, null);
                cursor.moveToFirst();
                txDescripcion.setText(cursor.getString(0));//muestra los campos
                txDetalle.setText(cursor.getString(1));
                txSector.setText(cursor.getString(2));
                cursor.close();
            } else {
                String[] parametros = {str};//guardamos en un array el str con todos sus digitos
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
                                    txSectorDescripcion.getText().toString(),
                                    txUbicacion.getText().toString()};
            fila = db.rawQuery("SELECT " + Utilidades.CAMPO_CANT_PROD +
                            " FROM "  + Utilidades.TABLA_PRODUCTOS +
                            " WHERE " + Utilidades.CAMPO_CODIGO_BARRA + " =? AND "
                                        + Utilidades.CAMPO_DESCRIP + "=? AND "
                                        + Utilidades.CAMPO_CATEGORIA + "=? AND "
                                        + Utilidades.CAMPO_UBICACION + "=? "
                    , parametros, null);
            //mostrar la cantidad
            fila.moveToFirst();
            txSumaCantidad.setText(fila.getString(0));
        }catch (Exception ex){
            // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
