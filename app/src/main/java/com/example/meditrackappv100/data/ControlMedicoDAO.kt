package com.example.meditrackappv100.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.meditrackappv100.entity.ControlMedico

class ControlMedicoDAO(context: Context) {
    private val dbHelper = AppDatabaseHelper(context)


    fun insertar(control: ControlMedico): Long {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("nombres_apellidos", control.nombres_apellidos)
            put("edad", control.edad)
            put("peso", control.peso)
            put("altura", control.altura)
            put("presion_arterial", control.presion_arterial)
            put("comentario", control.comentario)
            put("imc", control.imc)
            put("fecha_registro", control.fecha_registro)
        }
        return db.insert("control_medico", null, valores)
    }



    fun obtenerTodos(): List<ControlMedico> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<ControlMedico>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM control_medico ORDER BY fecha_registro DESC", null)

        while (cursor.moveToNext()) {
            lista.add(
                ControlMedico(
                    id_con_med = cursor.getInt(cursor.getColumnIndexOrThrow("id_con_med")),
                    nombres_apellidos = cursor.getString(cursor.getColumnIndexOrThrow("nombres_apellidos")),
                    edad = cursor.getInt(cursor.getColumnIndexOrThrow("edad")),
                    peso = cursor.getDouble(cursor.getColumnIndexOrThrow("peso")),
                    altura = cursor.getDouble(cursor.getColumnIndexOrThrow("altura")),
                    presion_arterial = cursor.getString(cursor.getColumnIndexOrThrow("presion_arterial")),
                    comentario = cursor.getString(cursor.getColumnIndexOrThrow("comentario")),
                    imc = cursor.getDouble(cursor.getColumnIndexOrThrow("imc")),
                    fecha_registro = cursor.getString(cursor.getColumnIndexOrThrow("fecha_registro"))
                )
            )
        }
        cursor.close()
        db.close()
        return lista
    }



    fun obtenerPorId(id: Int): ControlMedico? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM control_medico WHERE id_con_med = ?",
            arrayOf(id.toString())
        )

        var control: ControlMedico? = null
        if (cursor.moveToFirst()) {
            control = ControlMedico(
                id_con_med = cursor.getInt(cursor.getColumnIndexOrThrow("id_con_med")),
                nombres_apellidos = cursor.getString(cursor.getColumnIndexOrThrow("nombres_apellidos")),
                edad = cursor.getInt(cursor.getColumnIndexOrThrow("edad")),
                peso = cursor.getDouble(cursor.getColumnIndexOrThrow("peso")),
                altura = cursor.getDouble(cursor.getColumnIndexOrThrow("altura")),
                presion_arterial = cursor.getString(cursor.getColumnIndexOrThrow("presion_arterial")),
                comentario = cursor.getString(cursor.getColumnIndexOrThrow("comentario")),
                imc = cursor.getDouble(cursor.getColumnIndexOrThrow("imc")),
                fecha_registro = cursor.getString(cursor.getColumnIndexOrThrow("fecha_registro"))
            )
        }
        cursor.close()
        db.close()
        return control
    }


    fun eliminar(id: Int): Int {
        val db = dbHelper.writableDatabase
        val resultado = db.delete("control_medico", "id_con_med = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }
}