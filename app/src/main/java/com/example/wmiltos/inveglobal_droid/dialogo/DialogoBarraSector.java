package com.example.wmiltos.inveglobal_droid.dialogo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.principal.login.LoginActivity;
import com.example.wmiltos.inveglobal_droid.principal.ventanas.UbicacionActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

public class DialogoBarraSector extends AppCompatDialogFragment {
    private TextView editBarra;
    private DialogoBarraSectorListener listener;
    ConexionSQLiteHelper conn;
    //tv tabla INVENTARIO_SOPORTE
    TextView tv1,tv2,tv3,tv4,tv5,tv6;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialogo, null);
        //datosScanner();

        builder.setView(view)
                .setTitle("Atención..!!")
                /*.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })*/
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        //String barraSector = editBarra.getText().toString();
                        //enviamos el dato barraSector a la clase UbicacionActivity
                        //listener.applyTexts(barraSector);//A1

                    }
                });

        editBarra = view.findViewById(R.id.edit_barra);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogoBarraSectorListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface DialogoBarraSectorListener {
        void applyTexts(String barraSector);//A1
    }


    public void datosScanner() {
        SQLiteDatabase db = conn.getWritableDatabase();
        try {
            String[] campos = new String[] {Utilidades.CAMPO_ID_INVENTARIO_SOPORTE,
                    Utilidades.CAMPO_ID_CLAVE,
                    Utilidades.CAMPO_ID_SOPORTE_IS,
                    Utilidades.CAMPO_NRO_SOPORTE,
                    Utilidades.CAMPO_NRO_TIPO_SOPORTE,
                    Utilidades.CAMPO_DESCRIPCION_IS
            };
            String[] parametro = new String[] {editBarra.getText().toString()};
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

                    //tx a mostrar
                    tv1.setText(columna);
                    tv2.setText(columna1);
                    tv3.setText(columna2);
                    tv4.setText(columna3);
                    tv5.setText(columna4);
                    tv6.setText(columna5);


                } while(c.moveToNext());

            }


        }catch (Exception e){
           // Toast.makeText(getApplicationContext(), "Error al consultar inventario_soporte", Toast.LENGTH_SHORT).show();
        }

    }

}
