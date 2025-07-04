package com.example.miniproyecto_i.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clase7.RecyclerViewHolder
import com.example.miniproyecto_i.databinding.ItemAppointmentBinding
import com.example.miniproyecto_i.model.Appointment

class AppointmentAdapter(
    private val listAppointment:MutableList<Appointment>,
    private val onClick: (Appointment) -> Unit ):
    RecyclerView.Adapter<RecyclerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listAppointment.size
    }

    override fun onBindViewHolder(recyclerViewHolder:RecyclerViewHolder, position: Int) {
        val appointment = listAppointment[position]
        recyclerViewHolder.setItemAppointment(appointment)
        recyclerViewHolder.bindingItem.cvAppointment.setOnClickListener {
            onClick(appointment) // aquí llamas a la función que viene del fragment
        }
    }
}