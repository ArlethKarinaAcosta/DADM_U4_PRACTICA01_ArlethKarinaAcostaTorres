package com.example.arlet.dadm_u4_practica01_arlethkarinaacostatorres;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {
    public BaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE RECETA(ID INTEGER PRIMARY KEY NOT NULL, NOMBRE VARCHAR(200), INGREDIENTES VARCHAR(200), PREPARACION VARCHAR(1000), OBSERVACIONES VARCHAR(500))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}