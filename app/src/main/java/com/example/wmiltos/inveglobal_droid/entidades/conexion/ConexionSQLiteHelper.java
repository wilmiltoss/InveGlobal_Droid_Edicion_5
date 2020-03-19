package com.example.wmiltos.inveglobal_droid.entidades.conexion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {


    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(Utilidades.CREAR_TABLA_USUARIO);

    }

    //CREA LAS TABLAS POR DEFECTO SOLO EN EL PRIMER INGRESO
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        //db.execSQL(Utilidades.CREAR_TABLA_USUARIO);
        db.execSQL(Utilidades.INSERTAR_TABLA_USUARIO);
        db.execSQL(Utilidades.ELIMINAR_TABLA_USUARIO);

        onCreate(db);
    }


    public void borrarRegistros(String tabla, SQLiteDatabase db) {
        db.execSQL("DELETE FROM "+tabla);
    }

    public void consultaTabla(String tabla, SQLiteDatabase db) {
        db.execSQL("SELECT * FROM "+tabla);

    }

    public void countRegistroTabla(String tabla, String campo, SQLiteDatabase db) {
        db.execSQL("SELECT count("+campo+") FROM "+tabla);

    }




}
