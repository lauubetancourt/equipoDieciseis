package com.example.miniproyecto_i.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import com.example.miniproyecto_i.R
import com.example.miniproyecto_i.databinding.FragmentEditAppointmentBinding

class EditAppointmentFragment : Fragment() {
    private lateinit var binding: FragmentEditAppointmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditAppointmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        setupToolBar()
        setupAutocompleteTextBreeds()
        setupButton()
        controllers()
        return binding.root
    }

    private fun controllers() {
        validateData()
        binding.editButton.baseButton.setOnClickListener {
            // Función editar datos
        }
    }

    private fun setupToolBar() {
        val toolBar = binding.contentToolbar.toolbar
        val toolBarTitle = binding.contentToolbar.toolbarTitle
        toolBarTitle.text = "Editar Cita"
    }

    private fun setupAutocompleteTextBreeds() {
        val breedsList: List<String> = listOf("Pug", "Pastor", "Puddle")
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            breedsList
        )

        binding.formulary.actvBreed.setAdapter(adapter)
    }

    private fun setupButton() {
        val editButton = binding.editButton.baseButton
        editButton.text = "Editar Cita"
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

    private fun validateBreed(breed: String): Boolean {
        val breedsList: List<String> = listOf("Pug", "Pastor", "Puddle")
        return breedsList.any { it.lowercase() == breed.lowercase() }
    }
}