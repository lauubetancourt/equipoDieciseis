package com.example.miniproyecto_i.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
        setupSpinner()
        controladores()

        return binding.root
    }

    private fun setupToolBar() {
        val toolBarTitle = binding.contentToolbar.toolbarTitle
        toolBarTitle.text = "Nueva Cita"
    }


    private fun setupButton() {
        val addButton = binding.btnSave.baseButton
        addButton.text = "Guardar Cita"
        addButton.isEnabled = false
        addButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_save, 0, 0, 0)
    }

    private fun setupSpinner() {
        val spinner = binding.formulary.spinnerSymptoms
        val symptoms = listOf(
            "Síntomas",
            "Solo duerme",
            "No come",
            "Fractura extremidad",
            "Tiene pulgas",
            "Tiene garrapatas",
            "Bota demasiado pelo"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            symptoms
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }


    private fun controladores() {
        setupFormValidation()
        val addButton = binding.btnSave.baseButton
        addButton.setOnClickListener {
            val selectedSymptom = binding.formulary.spinnerSymptoms.selectedItem.toString()

            if (selectedSymptom == "Síntomas") {
                Toast.makeText(requireContext(), "Selecciona un síntoma", Toast.LENGTH_SHORT).show()
            } else {
                saveAppointment()
            }
        }
    }

    private fun saveAppointment() {
        val petName = binding.formulary.etPetName.text.toString()
        val petBreed = binding.formulary.actvBreed.text.toString()
        val ownerName = binding.formulary.etOwnerName.text.toString()
        val ownerPhone = binding.formulary.etOwnerPhone.text.toString()
        val symptoms = binding.formulary.spinnerSymptoms.selectedItem.toString()

        val appointment = Appointment(
            petName = petName,
            breed = petBreed,
            ownerName = ownerName,
            ownerPhone = ownerPhone,
            symptoms = symptoms,
            photo = "https:\\/\\/images.dog.ceo\\/breeds\\/poodle-standard\\/n02113799_3356.jpg\""
        )

        appointmentViewModel.saveAppointment(appointment)
        Toast.makeText(context, "Cita guardada", Toast.LENGTH_SHORT).show()
    }

    private fun setupFormValidation() {
        val editTexts = listOf(
            binding.formulary.etPetName,
            binding.formulary.actvBreed,
            binding.formulary.etOwnerName,
            binding.formulary.etOwnerPhone
        )

        val spinner = binding.formulary.spinnerSymptoms

        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateForm()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        for (et in editTexts) {
            et.addTextChangedListener(watcher)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                validateForm()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun validateForm() {
        val petName = binding.formulary.etPetName.text.toString().trim()
        val petBreed = binding.formulary.actvBreed.text.toString().trim()
        val ownerName = binding.formulary.etOwnerName.text.toString().trim()
        val ownerPhone = binding.formulary.etOwnerPhone.text.toString().trim()

        val allFieldsFilled = petName.isNotEmpty() &&
                petBreed.isNotEmpty() &&
                ownerName.isNotEmpty() &&
                ownerPhone.isNotEmpty()

       binding.btnSave.baseButton.isEnabled = allFieldsFilled
    }
}
