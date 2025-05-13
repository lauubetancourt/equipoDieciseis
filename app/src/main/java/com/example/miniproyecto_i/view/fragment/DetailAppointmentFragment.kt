package com.example.miniproyecto_i.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.miniproyecto_i.R
import com.example.miniproyecto_i.databinding.FragmentDetailAppointmentBinding

import com.example.miniproyecto_i.viewmodel.AppointmentViewModel

class DetailAppointmentFragment : Fragment(){
    private lateinit var binding: FragmentDetailAppointmentBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = FragmentDetailAppointmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        setupToolBar()
        observeAppointmentData()
        return binding.root
    }
    private fun setupToolBar() {
        val toolBar = binding.contentToolbar
        val toolBarTitle = toolBar.toolbarTitle
        appointmentViewModel.appointment.observe(viewLifecycleOwner) { appointment ->
            if (appointment != null) {
                toolBarTitle.text = appointment.petName
            } else {
                toolBarTitle.text = "Detalles de la Cita"
            }
        }
    }
    private fun observeAppointmentData() {
        appointmentViewModel.appointment.observe(viewLifecycleOwner) { appointment ->
            if (appointment != null) {

                binding.tvBreed.text = appointment.breed
                binding.tvOwner.text = "Propietario: ${appointment.ownerName}"
                binding.tvPhone.text = "Teléfono: ${appointment.ownerPhone}"
                binding.tvNotes.text = "Sin notas"
                if (appointment.photo != null) {
                    Glide.with(this)
                        .load(appointment.photo) // Add an error drawable
                        .centerCrop()
                        .into(binding.ivPetImage)
                }
            } else {

                binding.tvBreed.text = "No disponible"
                binding.tvOwner.text = "Propietario: No disponible"
                binding.tvPhone.text = "Teléfono: No disponible"
                binding.tvNotes.text = "No disponible"
            }
        }
    }
}