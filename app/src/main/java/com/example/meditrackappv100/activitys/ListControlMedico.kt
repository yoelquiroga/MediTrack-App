package com.example.meditrackappv100.activitys

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meditrackappv100.R
import com.example.meditrackappv100.adapter.ControlMedicoAdapter
import com.example.meditrackappv100.data.ControlMedicoDAO
import com.example.meditrackappv100.entity.ControlMedico
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListControlMedico : AppCompatActivity() {
    private lateinit var rvControles: RecyclerView
    private lateinit var tvAgregar: TextView
    private lateinit var controlMedicoAdapter: ControlMedicoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_control_medico)


        rvControles = findViewById(R.id.rvControles)
        tvAgregar = findViewById(R.id.tvAgregar)


        rvControles.layoutManager = LinearLayoutManager(this)
        controlMedicoAdapter = ControlMedicoAdapter(emptyList()) { control ->
            val intent = Intent(this, DetalleControl::class.java)
            intent.putExtra("id_con_med", control.id_con_med)
            startActivity(intent)
        }
        rvControles.adapter = controlMedicoAdapter


        tvAgregar.setOnClickListener {
            val intent = Intent(this, RegisControlMedico::class.java)
            startActivity(intent)
        }


        cargarListaControles()


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

    private fun cargarListaControles() {
        CoroutineScope(Dispatchers.Main).launch {
            val lista = withContext(Dispatchers.IO) {
                val dao = ControlMedicoDAO(this@ListControlMedico)
                dao.obtenerTodos()
            }

            if (lista.isEmpty()) {
                Toast.makeText(this@ListControlMedico, "No hay registros", Toast.LENGTH_SHORT).show()
            }

            controlMedicoAdapter.listaControles = lista
            controlMedicoAdapter.notifyDataSetChanged()
        }
    }


    override fun onResume() {
        super.onResume()
        cargarListaControles()
    }


}