package com.example.meditrackappv100.activitys

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.meditrackappv100.R
import com.example.meditrackappv100.data.ControlMedicoDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetalleControl : AppCompatActivity() {
    private lateinit var ivAtras: ImageView
    private lateinit var tvFechaRegistro: TextView
    private lateinit var tvHoraRegistro: TextView
    private lateinit var tvNombrePaciente: TextView
    private lateinit var tvEdad: TextView
    private lateinit var tvAltura: TextView
    private lateinit var tvPresionArterial: TextView
    private lateinit var tvComentario: TextView
    private lateinit var tvIMC: TextView
    private lateinit var btnEliminarRegistro: TextView

    private var idConMed: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_control)

        // Asignar Views
        ivAtras = findViewById(R.id.ivAtras)
        tvFechaRegistro = findViewById(R.id.tvFechaRegistro)
        tvHoraRegistro = findViewById(R.id.tvHoraRegistro)
        tvNombrePaciente = findViewById(R.id.tvNombrePaciente)
        tvEdad = findViewById(R.id.tvEdad)
        tvAltura = findViewById(R.id.tvAltura)
        tvPresionArterial = findViewById(R.id.tvPresionArterial)
        tvComentario = findViewById(R.id.tvComentario)
        tvIMC = findViewById(R.id.tvIMC)
        btnEliminarRegistro = findViewById(R.id.btnEliminarRegistro)

        // Obtener el ID del Intent
        idConMed = intent.getIntExtra("id_con_med", -1)
        if (idConMed == -1) {
            Toast.makeText(this, "Error: ID no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        cargarDetalleControl()


        ivAtras.setOnClickListener {
            finish()
        }


        btnEliminarRegistro.setOnClickListener {
            mostrarDialogoEliminar()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    private fun cargarDetalleControl() {
        CoroutineScope(Dispatchers.Main).launch {
            val control = withContext(Dispatchers.IO) {
                val dao = ControlMedicoDAO(this@DetalleControl)
                dao.obtenerPorId(idConMed)
            }

            if (control != null) {

                val partes = control.fecha_registro.split(" ")
                val fecha = partes.getOrNull(0) ?: ""
                val hora = partes.getOrNull(1) ?: ""

                tvFechaRegistro.text = "Fecha Registro: $fecha"
                tvHoraRegistro.text = "Hora Registro: $hora"
                tvNombrePaciente.text = control.nombres_apellidos
                tvEdad.text = control.edad.toString()
                tvAltura.text = "${String.format("%.2f", control.altura)} m"
                tvPresionArterial.text = control.presion_arterial
                tvComentario.text = control.comentario


                val clasificacion = when {
                    control.imc < 18.5 -> "Bajo peso"
                    control.imc < 25.0 -> "Peso normal"
                    control.imc < 30.0 -> "Sobrepeso"
                    else -> "Obesidad"
                }
                tvIMC.text = clasificacion
            } else {
                Toast.makeText(this@DetalleControl, "Registro no encontrado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun mostrarDialogoEliminar() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_eliminar, null)

        view.findViewById<TextView>(R.id.tvTitulo).text = "Eliminar Registro"
        view.findViewById<TextView>(R.id.tvMensaje).text = "Los datos se eliminarán permanentemente"

        val dialog = builder.setView(view).create()


        view.findViewById<TextView>(R.id.btnCancelar).setOnClickListener {
            dialog.dismiss()
        }


        view.findViewById<TextView>(R.id.btnEliminar).setOnClickListener {
            dialog.dismiss()
            eliminarRegistro()
        }

        dialog.show()
    }

    private fun eliminarRegistro() {
        CoroutineScope(Dispatchers.Main).launch {
            val resultado = withContext(Dispatchers.IO) {
                val dao = ControlMedicoDAO(this@DetalleControl)
                dao.eliminar(idConMed)
            }

            if (resultado > 0) {
                Toast.makeText(this@DetalleControl, "Registro eliminado", Toast.LENGTH_SHORT).show()
                finish() // Vuelve a la lista
            } else {
                Toast.makeText(this@DetalleControl, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}