package com.example.meditrackappv100.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meditrackappv100.R
import com.example.meditrackappv100.entity.ControlMedico

class ControlMedicoAdapter(
    var listaControles: List<ControlMedico>,
    private val onItemClickListener: (ControlMedico) -> Unit
) : RecyclerView.Adapter<ControlMedicoAdapter.ControlMedicoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ControlMedicoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_control_medico, parent, false)
        return ControlMedicoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ControlMedicoViewHolder, position: Int) {
        val control = listaControles[position]
        holder.bind(control)
    }

    override fun getItemCount(): Int = listaControles.size

    inner class ControlMedicoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvFechaRegistro: TextView = itemView.findViewById(R.id.tvFechaRegistro)
        private val tvHoraRegistro: TextView = itemView.findViewById(R.id.tvHoraRegistro)
        private val tvNombrePaciente: TextView = itemView.findViewById(R.id.tvNombrePaciente)
        private val tvEdad: TextView = itemView.findViewById(R.id.tvEdad)
        private val tvAltura: TextView = itemView.findViewById(R.id.tvAltura)
        private val tvPresionArterial: TextView = itemView.findViewById(R.id.tvPresionArterial)
        private val tvComentario: TextView = itemView.findViewById(R.id.tvComentario)

        fun bind(control: ControlMedico) {
            val fechaYHora = control.fecha_registro.split(" ")
            val fecha = fechaYHora[0]
            val hora = fechaYHora[1]

            tvFechaRegistro.text = "$fecha"
            tvHoraRegistro.text = "$hora"
            tvNombrePaciente.text = control.nombres_apellidos
            tvEdad.text = control.edad.toString()
            tvAltura.text = "${String.format("%.2f", control.altura)} m"
            tvPresionArterial.text = control.presion_arterial
            tvComentario.text = control.comentario


            itemView.setOnClickListener {
                onItemClickListener(control)
            }
        }
    }
}