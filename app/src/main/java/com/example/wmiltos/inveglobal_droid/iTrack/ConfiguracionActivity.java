package com.example.wmiltos.inveglobal_droid.iTrack;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Productos;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Soportes;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Usuarios;
import com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV_Objetos.Cadena;
import com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV_Objetos.Locales;
import com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV_Objetos.Tiponegocio;
import com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV_Objetos.Ubicaciones;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracionActivity extends AppCompatActivity {


    TextView txLocal,txDescripcionLocal, txUbicacion, txDescripUbicacion, txTipoNegocio, txDescripTipoNegocio,
            txCadena,txDescripCadenas,txSoporte,txDescripcionSoporte,tvUsuario;

    List<Locales> listaLocales = new ArrayList<>();//previas configuraciones del adaptador y la tablaCSV
    List<Ubicaciones> listaUbicaciones = new ArrayList<>();
    List<Tiponegocio> listaTiponegocio = new ArrayList<>();
    List<Cadena> listaCadena = new ArrayList<>();
    ConexionSQLiteHelper conn;

    //1************spinner******************
    Spinner comboLocales,comboTipoNegocio,comboCadenas,comboSoporte;
    ArrayList<String>listaLocalesSpinner,listaTipoNegocioSpinner,listaCadenasSpinner,listaSoporte;

    ArrayList<Locales>localLista;
    ArrayList<Soportes> soporteList;
    ArrayList<Tiponegocio>tipoNegocioLista;
    ArrayList<Cadena>cadenaslLista;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        try {
            variables();
            crearTablas3();
            pedirPermisos();
            consultarListaLocales();
            consultarListaSoporte();
            consultarListaCadena();
            consultarListaTipoNegocio();
            recepcionDatosUsuario();
        //compruebaRegistroConfiguracion();


            //2 - creamos los array adaptadores******************************************
            //Adaptadores de secuencias
            ArrayAdapter<CharSequence> adaptadorLocales = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaLocalesSpinner);
            ArrayAdapter<CharSequence> adaptadorSoporte = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaSoporte);
            ArrayAdapter<CharSequence> adaptadorCadena = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaCadenasSpinner);
            ArrayAdapter<CharSequence> adaptadorTipoNegocio = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaTipoNegocioSpinner);

            //3- mostrar en el combo el array adaptador
            comboLocales.setAdapter(adaptadorLocales);
            comboSoporte.setAdapter(adaptadorSoporte);
            comboCadenas.setAdapter(adaptadorCadena);
            comboTipoNegocio.setAdapter(adaptadorTipoNegocio);

            //4- captura de datos del spinner
            comboLocales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int posicion, long l) {
                    //accede al elemento seleccionado
                    txLocal.setText(localLista.get(posicion).getCodigo());
                    txDescripcionLocal.setText(localLista.get(posicion).getDescripcion());

                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            comboSoporte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //guarda en la tx el items seleccionado en la lista desplegable
                    txSoporte.setText(soporteList.get(position).getId_soporte().toString());
                    txDescripcionSoporte.setText(soporteList.get(position).getDescripcion());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            comboCadenas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int posicion, long l) {
                    //accede al elemento seleccionado
                    txCadena.setText(cadenaslLista.get(posicion).getIdcadena());
                    txDescripCadenas.setText(cadenaslLista.get(posicion).getDescrcadena());
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            comboTipoNegocio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int posicion, long l) {
                    //accede al elemento seleccionado
                    txTipoNegocio.setText(tipoNegocioLista.get(posicion).getId());
                    txDescripTipoNegocio.setText(tipoNegocioLista.get(posicion).getDescripcion());
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    //5-spinner Locacion
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
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    //spinner Tipo Soporte
    private void consultarListaSoporte() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Soportes soporte = null;
        soporteList = new ArrayList<>();
        Cursor cursor = db.rawQuery(Utilidades.CONSULTA_TABLA_SOPORTE, null);

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
        listaSoporte = new ArrayList<>();
        for (int i = 0; i < soporteList.size(); i++) {
            listaSoporte.add(soporteList.get(i).getId_soporte() + " - " + soporteList.get(i).getDescripcion());
        }
    }

    //spinner Cadena
    private void consultarListaCadena() {
        try {
            SQLiteDatabase db = conn.getReadableDatabase();
            Cadena cadena = null;
            cadenaslLista = new ArrayList<>();
            Cursor cursor = db.rawQuery(Utilidades.CONSULTA_TABLA_CADENAS, null);
            while (cursor.moveToNext()) {
                cadena = new Cadena();
                cadena.setIdcadena(cursor.getString(0));
                cadena.setDescrcadena(cursor.getString(1));
                cadena.setIdtiponegocio(cursor.getString(2));

                Log.i("id", cadena.getIdcadena().toString());
                Log.i("descripcion", cadena.getDescrcadena());
                Log.i("subdivisible", cadena.getIdtiponegocio().toString());
                cadenaslLista.add(cadena);
            }
            //obtener la lista del array
            listaCadenasSpinner = new ArrayList<>();
            for (int i = 0; i < cadenaslLista.size(); i++) {
                listaCadenasSpinner.add(cadenaslLista.get(i).getIdcadena() + " - " + cadenaslLista.get(i).getDescrcadena());
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //spinner Cadena
    private void consultarListaTipoNegocio() {
        try {
            SQLiteDatabase db = conn.getReadableDatabase();
            Tiponegocio tipoNegocio = null;
            tipoNegocioLista = new ArrayList<>();
            Cursor cursor = db.rawQuery(Utilidades.CONSULTA_TABLA_TIPO_NEGOCIO, null);
            while (cursor.moveToNext()) {
                tipoNegocio = new Tiponegocio();
                tipoNegocio.setId(cursor.getString(0));
                tipoNegocio.setDescripcion(cursor.getString(1));

                Log.i("id", tipoNegocio.getId());
                Log.i("descripcion", tipoNegocio.getDescripcion());
                tipoNegocioLista.add(tipoNegocio);
            }
            //obtener la lista del array
            listaTipoNegocioSpinner = new ArrayList<>();
            for (int i = 0; i < tipoNegocioLista.size(); i++) {
                listaTipoNegocioSpinner.add(tipoNegocioLista.get(i).getId() + " - " + tipoNegocioLista.get(i).getDescripcion());
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void variables() {
        try {
            conn = new ConexionSQLiteHelper(getApplicationContext(), "InveStock.sqlite", null, 1);

            comboLocales = findViewById(R.id.sp_local);
            comboSoporte = findViewById(R.id.sp_tipo_soporte);
            comboTipoNegocio = findViewById(R.id.sp_tipo_negocio);
            comboCadenas = findViewById(R.id.sp_cadena);

            txLocal = findViewById(R.id.textView_local);
            txDescripcionLocal = findViewById(R.id.textView_desc_local);
            txUbicacion = findViewById(R.id.textView_ubicacion);
            txDescripUbicacion = findViewById(R.id.textView_desc_ubicacion);
            txTipoNegocio = findViewById(R.id.textView_tipo_negocio);
            txDescripTipoNegocio = findViewById(R.id.textView_desc_tipo_negocio);
            txCadena = findViewById(R.id.textView_cadena);
            txDescripCadenas = findViewById(R.id.textView_desc_cadena);
            txSoporte = findViewById(R.id.textView_tipo_soporte);
            txDescripcionSoporte = findViewById(R.id.textView_desc_tipo_soporte);

            tvUsuario = findViewById(R.id.tv_usuario);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    //DE LA CREACION DE LAS TABLAS
    public void crearTablas3(){
        try {
            SQLiteDatabase db = conn.getReadableDatabase();
            // 1ro comprueba si existe las tablas
            if (isTableExists("PRODUCTOS") && isTableExists("LOCALES")
                    && isTableExists("UBICACIONES") && isTableExists("TIPO_NEGOCIO") && isTableExists("CADENAS")){//si existe
                cargarTablas();
                //Toast.makeText(getApplicationContext(), "Cargando tablas - Aguarde un momento", Toast.LENGTH_SHORT).show();
            }else {//sino existe crearla
                db.execSQL(Utilidades.CREAR_TABLA_PRODUCTOS);
                db.execSQL(Utilidades.CREAR_TABLA_LOCALES);
                db.execSQL(Utilidades.CREAR_TABLA_UBICACIONES);
                db.execSQL(Utilidades.CREAR_TABLA_TIPO_NEGOCIO);
                db.execSQL(Utilidades.CREAR_TABLA_CADENAS);

                Toast.makeText(getApplicationContext(), "tablas creadas correctamente", Toast.LENGTH_SHORT).show();
                //despues de crear, cargar las tablas
                Toast.makeText(getApplicationContext(), "Cargando tablas - Aguarde un momento", Toast.LENGTH_SHORT).show();
                cargarTablas();
            }
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //DE LA CARGA DE LAS TABLAS
    public void cargarTablas(){
        try {
            SQLiteDatabase db = conn.getReadableDatabase();
            Cursor _locales = db.rawQuery(Utilidades.CONSULTA_TABLA_LOCALES, null);//consulta si existe, luego
            Cursor _ubicaciones = db.rawQuery(Utilidades.CONSULTA_TABLA_UBICACIONES, null);
            Cursor _tipoNegocio = db.rawQuery(Utilidades.CONSULTA_TABLA_TIPO_NEGOCIO, null);
            Cursor _cadenas = db.rawQuery(Utilidades.CONSULTA_TABLA_CADENAS, null);
            if (_locales.getCount() < 1 & _ubicaciones.getCount() < 1 & _tipoNegocio.getCount() < 1 & _cadenas.getCount() < 1)   //2do comprueba si la tabla esta cargada
            {//sino importar datos del csv
                //cargar las tablas directo
                importarCSV_locales();
                importarCSV_ubicaciones();
                importarCSV_tipoNegocio();
                importarCSV_cadenas();
            } else {
                //Toast.makeText(getApplicationContext(), "No se pudo cargar las tablas", Toast.LENGTH_SHORT).show();
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

    //*****************************************************************************************************************




    //LOCALES
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
                    registro.put("CADENA", arreglo[2]);

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
                    //llama el adaptador en QuiebreActivity
                    //adaptador = new AdaptadorLocales(QuiebreActivity.this, listaLocales);
                    //rvLo.setAdapter(adaptador);
                }
                Toast.makeText(ConfiguracionActivity.this, "Tabla Locales cargada", Toast.LENGTH_SHORT).show();
            } catch(Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //UBICACIONES
    public void importarCSV_ubicaciones() {
        File carpeta = new File(Environment.getExternalStorageDirectory() + "/Download");
        String archivoAgenda = carpeta.toString() + "/" + "ubicaciones.csv";
        if(!carpeta.exists()) {
            Toast.makeText(this, "NO EXISTE LA CARPETA", Toast.LENGTH_SHORT).show();
        } else {
            String cadena;
            String[] arreglo;
            try {
                FileReader fileReader = new FileReader(archivoAgenda);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while((cadena = bufferedReader.readLine()) != null) {
                    arreglo = cadena.split(";");
                    SQLiteDatabase db = conn.getWritableDatabase();
                    ContentValues registro = new ContentValues();
                    registro.put("ID_UBICACION", arreglo[0]);
                    registro.put("DESCR_UBICACION", arreglo[1]);
                    listaUbicaciones.add(
                            new Ubicaciones(
                                    arreglo[0],
                                    arreglo[1]
                            )
                    );
                    // los inserto en la base de datos
                    db.insert("UBICACIONES", "1", registro);
                    db.close();
                }
                Toast.makeText(ConfiguracionActivity.this, "Tabla Ubicaciones cargada", Toast.LENGTH_SHORT).show();
            } catch(Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //TIPO DE NEGOCIOS
    public void importarCSV_tipoNegocio() {
        File carpeta = new File(Environment.getExternalStorageDirectory() + "/Download");
        String archivoAgenda = carpeta.toString() + "/" + "tiponegocio.csv";
        if(!carpeta.exists()) {
            Toast.makeText(this, "NO EXISTE LA CARPETA", Toast.LENGTH_SHORT).show();
        } else {
            String cadena;
            String[] arreglo;
            try {
                FileReader fileReader = new FileReader(archivoAgenda);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while((cadena = bufferedReader.readLine()) != null) {
                    arreglo = cadena.split(";");
                    SQLiteDatabase db = conn.getWritableDatabase();
                    ContentValues registro = new ContentValues();
                    registro.put("ID", arreglo[0]);
                    registro.put("DESCRIPCION", arreglo[1]);
                    listaTiponegocio.add(
                            new Tiponegocio(
                                    arreglo[0],
                                    arreglo[1]
                            )
                    );
                    // los inserto en la base de datos
                    db.insert("TIPO_NEGOCIO", "1", registro);
                    db.close();
                }
                Toast.makeText(ConfiguracionActivity.this, "Tabla Tipo de Negocio cargada", Toast.LENGTH_SHORT).show();
            } catch(Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //CADENA
    public void importarCSV_cadenas() {
        File carpeta = new File(Environment.getExternalStorageDirectory() + "/Download");
        String archivoAgenda = carpeta.toString() + "/" + "cadenas.csv";
        if(!carpeta.exists()) {
            Toast.makeText(this, "NO EXISTE LA CARPETA", Toast.LENGTH_SHORT).show();
        } else {
            String cadena;
            String[] arreglo;
            try {
                FileReader fileReader = new FileReader(archivoAgenda);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while((cadena = bufferedReader.readLine()) != null) {
                    arreglo = cadena.split(";");
                    SQLiteDatabase db = conn.getWritableDatabase();
                    ContentValues registro = new ContentValues();
                    registro.put("IDCADENA", arreglo[0]);
                    registro.put("DESCRCADENA", arreglo[1]);
                    registro.put("IDTIPONEGOCIO", arreglo[2]);
                    listaCadena.add(
                            new Cadena(
                                    arreglo[0],
                                    arreglo[1],
                                    arreglo[2]
                            )
                    );
                    // los inserto en la base de datos
                    db.insert("CADENAS", "1", registro);
                    db.close();
                }
                Toast.makeText(ConfiguracionActivity.this, "Tabla cadena cargada", Toast.LENGTH_SHORT).show();
            } catch(Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void pedirPermisos() {
        // PERMISOS PARA ANDROID 6 O SUPERIOR
        if(ContextCompat.checkSelfPermission(ConfiguracionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ConfiguracionActivity.this, new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }
    }

    public void onClickIniciarCarga(View view) {
        Intent miIntent = null;
        switch (view.getId()) {
            case R.id.btn_iniciar_carga:
                String msjTipoNegocio = txDescripTipoNegocio.getText().toString();
                String msjCadena = txDescripCadenas.getText().toString();
                String msjLocal = txDescripcionLocal.getText().toString();
                String msjTipoSoporte = txDescripcionSoporte.getText().toString();
                String msjUsuario = tvUsuario.getText().toString();//clave inventario

                miIntent = new Intent(ConfiguracionActivity.this, QuiebreActivity.class);
                Bundle miBundle = new Bundle();
                miBundle.putString("msjTipoNegocio", txDescripTipoNegocio.getText().toString());
                miBundle.putString("msjCadena", txDescripCadenas.getText().toString());
                miBundle.putString("msjLocal", txDescripcionLocal.getText().toString());
                miBundle.putString("msjTipoSoporte", txDescripcionSoporte.getText().toString());
                miBundle.putString("msjUsuario", tvUsuario.getText().toString());
                miIntent.putExtras(miBundle);
                startActivity(miIntent);//abre la sgte ventana
                break;
        }
    }

    private void recepcionDatosUsuario() {
        //recibe los datos cargado en los campos de Login
        Bundle miBundle = this.getIntent().getExtras();
        if (miBundle != null) {
            String usuario = miBundle.getString("msjUsuario");//recibe
            //consulta
            SQLiteDatabase db = conn.getReadableDatabase();
            Usuarios usuarios = null;
            Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_USUARIO+" WHERE "+Utilidades.CAMPO_ID_USUARIO+" = "+usuario, null);
            if (cursor.moveToNext()) {
                usuarios = new Usuarios();
                usuarios.setId_usuario(cursor.getInt(0));
                usuarios.setNombre_usuario(cursor.getString(1));

                Log.i("id_usuario", usuarios.getId_usuario().toString());
                Log.i("nombre", usuarios.getNombre_usuario());

                tvUsuario.setText(usuarios.getNombre_usuario());//mostrar en la tx
            }
        }
    }


    public void limpiarTablas(String tabla) {
        SQLiteDatabase db = conn.getWritableDatabase();
        conn.borrarRegistros(tabla, db);
        Toast.makeText(ConfiguracionActivity.this, "Se limpio los registros de la "+tabla, Toast.LENGTH_SHORT).show();
    }
    //Comprueba si existe registros en la bd con las configuraciones iniciales, salta en caso de que ya tenga la configuracion ITRACK
    public void compruebaRegistroConfiguracion(){
        try {
            SQLiteDatabase db = conn.getReadableDatabase();
            Cursor _productos = db.rawQuery(Utilidades.CONSULTA_TABLA_PRODUCTOS, null);//consulta si existe, luego
            if (_productos.getCount() > 0)   //2do comprueba si la tabla esta cargada
            {//si ya tiene registros salta a la ventana Quiebres
                Toast.makeText(getApplicationContext(), "Aviso: Finalice la carga para volver a configurar los parametros", Toast.LENGTH_SHORT).show();
                ConfiguracionActivity.this.finish();//finaliza la ventana
            } else {//si no pasa a la ventana ConfiguracionActivity
                //ConfiguracionActivity.this.finish();//finaliza la ventana
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //setea el items del spinner de lo que viene de la configuracion
    public void seleccionItemsSpinner (){
        try {
            int item = 0;
            Bundle miBundle = this.getIntent().getExtras();
            String _item = miBundle.getString("msjtv3");

            item = Integer.parseInt(_item);//convertimos a int
            comboLocales.setSelection(item-1);//selecciona el nro de soporte para mostrarlo en el spinner

        } catch (Exception e){
            //Toast.makeText(getApplicationContext(), "error al obtener spinner", Toast.LENGTH_SHORT).show();
        }
    }


}
