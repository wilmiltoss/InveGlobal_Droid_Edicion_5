package com.example.wmiltos.inveglobal_droid.Deposito;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.Deposito.Tablas.UbicacionesSite;
import com.example.wmiltos.inveglobal_droid.PruebasBD.LimpiarActivity;
import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV_Objetos.Locales;
import com.example.wmiltos.inveglobal_droid.principal.login.LoginActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class UbicacionDeposito extends AppCompatActivity {

    private Button btnbarra, btnHecho;
    TextView tvIdLocal,tvDescripLocal , tvUbicacion,tvCoddManipulacion;

    //variables previas del adaptador y la tablaCSV
    List<Locales> listaLocales = new ArrayList<>();
    List<UbicacionesSite> listaUbicacionesSites = new ArrayList<>();
    ConexionSQLiteHelper conn;
    Spinner comboLocales;
    ArrayList<String>listaLocalesSpinner;
    ArrayList<Locales>localLista;
    BottomNavigationView navigation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion_deposito);

        try {
        variables();

        crearTablas();
        pedirPermisos();
        consultarListaLocales();
        recepcionConfiguracionSoporteActivity();
        validacionHabiltarBoton();

        ArrayAdapter<CharSequence> adaptadorLocales = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaLocalesSpinner);
        comboLocales.setAdapter(adaptadorLocales);

        comboLocales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int posicion, long l) {
                //accede al elemento seleccionado
                tvIdLocal.setText(localLista.get(posicion).getCodigo());
                tvDescripLocal.setText(localLista.get(posicion).getDescripcion());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });




        btnbarra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(UbicacionDeposito.this, CapturaUbicacion.class);
                    startActivity(intent);

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "error al ir a la sgte ventana", Toast.LENGTH_SHORT).show();
                }
            }
        });

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
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
                    Intent intent2 = new Intent(UbicacionDeposito.this, GenerarArchivoDeposito.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_notifications:
                    Intent intent3 = new Intent(UbicacionDeposito.this, LimpiarDepo.class);
                    startActivity(intent3);
                    return true;
            }
            return false;
        }
    };


    private void consultarListaLocales() {

        try {
            SQLiteDatabase db = conn.getReadableDatabase();
            Locales locales = null;
            localLista = new ArrayList<>();
            Cursor cursor = db.rawQuery(Utilidades.CONSULTA_TABLA_LOCALES, null);
            while (cursor.moveToNext()) {
                locales = new Locales();
                locales.setCodigo(cursor.getString(0));
                locales.setDescripcion(cursor.getString(1));

                Log.i("id", locales.getCodigo());
                Log.i("descripcion", locales.getDescripcion());
                localLista.add(locales);
            }
            //obtener la lista del array
            listaLocalesSpinner = new ArrayList<>();
            for (int i = 0; i < localLista.size(); i++) {
                listaLocalesSpinner.add(localLista.get(i).getCodigo() + " - " + localLista.get(i).getDescripcion());
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error de consulta Lista Locales" +e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void variables () {
        conn = new ConexionSQLiteHelper(getApplicationContext(), "InveStock.sqlite", null, 1);
        comboLocales = findViewById(R.id.sp_local);

        btnHecho =  findViewById(R.id.btn_hecho);
        btnbarra =  findViewById(R.id.btn_barra);

        tvIdLocal = findViewById(R.id.tv_id_local);
        tvDescripLocal = findViewById(R.id.textView_desc_local);
        tvUbicacion = findViewById(R.id.tv_descrip_ubicacion);
        tvCoddManipulacion = findViewById(R.id.tv_cod_manipulacion);

        navigation = findViewById(R.id.navigation_deposito);
        navigation.setOnNavigationItemSelectedListener(menuNavegacion);

    }
    public void pedirPermisos() {
        // PERMISOS PARA ANDROID 6 O SUPERIOR
        if(ContextCompat.checkSelfPermission(UbicacionDeposito.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(UbicacionDeposito.this, new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }
    }



    public void onClickHecho(View view) {
        //envio de datos a LecturaDeposito
        Intent miIntent = null;
        switch (view.getId()) {
            case R.id.btn_hecho:
                String msjDescLocal = tvDescripLocal.getText().toString();
                String msjUbicacion = tvUbicacion.getText().toString();
                String msjCodManipulacion = tvCoddManipulacion.getText().toString();

                miIntent = new Intent(UbicacionDeposito.this, LecturaDeposito.class);
                Bundle miBundle = new Bundle();
                miBundle.putString("msjDescLocal", tvDescripLocal.getText().toString());
                miBundle.putString("msjUbicacion", tvUbicacion.getText().toString());
                miBundle.putString("msjCodManipulacion", tvCoddManipulacion.getText().toString());

                miIntent.putExtras(miBundle);
                startActivity(miIntent);//abre la sgte ventana
                UbicacionDeposito.this.finish();//finaliza la ventana anterior

                break;
        }

    }


    public void dialogo(){
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(UbicacionDeposito.this);
        dialogo.setMessage("Desea salir del sistema?").setTitle("InveGlobal")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(UbicacionDeposito.this, LoginActivity.class);
                startActivity(intent);
                UbicacionDeposito.this.finish();//finaliza la ventana

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


    //recibe los datos cargado en los campos de ConfiguracionSoporteActivity
    private void recepcionConfiguracionSoporteActivity() {
        Bundle miBundle = this.getIntent().getExtras();
        if (miBundle != null) {
            String stv1 = miBundle.getString("msjcodigoSoporte");//guardamos en una variable los valores
            String stv2 = miBundle.getString("msjcodigoManipulacion");//guardamos en una variable los valores
            tvUbicacion.setText(stv1);// lo mostramos en un textView
            tvCoddManipulacion.setText(stv2);// lo mostramos en un textView
            validacionHabiltarBoton();
        }

    }

    private void validacionHabiltarBoton() {
        String nroInventario = tvUbicacion.getText().toString();
        if (nroInventario.equals("")) {
            //bloqueamos botones y cambiamos de color
            btnHecho.setEnabled(false);
            btnHecho.setBackgroundColor(0xFFE2E2E2);
            btnHecho.setTextColor(Color.parseColor("#FFFFFF"));

        } else {
            btnHecho.setEnabled(true);

        }
    }

    public void crearTablas(){
        try {
            SQLiteDatabase db = conn.getReadableDatabase();
            // 1ro comprueba si existe las tablas
            if (isTableExists("DEPOSITO") && isTableExists("LOCALES")&& isTableExists("CODIGO_SITE")){//si existe
                cargarTablas();
                //Toast.makeText(getApplicationContext(), "Cargando tablas - Aguarde un momento", Toast.LENGTH_SHORT).show();
            }else {//sino existe crearla
                db.execSQL(Utilidades.CREAR_TABLA_LOCALES);
                db.execSQL(Utilidades.CREAR_TABLA_DEPOSITO);
                db.execSQL(Utilidades.CREAR_TABLA_CODIGO_SITE);

                Toast.makeText(getApplicationContext(), "tablas creadas correctamente", Toast.LENGTH_SHORT).show();
                //despues de crear, cargar las tablas
                Toast.makeText(getApplicationContext(), "Cargando tablas - Aguarde un momento", Toast.LENGTH_SHORT).show();
                cargarTablas();
            }
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Error al crear las tablas"+ex.toString(), Toast.LENGTH_SHORT).show();
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


    //DE LA CARGA DE LAS TABLAS
    public void cargarTablas(){
        try {
            SQLiteDatabase db = conn.getReadableDatabase();
            Cursor _locales = db.rawQuery(Utilidades.CONSULTA_TABLA_LOCALES, null);//consulta si existe, luego
            Cursor _ubicaciones_site = db.rawQuery(Utilidades.CONSULTA_TABLA_SITE, null);
            if (_locales.getCount() < 1 & _ubicaciones_site.getCount() < 1)   //2do comprueba si la tabla esta cargada
            {//sino importar datos del csv
                //cargar las tablas directo
                importarCSV_locales();
                //importarCSV_ubicaciones_site();

            } else {
                //Toast.makeText(getApplicationContext(), "No se pudo cargar las tablas", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error al cargar las tablas" +e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void importarCSV_ubicaciones_site() {
        File carpeta = new File(Environment.getExternalStorageDirectory() + "/Download");
        String archivoAgenda = carpeta.toString() + "/" + "planilla_ubicaciones.csv";
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

                    registro.put("SITE", arreglo[0]);
                    registro.put("AREA", arreglo[1]);
                    registro.put("GRUPO", arreglo[2]);
                    registro.put("CODIGO_UBICACION", arreglo[3]);
                    registro.put("ALTO", arreglo[4]);
                    registro.put("ANCHO", arreglo[5]);
                    registro.put("PROF", arreglo[6]);
                    registro.put("VOLUMEN", arreglo[7]);
                    registro.put("VOL_LIBRE", arreglo[8]);
                    registro.put("MP", arreglo[9]);
                    registro.put("MV", arreglo[10]);
                    registro.put("ML", arreglo[11]);
                    registro.put("SEC_PICKING", arreglo[12]);
                    registro.put("VACIA", arreglo[13]);

                    listaUbicacionesSites.add(
                            new UbicacionesSite(
                                    arreglo[0],
                                    arreglo[1],
                                    arreglo[2],
                                    arreglo[3],
                                    arreglo[4],
                                    arreglo[5],
                                    arreglo[6],
                                    arreglo[7],
                                    arreglo[8],
                                    arreglo[9],
                                    arreglo[10],
                                    arreglo[11],
                                    arreglo[12],
                                    arreglo[13]

                            )
                    );
                    // los inserto en la base de datos
                    db.insert("CODIGO_SITE", "1", registro);
                    db.close();
                }
                Toast.makeText(UbicacionDeposito.this, "Tabla Planilla ubicaciones cargada", Toast.LENGTH_SHORT).show();
            } catch(Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void importarCSV_locales() {
        File carpeta = new File(Environment.getExternalStorageDirectory() + "/Download");
        String archivoAgenda = carpeta.toString() + "/" + "locales.csv";
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

                    registro.put("CODIGO", arreglo[0]);
                    registro.put("DESCRIPCION", arreglo[1]);


                    listaLocales.add(
                            new Locales(
                                    arreglo[0],
                                    arreglo[1],
                                    arreglo[2]
                            )
                    );
                    // los inserto en la base de datos
                    db.insert("LOCALES", "1", registro);
                    db.close();
                }
                Toast.makeText(UbicacionDeposito.this, "Tabla Locales cargada", Toast.LENGTH_SHORT).show();
            } catch(Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }


}
