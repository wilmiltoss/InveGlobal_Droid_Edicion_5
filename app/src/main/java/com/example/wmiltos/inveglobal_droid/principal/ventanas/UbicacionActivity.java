package com.example.wmiltos.inveglobal_droid.principal.ventanas;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.dialogo.DialogoBarraSector;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Locacion;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Soportes;
import com.example.wmiltos.inveglobal_droid.iTrack.ConfiguracionActivity;
import com.example.wmiltos.inveglobal_droid.iTrack.QuiebreActivity;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.ConfiguracionSoporteActivity;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.Limpiar2Activity;
import com.example.wmiltos.inveglobal_droid.principal.login.LoginActivity;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.RedActivity;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.VisualizarRegistroActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.util.ArrayList;

public class UbicacionActivity extends AppCompatActivity implements View.OnClickListener,DialogoBarraSector.DialogoBarraSectorListener {

    private TextView mTextMessage;
    private Button btnbarra, btnCerrar;

    TextView  campoLocacionId, campoLocacionDescripcion, campoIdSoporte, campoDescripSoporte;

    TextView txLocacion, txDescripcion, txSoporte, txDescripcionSoporte, txnombreEquipo, txtUsuario,
            campoConteo,campoMetro, campoNivel, campoNroSoporte, tvIdInventario,tvBarraSector;
    //tv tabla INVENTARIO_SOPORTE
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tvConteo1,tvConteo2,tvConteo3;

    Spinner comboLocacion;
    Spinner comboSoporte;
    ArrayList<String> listaLocacion;
    ArrayList<Locacion> locacionList;
    ArrayList<String> listaSoporte;
    ArrayList<Soportes> soporteList;
    ConexionSQLiteHelper conn;
    private Cursor fila;
    Button btnHecho, btnConteo,btnMetro,btnNivel, btnNroSoporte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        //Icono en el action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        variables();

        consultarListaLocacion();
        consultarListaSoporte();
        recepcionDatosUsuario();
        verIdInventario();
        recepcionConfiguracionSoporteActivity();

       // datosScanner();
        //botones de NumberPicker
        btnMetro =  findViewById(R.id.btn_Metro);
        btnMetro.setOnClickListener(this);

        btnConteo =  findViewById(R.id.btn_conteo);
        btnConteo.setOnClickListener(this);

        btnNivel =  findViewById(R.id.btn_nivel);
        btnNivel.setOnClickListener(this);

        btnNroSoporte = findViewById(R.id.btn_nroSoporte);
        btnNroSoporte.setOnClickListener(this);

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaLocacion);
        ArrayAdapter<CharSequence> adaptadorSp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaSoporte);
        //adaptador de los spinner
        comboLocacion.setAdapter(adaptador);
        comboSoporte.setAdapter(adaptadorSp);
            //captura de datos del spinner
        comboLocacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //accede al elemento seleccionado
                txLocacion.setText(locacionList.get(position).getId_locacion().toString());
                txDescripcion.setText(locacionList.get(position).getDescripcion().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        comboSoporte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //guarda en la tx el items seleccionado en la lista desplegable
                txSoporte.setText(soporteList.get(position).getId_soporte().toString());
                txDescripcionSoporte.setText(soporteList.get(position).getDescripcion().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //Al pulsar el boton de la barra abre el dialogo
        btnbarra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    envioUsuario();
                //Intent intent = new Intent(UbicacionActivity.this, ConfiguracionSoporteActivity.class);
                //startActivity(intent);

            }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "error al ir a la sgte ventana", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    //IMPORTANTE= inflater de la barra superior menu para que funcione la grilla
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu); //apuntamos al xml menu_principal para q aparezca
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.navigation_home:
                dialogo();
                return true;
            case R.id.quiebre:compruebaRegistroConfiguracion();

                return true;
/*                Intent intent3 = new Intent(UbicacionActivity.this, Limpiar2Activity.class);
                startActivity(intent3);
                return true;*/
            //case R.id.quiebre3:

                //return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //boton fisico atraz
    @Override
    public void onBackPressed() {
        UbicacionActivity.this.finish();
    }

    //setea el items del spinner de lo que viene de la configuracion
    public void seleccionItemsSpinner (){
        try {
            int stt3 = 0;
            int stt7 = 0;
            Bundle miBundle = this.getIntent().getExtras();
            String stv3 = miBundle.getString("msjtv3");
            String stv7 = miBundle.getString("msjtv7");
            stt3 = Integer.parseInt(stv3);
            comboSoporte.setSelection(stt3-1);//selecciona el nro de soporte para mostrarlo en el spinner

            stt7 = Integer.parseInt(stv7);
            comboLocacion.setSelection(stt7-1);

        } catch (Exception e){
            //Toast.makeText(getApplicationContext(), "error al obtener spinner", Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirDialogoClave() {

        DialogoBarraSector dialogoBarra = new DialogoBarraSector();
        dialogoBarra.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void applyTexts(String barraSector) {//A1
        tvBarraSector.setText(barraSector);

    }

    private void variables() {
        //conexion
        conn = new ConexionSQLiteHelper(getApplicationContext(), "InveStock.sqlite", null, 1);

        btnHecho =  findViewById(R.id.btn_hecho);
        btnbarra =  findViewById(R.id.btn_barra);
        btnCerrar =  findViewById(R.id.btn_cerrar);

        campoConteo =  findViewById(R.id.tv_conteo);//1
        campoNivel = (TextView) findViewById(R.id.tv_nivel);//4
        campoMetro = (TextView) findViewById(R.id.tv_metro);//
        campoNroSoporte = (TextView) findViewById(R.id.tv_nro_soporte);

        comboLocacion = (Spinner) findViewById(R.id.sp_locacion);
        comboSoporte = (Spinner) findViewById(R.id.sp_tipoSoporte);
        txnombreEquipo = (TextView) findViewById(R.id.text_nombreEquipoL);
        txtUsuario = (TextView) findViewById(R.id.text_usuario);
        tvIdInventario = (TextView) findViewById(R.id.tv_IdInventario);

        //variables de lecturas de los txt capturados de los Spinner
        txLocacion = (TextView) findViewById(R.id.txtIdlocacion);//2
        txDescripcion = (TextView) findViewById(R.id.txtDescripcionloca);
        txSoporte = (TextView) findViewById(R.id.txtIdSoporte);//3
        txDescripcionSoporte = (TextView) findViewById(R.id.txtDescripSoporte);

        //de los spinner
        campoLocacionId = (TextView) findViewById(R.id.txtIdlocacion);
        campoLocacionDescripcion = (TextView) findViewById(R.id.txtDescripcionloca);
        campoIdSoporte = (TextView) findViewById(R.id.txtIdSoporte);
        campoDescripSoporte = (TextView) findViewById(R.id.txtDescripSoporte);


        //dialogo barra
        tvBarraSector = (TextView) findViewById(R.id.tv_barraSector);

        //prueba de consulta
        tv1 = (TextView) findViewById(R.id.tv_1);
        tv2 = (TextView) findViewById(R.id.tv_2);
        tv3 = (TextView) findViewById(R.id.tv_3);
        tv4 = (TextView) findViewById(R.id.tv_4);
        tv5 = (TextView) findViewById(R.id.tv_5);
        tv6 = (TextView) findViewById(R.id.tv_6);
        tv7 = (TextView) findViewById(R.id.tv_7);

        tvConteo1 = (TextView) findViewById(R.id.tv_conteo1);
        tvConteo2 = (TextView) findViewById(R.id.tv_conteo2);
        tvConteo3 = (TextView) findViewById(R.id.tv_conteo3);

    }

    //spinner Locacion
    private void consultarListaLocacion() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Locacion locales = null;
        locacionList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_LOCACION, null);

        while (cursor.moveToNext()) {
            locales = new Locacion();
            locales.setId_locacion(cursor.getInt(0));
            locales.setDescripcion(cursor.getString(1));

            Log.i("id", locales.getId_locacion().toString());
            Log.i("nombre", locales.getDescripcion());

            locacionList.add(locales);
        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaLocacion = new ArrayList<String>();
        for (int i = 0; i < locacionList.size(); i++) {
            listaLocacion.add(locacionList.get(i).getId_locacion() + " - " + locacionList.get(i).getDescripcion());
        }
    }
    //spinner tipo soporte
        private void consultarListaSoporte() {
            SQLiteDatabase db = conn.getReadableDatabase();
            Soportes soporte = null;
            soporteList = new ArrayList<Soportes>();
            Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_SOPORTE, null);

            while (cursor.moveToNext()) {
                soporte = new Soportes();
                soporte.setId_soporte(cursor.getInt(0));
                soporte.setDescripcion(cursor.getString(1));
                soporte.setSubdivisible(cursor.getInt(2));

                Log.i("id", soporte.getId_soporte().toString());
                Log.i("descripcion", soporte.getDescripcion());
                Log.i("subdivisible", soporte.getSubdivisible().toString());

                soporteList.add(soporte);

            }
            obtenerListaSoporte();
        }

    private void obtenerListaSoporte() {
        listaSoporte = new ArrayList<String>();

        for (int i = 0; i < soporteList.size(); i++) {
            listaSoporte.add(soporteList.get(i).getId_soporte() + " - " + soporteList.get(i).getDescripcion());
        }
    }

    //capturar los datos de los campos EditText para envio a LecturasActivity
    public void onClickHecho(View view) {
        Intent miIntent = null;
        switch (view.getId()) {
            case R.id.btn_hecho:
                String msjConteo = campoConteo.getText().toString();
                String msjMetro = campoMetro.getText().toString();
                String msjNivel = campoNivel.getText().toString();
                String msjLocacionId = campoLocacionId.getText().toString();
                String msjLocacionDescrip = campoLocacionDescripcion.getText().toString();
                String msjIdSoporte = campoIdSoporte.getText().toString();
                String msjDescripSoporte = campoDescripSoporte.getText().toString();
                String msjNroSoporte = campoNroSoporte.getText().toString();
                String msjUsuario = txtUsuario.getText().toString();
                String msjClaveInv = tv3.getText().toString();//clave inventario

                if (msjClaveInv == ""){
                    Toast.makeText(getApplicationContext(), "Aviso: No ha ingresado ninguna Clave Inventario", Toast.LENGTH_SHORT).show();
                }

                miIntent = new Intent(UbicacionActivity.this, LecturasActivity.class);
                Bundle miBundle = new Bundle();
                miBundle.putString("msjConteo", campoConteo.getText().toString());
                miBundle.putString("msjMetro", campoMetro.getText().toString());
                miBundle.putString("msjNivel", campoNivel.getText().toString());
                miBundle.putString("msjLocacionId", campoLocacionId.getText().toString());
                miBundle.putString("msjLocacionDescrip", campoLocacionDescripcion.getText().toString());
                miBundle.putString("msjIdSoporte", campoIdSoporte.getText().toString());
                miBundle.putString("msjDescripSoporte", campoDescripSoporte.getText().toString());
                miBundle.putString("msjNroSoporte", campoNroSoporte.getText().toString());
                miBundle.putString("msjUsuario", txtUsuario.getText().toString());
                miBundle.putString("msjClaveInv", tv2.getText().toString());

                miIntent.putExtras(miBundle);
                startActivity(miIntent);//abre la sgte ventana
               // UbicacionActivity.this.finish();//finaliza la ventana anterior

                break;
        }
    }

    //recibe los datos cargado en los campos de ConfiguracionSoporteActivity
    private void recepcionConfiguracionSoporteActivity() {
        Bundle miBundle = this.getIntent().getExtras();
        if (miBundle != null) {

            String stv1 = miBundle.getString("msjtv1");//guardamos en una variable los valores
            tv1.setText(stv1);// lo mostramos en un textView

            String stv2 = miBundle.getString("msjtv2");
            tv2.setText(stv2);

            String stv3 = miBundle.getString("msjtv3");
            tv3.setText(stv3);

            String stv4 = miBundle.getString("msjtv4");
            campoNroSoporte.setText(stv4);

            String stv5 = miBundle.getString("msjtv5");
            tv5.setText(stv5);

            String stv6 = miBundle.getString("msjtv6");
            tv6.setText(stv6);

            String stv7 = miBundle.getString("msjtv7");
            tv7.setText(stv7);
            //Valores del conteo
            String cont1 = miBundle.getString("msjcont1");
            tvConteo1.setText(cont1);
            String cont2 = miBundle.getString("msjcont2");
            tvConteo2.setText(cont2);
            String cont3 = miBundle.getString("msjcont3");
            tvConteo3.setText(cont3);

            validacionHabiltarBoton();

           //trae el user de la ventana ConfiguracionSoporteActivity si es que se carga, sino mantiene p/ continuar en LecturaActivity
            if (tv1.getText().toString() != "")
            {
                String stmsjUsuConfig = miBundle.getString("msjUsuConfig");
                txtUsuario.setText(stmsjUsuConfig);
                //bloqueamos controles
                comboLocacion.setEnabled(false);
                comboSoporte.setEnabled(false);
                //campoNroSoporte.setEnabled(false);

            }

            //CAMPO_ID_INVENTARIO_SOPORTE tv1
            //CAMPO_ID_CLAVE              tv2
            // CAMPO_ID_SOPORTE_IS         tv3
            //CAMPO_NRO_SOPORTE           tv4
            //CAMPO_NRO_TIPO_SOPORTE      tv5
            //CAMPO_DESCRIPCION_IS        tv6
        }
        try {
            //validacion no mostrar dialogo al iniciar si el campo tv1  si esta vacio, si mostrar al cargar idsoporteinventario
            if (tv1.getText().toString() != "") {
                dialogoSpinner();
            }

        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "error dialogo", Toast.LENGTH_SHORT).show();
        }
    }
    //validamos los botones hecho y cerrar inventario si poseen claves ingresadas
    public void validacionHabiltarBoton(){
        String conteo1 = tvConteo1.getText().toString();
        String conteo2 = tvConteo2.getText().toString();
        String conteo3 = tvConteo3.getText().toString();

        //Evaluamos los textview; validacion de los estados de los conteos, para habilitar botones
        //si hay datos de conteo habilitamos botones
        if (conteo1.equals("0") || conteo1.equals("1") || conteo1.equals("2")) {
            //Toast.makeText(getApplicationContext(), "habilitamos boton"+conteo1, Toast.LENGTH_SHORT).show();
            btnHecho.setEnabled(true);
            btnCerrar.setEnabled(true);


        }else if (conteo2.equals("0") || conteo2.equals("1") || conteo2.equals("2")) {
            //Toast.makeText(getApplicationContext(), "habilitamos boton"+conteo1, Toast.LENGTH_SHORT).show();
            btnHecho.setEnabled(true);
            btnCerrar.setEnabled(true);


        }else if (conteo3.equals("0") || conteo3.equals("1") || conteo3.equals("2")){
           // Toast.makeText(getApplicationContext(), "habilitamos boton"+conteo1, Toast.LENGTH_SHORT).show();
            btnHecho.setEnabled(true);
            btnCerrar.setEnabled(true);

        } else {
            //bloqueamos botones y cambiamos de color
            btnHecho.setEnabled(false);
            btnHecho.setBackgroundColor(0xFFE2E2E2);
            btnHecho.setTextColor(Color.parseColor("#FFFFFF"));

            btnCerrar.setEnabled(false);
            btnCerrar.setTextColor(Color.parseColor("#FFFFFF"));
            btnCerrar.setBackgroundColor(0xFFE2E2E2);


           // Toast.makeText(getApplicationContext(), "boton no habilitado", Toast.LENGTH_SHORT).show();
        }
    }

    private void dialogoSpinner() {
        Bundle miBundle = this.getIntent().getExtras();
        String concatenar = miBundle.getString("msjtv6");
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(UbicacionActivity.this);
        dialogo.setMessage("Modificar a: "+concatenar).setTitle("InveGlobal")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                seleccionItemsSpinner();
                btnNroSoporte.setEnabled(false);//bloqueamos el nro de soporte
            }
        });
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   dialogo();
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent2 = new Intent(UbicacionActivity.this, VisualizarRegistroActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_notifications:
                    Intent intent3 = new Intent(UbicacionActivity.this, Limpiar2Activity.class);
                    startActivity(intent3);
                    return true;
                case R.id.navigation_red:
                    Intent intent4 = new Intent(UbicacionActivity.this, RedActivity.class);
                    startActivity(intent4);
                    return true;
            }
            return false;
        }
    };

    public void dialogo(){
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(UbicacionActivity.this);
        dialogo.setMessage("Desea salir del sistema?").setTitle("InveGlobal")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(UbicacionActivity.this, LoginActivity.class);
                startActivity(intent);
                UbicacionActivity.this.finish();//finaliza la ventana

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


    private void recepcionDatosUsuario() {
        //recibe los datos cargado en los campos de Login
        Bundle miBundle = this.getIntent().getExtras();
        if (miBundle != null) {
            String usuario = miBundle.getString("msjUsuario");
            txtUsuario.setText(usuario);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_conteo:numberPickerDialogoConteo();
                break;
            case R.id.btn_Metro:numberPickerDialogoMetro();
                break;
            case R.id.btn_nivel:numberPickerDialogoNivel();
                break;
            case R.id.btn_nroSoporte:numberPickerDialogoNroSoporte();
                break;
        }
    }

    //numberPickerDialogo de Conteo
    private void numberPickerDialogoConteo(){
        NumberPicker myNumberPickerConteo = new NumberPicker(this);
        myNumberPickerConteo.setMaxValue(1);
        myNumberPickerConteo.setMinValue(1);
        NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                campoConteo.setText(""+newVal);
            }
        };
        myNumberPickerConteo.setOnValueChangedListener(myValChangedListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(myNumberPickerConteo);
        builder.setTitle("Conteo")
                .setIcon(R.drawable.alert_dialogo);
        //botones del alertdialogos
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
    //numberPickerDialogo de Metro
      private void numberPickerDialogoMetro(){

        NumberPicker myNumberPickerMetro = new NumberPicker(this);
        myNumberPickerMetro.setMaxValue(50);
        myNumberPickerMetro.setMinValue(1);
        NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                campoMetro.setText(""+newVal);
            }
        };
        myNumberPickerMetro.setOnValueChangedListener(myValChangedListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(myNumberPickerMetro);
        builder.setTitle("Metro")
                .setIcon(R.drawable.alert_dialogo);
        //botones del alertdialogos
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    //numberPickerDialogo de Nivel
    private void numberPickerDialogoNivel(){
        NumberPicker myNumberPickerNivel = new NumberPicker(this);
        myNumberPickerNivel.setMaxValue(30);
        myNumberPickerNivel.setMinValue(1);
        NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                campoNivel.setText(""+newVal);
            }
        };
        myNumberPickerNivel.setOnValueChangedListener(myValChangedListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(myNumberPickerNivel);
        builder.setTitle("Nivel")
                .setIcon(R.drawable.alert_dialogo);
        //botones del alertdialogos
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    //numberPickerDialogo de NroSoporte
    private void numberPickerDialogoNroSoporte(){
        NumberPicker myNumberPickerNroSoporte = new NumberPicker(this);
        myNumberPickerNroSoporte.setMaxValue(150);
        myNumberPickerNroSoporte.setMinValue(1);
        NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                campoNroSoporte.setText(""+newVal);
            }
        };
        myNumberPickerNroSoporte.setOnValueChangedListener(myValChangedListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(myNumberPickerNroSoporte);
        builder.setTitle("Nro.")
                .setIcon(R.drawable.alert_dialogo);
        //botones del alertdialogos
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    public void verIdInventario (){
        SQLiteDatabase db = conn.getWritableDatabase();
        try {
            String[] campos = new String[] {Utilidades.CAMPO_ID_INVENTARIO_IS};
            String[] args = new String[] {"1"};
            Cursor c = db.query(Utilidades.TABLA_INVENTARIO_SOPORTE, campos, null, null, null, null, null);
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String ID_INVENTARIO = c.getString(0);
                    tvIdInventario.setText(ID_INVENTARIO);
                } while(c.moveToNext());
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error al consultar IdInventario", Toast.LENGTH_SHORT).show();
        }
    }

    public void envioUsuario(){
        Intent miIntent = null;
        String msjUsuario = txtUsuario.getText().toString();
        String msjNroSoporteTv = campoConteo.getText().toString();
        miIntent = new Intent(UbicacionActivity.this, ConfiguracionSoporteActivity.class);

        Bundle miBundle = new Bundle();
        miBundle.putString("msjUsuario", txtUsuario.getText().toString());
        miBundle.putString("msjNroSoporteTv", campoConteo.getText().toString());
        miIntent.putExtras(miBundle);
        startActivity(miIntent);
    }

    public void envioUsuarioQuiebre(){
        Intent miIntent = null;
        String msjUsuario = txtUsuario.getText().toString();

        miIntent = new Intent(UbicacionActivity.this, ConfiguracionActivity.class);
        Bundle miBundle = new Bundle();
        miBundle.putString("msjUsuario", txtUsuario.getText().toString());
        miIntent.putExtras(miBundle);
        startActivity(miIntent);
    }

    public void onClickCerrar(View view) {

        dialogoCerrar();

    }

    private void dialogoCerrar() {
        String descUbicacion =  tv6.getText().toString();
        final String nroConteo   = campoConteo.getText().toString();
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(UbicacionActivity.this);
        dialogo.setMessage("Desea cerrar la ubicación? "+descUbicacion +", del conteo "+nroConteo).setTitle("Cierre de Ubicación")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String valorConteo1 = tvConteo1.getText().toString();
                String valorConteo2 = tvConteo2.getText().toString();
                String valorConteo3 = tvConteo3.getText().toString();
                //Segun el nro de conteo que figura en el texview evaluamos el valor del conteo y lo validamos
                if(nroConteo.equals("1") ){
                    validacionCampoConteo(valorConteo1);
                }else if (nroConteo.equals("2") ){
                    validacionCampoConteo(valorConteo2);
                } else if (nroConteo.equals("3") ) {
                    validacionCampoConteo(valorConteo3);
                }
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
    public void validacionCampoConteo(String valorConteo){
        if (Integer.parseInt(valorConteo)  == 0){
            Toast.makeText(getApplicationContext(), "No se puede cerrar el conteo porque no está abierto", Toast.LENGTH_LONG).show();
            //bloqueamos botones
            btnCerrar.setEnabled(false);
            btnCerrar.setTextColor(Color.parseColor("#FFFFFF"));
            btnCerrar.setBackgroundColor(0xFFE2E2E2);

        }else if (Integer.parseInt(valorConteo)  == 2 ) {
            Toast.makeText(getApplicationContext(), "El conteo ya esta cerrado", Toast.LENGTH_LONG).show();
            //bloqueamos botones
            btnCerrar.setEnabled(false);
            btnCerrar.setTextColor(Color.parseColor("#FFFFFF"));
            btnCerrar.setBackgroundColor(0xFFE2E2E2);

        }else{
            //convertimos la variable campo a int p/ enviarlo a seleccionarConteo, la variable es extraida del texView campoConteo
            int campo = Integer.parseInt(campoConteo.getText().toString());
            cerrarConteo(campo);//cargamos el valor campo
            //bloqueamos los botones cerrar y hecho despues de cerrar
            btnCerrar.setEnabled(false);
            btnCerrar.setTextColor(Color.parseColor("#FFFFFF"));
            btnCerrar.setBackgroundColor(0xFFE2E2E2);

            btnHecho.setEnabled(false);
            btnHecho.setBackgroundColor(0xFFE2E2E2);
            btnHecho.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    //invoca a actualizarConteoEnProceso de acuerdo al valor de texView conteo obtenido mas arriba
    public void cerrarConteo(int nroConteo) {
        //Actualiza los campos de acuerdo al nro de conteo
        try{
            if (nroConteo == 1){
                actualizarConteoCerrar("CONTEO1");//CONTEO1 envia a actualizarConteoCerrar(String CONTEO)
            }else if (nroConteo == 2){
                actualizarConteoCerrar("CONTEO2");
            }else if (nroConteo == 3){
                actualizarConteoCerrar("CONTEO3");
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al actualizar nro de conteo", Toast.LENGTH_LONG).show();
        }
    }
    //cerramos el conteo de acuerdo al parametro nro de CONTEO cargado
    public void actualizarConteoCerrar(String CONTEO){
        String descUbicacion =  tv6.getText().toString();
        SQLiteDatabase db = conn.getWritableDatabase();
        String claveSoporte = tv2.getText().toString();//ID_CLAVE
        try {
            //actualiza los campos a 2 solo si CONTEO es igual a 1
            String update = "UPDATE " + Utilidades.TABLA_INVENTARIO_SOPORTE + " SET " + CONTEO + " = 2 " +
                    "WHERE " + Utilidades.CAMPO_ID_CLAVE + "= " +claveSoporte + " AND "+ CONTEO + " = 1 ";
            db.execSQL(update);
            db.close();
            //"Id Registrado" es el mensaje que envia al insertar
            Toast.makeText(getApplicationContext(), "Cerrando ubicación "+ descUbicacion+ " del "+ CONTEO, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            abrirDialogoClave();//dialogo en caso de que no tenga clave inventario
            Toast.makeText(getApplicationContext(), "Error al intentar cerrar el conteo" , Toast.LENGTH_SHORT).show();
        }
    }

    //Comprueba si existe registros en la bd con las configuraciones iniciales, salta en caso de que ya tenga la configuracion ITRACK
    public void compruebaRegistroConfiguracion(){
        try {
            if (isTableExists("PRODUCTOS")) {
                SQLiteDatabase db = conn.getReadableDatabase();
                Cursor _productos = db.rawQuery(Utilidades.CONSULTA_TABLA_PRODUCTOS, null);//consulta si existe, luego
                if (_productos.getCount() > 0)   //2do comprueba si la tabla esta cargada
                {//si ya tiene registros salta a la ventana Quiebrs
                    Toast.makeText(getApplicationContext(), "Aviso: Finalice la carga para volver a configurar los parametros", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UbicacionActivity.this, QuiebreActivity.class);
                    startActivity(intent);
                } else {//si no pasa a la ventana ConfiguracionActivity
                    Toast.makeText(getApplicationContext(), "Establezca las configuraciones iniciales", Toast.LENGTH_SHORT).show();
                    envioUsuarioQuiebre();
                }
            }else {//sino pasa a la sgte ventana Configuracion p/ la 1ra carga de la tabla
                Intent intent = new Intent(UbicacionActivity.this, ConfiguracionActivity.class);
                startActivity(intent);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    //comprueba si existe la tabla en la bd sqlite
    public boolean isTableExists(String nombreTabla) {
        SQLiteDatabase db = conn.getReadableDatabase();
        boolean isExist = false;
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + nombreTabla + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                isExist = true;
                // Toast.makeText(getApplicationContext(), "la tabla existe", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "la/s tabla/s no existe/n", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }
        return isExist;
    }



    //++++++++++++++++++++++++ PRUEBAS NO SE USA++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //Carga de datos segun clave scaneada
    public void datosScanner() {
        SQLiteDatabase db = conn.getWritableDatabase();
        try {
            String[] campos = new String[] {Utilidades.CAMPO_ID_INVENTARIO_SOPORTE,
                    Utilidades.CAMPO_ID_CLAVE,
                    Utilidades.CAMPO_ID_SOPORTE_IS,
                    Utilidades.CAMPO_NRO_SOPORTE,
                    Utilidades.CAMPO_NRO_TIPO_SOPORTE,
                    Utilidades.CAMPO_DESCRIPCION_IS,
                    Utilidades.CAMPO_CONTEO_1,
                    Utilidades.CAMPO_CONTEO_2,
                    Utilidades.CAMPO_CONTEO_3
            };
            String[] parametro = new String[] {tvBarraSector.getText().toString()};
            Cursor c = db.query(Utilidades.TABLA_INVENTARIO_SOPORTE, campos, "ID_CLAVE=?",parametro , null, null, null);
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {//recorremos todas las filas
                //Recorremos el cursor hasta que no haya más registros
                do {
                    //referenciar la columna en la tabla
                    String columna = c.getString(0);
                    String columna1 = c.getString(1);
                    String columna2 = c.getString(2);
                    String columna3 = c.getString(3);
                    String columna4 = c.getString(4);
                    String columna5 = c.getString(5);
                    String columna6 = c.getString(6);
                    String columna7 = c.getString(7);
                    String columna8 = c.getString(8);

                    //tx a mostrar
                    tv1.setText(columna);
                    tv2.setText(columna1);
                    tv3.setText(columna2);
                    tv4.setText(columna3);
                    tv5.setText(columna4);
                    tv6.setText(columna5);
                    tvConteo1.setText(columna6);
                    tvConteo2.setText(columna7);
                    tvConteo3.setText(columna8);
                    Toast.makeText(getApplicationContext(), "columnas"+columna6+columna6+columna7+columna8, Toast.LENGTH_SHORT).show();

                    //while (tvBarraSector != null){
                    // refrescarVentana();
                    // }
                } while(c.moveToNext());
            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error al consultar inventario_soporte", Toast.LENGTH_SHORT).show();
        }

    }
    public void refrescarVentana(){
        Intent refresh = new Intent(this, UbicacionActivity.class);
        startActivity(refresh);
        this.finish();
    }
    private void registrarDatosUbicacionSQL() {
        //CONEXION

        //ABRIR LA BD PARA EDITARLO
        SQLiteDatabase db = conn.getWritableDatabase();
        //CON SENTENCIA SQL
        String insert = "INSERT INTO " + Utilidades.TABLA_LECTURAS + "("
                + Utilidades.CAMPO_NRO_CONTEO + ","    //1
                + Utilidades.CAMPO_ID_LOCACION_L + "," //2
                + Utilidades.CAMPO_ID_SOPORTE_L + "," //3
                + Utilidades.CAMPO_NIVEL + ","        //4
                + Utilidades.CAMPO_METRO + ")" +       //5
                "VALUES (" + campoConteo.getText().toString() + ", '"
                + txLocacion.getText().toString() + "','"
                + txSoporte.getText().toString() + "','"
                + campoNivel.getText().toString() + "','"
                + campoMetro.getText().toString() + "')";

        //"Id Registrado" es el mensaje que envia al insertar
        Toast.makeText(getApplicationContext(), "Ubicacion Registrada", Toast.LENGTH_SHORT).show();

        db.execSQL(insert);

        db.close();
    }
    public void dialogoBtnCerrar(){
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(UbicacionActivity.this);
        dialogo.setMessage("Desea confirmar cierre de ubicación?").setTitle("InveGlobal")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(UbicacionActivity.this, LoginActivity.class);
                startActivity(intent);
                UbicacionActivity.this.finish();//finaliza la ventana

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
    private void registrarEquipo() {
        //CONEXION
        SQLiteDatabase db = conn.getWritableDatabase();

        String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
        txnombreEquipo.setText(deviceName);

        //CON SENTENCIA SQL
        String insert = "INSERT INTO " + Utilidades.TABLA_LECTURAS + "("
                + Utilidades.CAMPO_ID_LOCACION_L + ")" +
                "VALUES ('" + deviceName + "')";

        db.execSQL(insert);
        db.close();
    }



}
