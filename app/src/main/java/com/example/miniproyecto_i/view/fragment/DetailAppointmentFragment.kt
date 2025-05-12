package com.example.miniproyecto_i.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
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
                // Nota: petName no se usa en el layout actual; considera añadir un TextView para petName
                // binding.petNameTextView.text = appointment.petName
                binding.tvBreed.text = appointment.breed
                binding.tvOwner.text = "Propietario: ${appointment.ownerName}"
                binding.tvPhone.text = "Teléfono: ${appointment.ownerPhone}"
                // tvNotes no está en la entidad Appointment; usa un valor por defecto o añade el campo
                binding.tvNotes.text = "Sin notas" // Opcional: personaliza según necesites
                // ivPetImage no está en la entidad; usa un placeholder o carga una imagen
                // Por ejemplo: Glide.with(this).load("url_de_imagen").into(binding.ivPetImage)
            } else {
                // Manejo de caso cuando no hay cita
                // binding.petNameTextView.text = "No disponible"
                binding.tvBreed.text = "No disponible"
                binding.tvOwner.text = "Propietario: No disponible"
                binding.tvPhone.text = "Teléfono: No disponible"
                binding.tvNotes.text = "No disponible"
                // Opcional: establecer una imagen por defecto
                // binding.ivPetImage.setImageResource(R.drawable.placeholder_image)
            }
        }
    }
}