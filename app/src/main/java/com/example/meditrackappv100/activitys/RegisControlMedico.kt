package com.example.meditrackappv100.activitys

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.meditrackappv100.R
import com.example.meditrackappv100.data.ControlMedicoDAO
import com.example.meditrackappv100.entity.ControlMedico
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class RegisControlMedico : AppCompatActivity() {
    private lateinit var tietNombresApellidos: TextInputEditText
    private lateinit var tietEdad: TextInputEditText
    private lateinit var tietPeso: TextInputEditText
    private lateinit var tietAltura: TextInputEditText
    private lateinit var tietPresionArterial: TextInputEditText
    private lateinit var tietComentario: TextInputEditText
    private lateinit var tilNombresApellidos: TextInputLayout
    private lateinit var tilEdad: TextInputLayout
    private lateinit var tilPeso: TextInputLayout
    private lateinit var tilAltura: TextInputLayout
    private lateinit var tilPresionArterial: TextInputLayout
    private lateinit var tilComentario: TextInputLayout
    private lateinit var btnRegistrar: TextView
    private lateinit var ivAtras: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regis_control_medico)

        tietNombresApellidos = findViewById(R.id.tietNombresApellidos)
        tietEdad = findViewById(R.id.tietEdad)
        tietPeso = findViewById(R.id.tietPeso)
        tietAltura = findViewById(R.id.tietAltura)
        tietPresionArterial = findViewById(R.id.tietPresionArterial)
        tietComentario = findViewById(R.id.tietComentario)
        tilNombresApellidos = findViewById(R.id.tilNombresApellidos)
        tilEdad = findViewById(R.id.tilEdad)
        tilPeso = findViewById(R.id.tilPeso)
        tilAltura = findViewById(R.id.tilAltura)
        tilPresionArterial = findViewById(R.id.tilPresionArterial)
        tilComentario = findViewById(R.id.tilComentario)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        ivAtras = findViewById(R.id.ivAtras)


        btnRegistrar.setOnClickListener {
            validarCampos()
        }

        ivAtras.setOnClickListener {
            finish()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                maxOf(systemBars.bottom, imeInsets.bottom)
            )
            insets
        }
    }

    private fun validarCampos() {
        val nombresApellidos = tietNombresApellidos.text.toString().trim()
        val edadStr = tietEdad.text.toString().trim()
        val pesoStr = tietPeso.text.toString().trim()
        val alturaStr = tietAltura.text.toString().trim()
        val presionArterial = tietPresionArterial.text.toString().trim()
        val comentario = tietComentario.text.toString().trim()

        var error = false

        if (nombresApellidos.isEmpty()) {
            tilNombresApellidos.error = "Ingrese nombres y apellidos"
            error = true
        } else {
            tilNombresApellidos.error = ""
        }

        if (edadStr.isEmpty()) {
            tilEdad.error = "Ingrese edad"
            error = true
        } else {
            tilEdad.error = ""
        }

        if (pesoStr.isEmpty()) {
            tilPeso.error = "Ingrese peso"
            error = true
        } else {
            tilPeso.error = ""
        }

        if (alturaStr.isEmpty()) {
            tilAltura.error = "Ingrese altura"
            error = true
        } else {
            tilAltura.error = ""
        }

        if (presionArterial.isEmpty()) {
            tilPresionArterial.error = "Ingrese presión arterial"
            error = true
        } else {
            tilPresionArterial.error = ""
        }

        if (error) return

        val edad = edadStr.toInt()
        val peso = pesoStr.toDouble()
        val altura = alturaStr.toDouble()

        // Calcular IMC
        val imc = peso / (altura * altura)


        val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "PE")).apply {
            timeZone = TimeZone.getTimeZone("America/Lima")
        }.format(Date())


        val control = ControlMedico(
            id_con_med = 0,
            nombres_apellidos = nombresApellidos,
            edad = edad,
            peso = peso,
            altura = altura,
            presion_arterial = presionArterial,
            comentario = comentario,
            imc = imc,
            fecha_registro = fechaActual
        )

        // Insertar BD
        CoroutineScope(Dispatchers.Main).launch {
            val resultado = withContext(Dispatchers.IO) {
                val dao = ControlMedicoDAO(this@RegisControlMedico)
                dao.insertar(control)
            }

            if (resultado > 0) {
                mostrarDialogoConfirmacion(
                    titulo = "Registro correcto",
                    mensaje = "Su registro de control médico se realizó correctamente.",
                    textoBoton = "Entendido"
                )
            } else {
                Toast.makeText(this@RegisControlMedico, "Error al registrar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Dialog
    private fun mostrarDialogoConfirmacion(titulo: String, mensaje: String, textoBoton: String) {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_registro_correcto, null)

        view.findViewById<TextView>(R.id.tvTitulo).text = titulo
        view.findViewById<TextView>(R.id.tvMensaje).text = mensaje
        view.findViewById<TextView>(R.id.btnAceptar).text = textoBoton

        val dialog = builder.setView(view).create()

        view.findViewById<TextView>(R.id.btnAceptar).setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }
}