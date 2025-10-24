package com.example.meditrackappv100.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "meditrack.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE control_medico(
                id_con_med INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                nombres_apellidos TEXT,
                edad INTEGER,
                peso REAL,
                altura REAL,
                presion_arterial TEXT,
                comentario TEXT,
                imc REAL,
                fecha_registro TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS control_medico")
        onCreate(db)
    }
}