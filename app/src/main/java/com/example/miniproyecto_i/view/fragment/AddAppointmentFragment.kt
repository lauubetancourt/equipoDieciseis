package com.example.miniproyecto_i.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.miniproyecto_i.R
import com.example.miniproyecto_i.databinding.FragmentAddAppointmentBinding
import com.example.miniproyecto_i.model.Appointment
import com.example.miniproyecto_i.model.BreedsModelResponse
import com.example.miniproyecto_i.viewmodel.AppointmentViewModel

class AddAppointmentFragment : Fragment() {
    private lateinit var binding: FragmentAddAppointmentBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()
    private var breedsList: List<String> = emptyList()
    private var isSaving = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAppointmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        setupToolBar()
        setupAutocompleteTextBreeds()
        setupButton()
        setupSpinner()
        controladores()
        goBack()

        return binding.root
    }

    private fun setupToolBar() {
        binding.contentToolbar.toolbarTitle.text = "Nueva Cita"
    }

    private fun setupAutocompleteTextBreeds() {
        appointmentViewModel.getListBreeds()
        appointmentViewModel.listBreeds.observe(viewLifecycleOwner) { response ->
            breedsList = parseListBreeds(response)

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                breedsList
            )
            binding.formulary.actvBreed.setAdapter(adapter)
        }
    }

    private fun parseListBreeds(response: BreedsModelResponse): List<String> {
        val breedsList = mutableListOf<String>()
        response.breeds.forEach { (breed, subBreeds) ->
            if (subBreeds.isEmpty()) {
                breedsList.add(breed)
            } else {
                subBreeds.forEach { subBreed ->
                    breedsList.add("$subBreed $breed")
                }
            }
        }
        return breedsList
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

    private fun goBack() {
        binding.contentToolbar.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_addAppointmentFragment_to_homeAppointments)
        }
    }

    private fun controladores() {
        setupFormValidation()
        binding.btnSave.baseButton.setOnClickListener {
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

        appointmentViewModel.getBreedPhotoByName(petBreed)
        appointmentViewModel.breedPhoto.observe(viewLifecycleOwner) { response ->
            val photoUrl = response.photo
            val appointment = Appointment(
                petName = petName,
                breed = petBreed,
                ownerName = ownerName,
                ownerPhone = ownerPhone,
                symptoms = symptoms,
                photo = photoUrl
            )

            isSaving = true
            appointmentViewModel.saveAppointment(appointment)
            observeStatus()
        }
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

        editTexts.forEach { it.addTextChangedListener(watcher) }

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

        val isBreedValid = validateBreed(petBreed)

        val allFieldsFilled = petName.isNotEmpty() &&
                petBreed.isNotEmpty() &&
                isBreedValid &&
                ownerName.isNotEmpty() &&
                ownerPhone.isNotEmpty()

        binding.btnSave.baseButton.isEnabled = allFieldsFilled
    }

    private fun validateBreed(breed: String): Boolean {
        return breedsList.any { it.equals(breed, ignoreCase = true) }
    }

    private fun observeStatus() {
        appointmentViewModel.progresState.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSave.baseButton.isEnabled = !isLoading

            if (!isLoading && isSaving) {
                isSaving = false
                Toast.makeText(requireContext(), "Cita guardada exitosamente", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addAppointmentFragment_to_homeAppointments)
            }
        }
    }
}
