package com.example.miniproyecto_i.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.miniproyecto_i.R
import com.example.miniproyecto_i.databinding.FragmentAddAppointmentBinding
import com.example.miniproyecto_i.model.Appointment
import com.example.miniproyecto_i.viewmodel.AppointmentViewModel

class AddAppointmentFragment : Fragment() {
    private lateinit var binding: FragmentAddAppointmentBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAppointmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        setupToolBar()
        setupButton()
        controladores()
        appointmentViewModel.getListAppointment()

        return binding.root
    }

    private fun setupToolBar() {
        val toolBar = binding.contentToolbar.toolbar
        val toolBarTitle = binding.contentToolbar.toolbarTitle
        toolBarTitle.text = "Nueva Cita"
    }

    private fun setupButton() {
        val addButton = binding.btnSave.baseButton
        addButton.text = "Guardar Cita"
        addButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_save, 0, 0, 0)
    }

    private fun controladores() {
        val addButton = binding.btnSave.baseButton
        addButton.setOnClickListener {
            saveAppointment()
        }
    }

    private fun saveAppointment() {
        val petName = binding.formulary.etPetName.text.toString()
        val petBreed = binding.formulary.actvBreed.text.toString()
        val ownerName = binding.formulary.etOwnerName.text.toString()
        val ownerPhone = binding.formulary.etOwnerPhone.text.toString()

        val appointment = Appointment(
            petName = petName,
            breed = petBreed,
            ownerName = ownerName,
            ownerPhone = ownerPhone
        )

        appointmentViewModel.saveAppointment(appointment)
        Log.d("test", appointment.toString())
        Toast.makeText(context, "Cita guardada !!", Toast.LENGTH_SHORT).show()
    }
}
