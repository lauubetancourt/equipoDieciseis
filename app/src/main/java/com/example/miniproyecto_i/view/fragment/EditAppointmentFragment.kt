package com.example.miniproyecto_i.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.miniproyecto_i.R
import com.example.miniproyecto_i.databinding.FragmentEditAppointmentBinding
import com.example.miniproyecto_i.model.Appointment
import com.example.miniproyecto_i.model.BreedsModelResponse
import com.example.miniproyecto_i.viewmodel.AppointmentViewModel

class EditAppointmentFragment : Fragment() {
    private lateinit var binding: FragmentEditAppointmentBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()
    private lateinit var receivedAppointment: Appointment
    private var breedsList: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditAppointmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controllers()
        dataAppointment()
        setupToolBar()
        setupAutocompleteTextBreeds()
        setupButton()
        setupSymptomsSpinner()
    }

    private fun controllers() {
        validateData()
        binding.editButton.baseButton.setOnClickListener {
            updateAppointment()
        }
        binding.contentToolbar.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_appointmentEditFragment_to_appointmentDetailsFragment)
        }
    }

    private fun dataAppointment() {
        val receivedBundle = arguments
        receivedAppointment = receivedBundle?.getSerializable("dataAppointment") as Appointment
        binding.formulary.etPetName.setText(receivedAppointment.petName)
        binding.formulary.actvBreed.setText(receivedAppointment.breed)
        binding.formulary.etOwnerName.setText(receivedAppointment.ownerName)
        binding.formulary.etOwnerPhone.setText(receivedAppointment.ownerPhone)
    }

    private fun updateAppointment(){
        val petName = binding.formulary.etPetName.text.toString()
        val breed = binding.formulary.actvBreed.text.toString()
        val ownerName = binding.formulary.etOwnerName.text.toString()
        val ownerPhone = binding.formulary.etOwnerPhone.text.toString()
        val appointment = Appointment(receivedAppointment.id, petName,breed,ownerName,ownerPhone,receivedAppointment.symptoms,receivedAppointment.photo)
        appointmentViewModel.updateAppointment(appointment)
        findNavController().navigate(R.id.action_appointmentEditFragment_to_appointmentDetailsFragment)
    }

    private fun setupToolBar() {
        val toolBar = binding.contentToolbar
        toolBar.toolbarTitle.text = "Editar Cita"
    }

    private fun setupAutocompleteTextBreeds() {
        appointmentViewModel.getListBreeds()
        appointmentViewModel.listBreeds.observe(viewLifecycleOwner) { response ->
            breedsList = parseListBreeds(response)

            val adapter: ArrayAdapter<String> = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                breedsList
            )
            binding.formulary.actvBreed.setAdapter(adapter)
        }
    }

    private fun setupButton() {
        val editButton = binding.editButton.baseButton
        editButton.text = "Editar Cita"
        editButton.isEnabled = false
        editButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pencil, 0,0,0)
    }

    private fun validateData() {
        // Get data from formulary
        val formulary = binding.formulary
        val listEditText = listOf(formulary.etPetName, formulary.actvBreed, formulary.etOwnerName, formulary.etOwnerPhone)

        for (editText in listEditText) {
            editText.addTextChangedListener {
                val isListFull = listEditText.all {
                    it.text.isNotEmpty() // if all list is not empty
                }
                // Check if given breed is in the list
                val isBreedValid = validateBreed(formulary.actvBreed.text.toString())
                // Activate button if all fields are filled and given breed is valid
                binding.editButton.baseButton.isEnabled = isListFull && isBreedValid
            }
        }
    }

    private fun setupSymptomsSpinner() {
        binding.formulary.spinnerSymptoms.isEnabled = false
    }

    // since the breeds are in a JSON form and not List of strings, then parse it
    private fun parseListBreeds(response: BreedsModelResponse): List<String> {
        val breedsList = mutableListOf<String>()

        response.breeds.forEach { (breed, subBreeds) ->
            if (subBreeds.isEmpty()) {          // There are no subbreeds, just add the breed
                breedsList.add(breed)
        } else {                                // There are subbreeds, add as subbreed + breed
                subBreeds.forEach { subBreed ->
                    breedsList.add("$subBreed $breed")
                }
            }
        }
        return breedsList
    }

    // validate a given string is a breed from the breeds list
    private fun validateBreed(breed: String): Boolean {
        return breedsList.any { it.lowercase() == breed.lowercase() }
    }
}