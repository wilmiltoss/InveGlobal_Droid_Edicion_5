package com.example.wmiltos.inveglobal_droid.iTrack.utils_csv;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Productos;
import com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV_Objetos.Product;
import com.example.wmiltos.inveglobal_droid.iTrack.utils_csv.ObjetoList;
import com.example.wmiltos.inveglobal_droid.principal.login.LoginActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.util.ArrayList;

public class CapturaListViewActivity extends AppCompatActivity {

    private ListView listView;
    private ListAdapter adapter, adapter2;
    private int lastMarkedPosition = 0;
    ConexionSQLiteHelper conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura_list_view);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "InveStock.sqlite", null, 1);

        listView = findViewById(R.id.listView);

        /* Populate the ListView */
        final ArrayList<ObjetoList> datos  = new ArrayList<>();
       // final ArrayList<Product> datos2  = new ArrayList<>();
        //datos.add(new ObjetoList("2", "COLIBRÍ", "Los troquilinos","5"));
        datos.add(new ObjetoList("2", "COLIBRÍ", "Los troquilinos"));
        datos.add(new ObjetoList("3", "PORC", "Los ratones locos"));
        datos.add(new ObjetoList("4", "BROASCA", "BROASCA es el nombre"));
        datos.add(new ObjetoList("5", "LEOAICA", "Los troquilinos"));
        datos.add(new ObjetoList("6", "SORICEL", "Los ratones locos"));
        datos.add(new ObjetoList("7", "PANTERA", "Búho es el nombre"));
        datos.add(new ObjetoList("8", "PUMA", "Los troquilinos"));
        datos.add(new ObjetoList("9", "CABALLO", "Los ratones locos"));
        datos.add(new ObjetoList("10", "JIRAFA", "Búho es el nombre"));
        datos.add(new ObjetoList("11", "CUINE", "Los troquilinos"));
        datos.add(new ObjetoList("12", "PASARICA", "Los ratones locos"));
        datos.add(new ObjetoList("12.5", "CERDO", "Búho es el nombre"));
        datos.add(new ObjetoList("14", "VEVERITA", "Los troquilinos"));
        datos.add(new ObjetoList("15", "FOX", "Los ratones locos"));

        //captura los datos
        //adapter = new AdaptadorLista(this,datos);
        listView.setAdapter(adapter);

       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_row, cargar());//myListado =cambio manual del tamaño list
       // listView.setAdapter(adapter);

        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.mylistado, cargar());//myListado =cambio manual del tamaño list
            listView.setAdapter(adapter);
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "no hay registros", Toast.LENGTH_LONG).show();
        }



        //adapter2 = new AdaptadorLista(this, datos);
        //listView.setAdapter(adapter);


        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_row, cargar());//myListado =cambio manual del tamaño list
        //listView.setAdapter(adapter);
      /*  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, MainActivity2.class);

                intent.putExtra("nombre", datos.get(position).getTexto());
                intent.putExtra("descripcion", datos.get(position).getDescripcion());
                startActivity(intent);

                //listView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();

               // ((TextView)parent.getItemAtPosition(lastMarkedPosition)).setText("");
               //  ((TextView)(view.findViewById(R.id.descripcion))).setText("Usted salió de aquí!");


                lastMarkedPosition = position;
            }
        });*/


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(CapturaListViewActivity.this, LoginActivity.class);
                startActivity(intent);

       //En esta parte me manda al otro activity donde modifico la informacion en sqlite del elemento seleccionado

            }
        });

    }


    //cargar la vista preeliminar del listview
    public String []cargar() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = null;

        cursor = db.rawQuery("SELECT " + Utilidades.CAMPO_ID + "," +
                Utilidades.CAMPO_CODIGO_BARRA + "," +
                Utilidades.CAMPO_DESCRIP + "," +
                Utilidades.CAMPO_CANT_PROD
                + " FROM " + Utilidades.TABLA_PRODUCTOS, null);
        String[] listado = new String[cursor.getCount()];//arreglo string que trae el listado
        int post = 0;
        if (cursor.moveToFirst()) {//si tenemo al menos 1 reg lo recorremos
            do {
                String id = cursor.getString(0);
                String codigo = cursor.getString(1);
                String Descripcion = cursor.getString(2);
                String Cantidad = cursor.getString(3);
                listado[post] = id + " -" + codigo + "-" + Descripcion + "  " + Cantidad;
                post++;

            } while (cursor.moveToNext());//mientras se mueve al sgte registro
            if (cursor.getCount() <= 0) {
                cursor.close();
                Toast.makeText(getApplicationContext(), "No se registró ningun articulo", Toast.LENGTH_LONG).show();
            }
        }
        return listado;
    }


}
