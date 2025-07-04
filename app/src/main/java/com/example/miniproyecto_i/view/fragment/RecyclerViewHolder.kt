package com.example.clase7

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.miniproyecto_i.R
import com.example.miniproyecto_i.databinding.ItemAppointmentBinding
import com.example.miniproyecto_i.model.Appointment

class RecyclerViewHolder(binding: ItemAppointmentBinding): RecyclerView.ViewHolder(binding.root) {
    val bindingItem = binding


    fun setItemAppointment(appointment: Appointment){
        Log.d("imageImg",appointment.photo)
            Glide.with(bindingItem.root.context)
                .load(appointment.photo.replace("\\/", "/"))
                .placeholder(R.drawable.dog_icon)
                .circleCrop()
                .into(bindingItem.petImage)

        bindingItem.appointmentId.text = "#${appointment.id}"
        bindingItem.petName.text = appointment.petName
        bindingItem.description.text = appointment.symptoms

        bindingItem.cvAppointment.setOnClickListener {
            Toast.makeText(it.context,"${appointment.petName}", Toast.LENGTH_SHORT).show()
        }
    }
}