package com.example.miniproyecto_i.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.miniproyecto_i.R
import com.example.miniproyecto_i.databinding.FragmentAddAppointmentBinding

class AddAppointmentFragment : Fragment() {
    private lateinit var binding: FragmentAddAppointmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddAppointmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        setupToolBar()
        setupButton()
        return binding.root
    }

    private fun setupToolBar() {
        val toolBar = binding.contentToolbar.toolbar
        val toolBarTitle = binding.contentToolbar.toolbarTitle
        toolBarTitle.text = "Nueva Cita"
    }

    private fun setupButton() {
        val addButton = binding.addButton.baseButton
        addButton.text = "Guardar Cita"
        addButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_save, 0,0,0)
    }
}