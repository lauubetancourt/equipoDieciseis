package com.example.miniproyecto_i.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.miniproyecto_i.R
import com.example.miniproyecto_i.databinding.FragmentDetailAppointmentBinding
import com.example.miniproyecto_i.model.Appointment

import com.example.miniproyecto_i.viewmodel.AppointmentViewModel

class DetailAppointmentFragment : Fragment(){
    private lateinit var binding: FragmentDetailAppointmentBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()
    private lateinit var receivedAppointment: Appointment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = FragmentDetailAppointmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        receivedAppointment = arguments?.getSerializable("dataAppointment") as Appointment
        setupToolBar()
        observeAppointmentData()
        controladores()
        goBack()
        return binding.root
    }
    private fun setupToolBar() {
        val toolBar = binding.contentToolbar
        val toolBarTitle = toolBar.toolbarTitle
        toolBarTitle.text = receivedAppointment.petName
    }
    private fun observeAppointmentData() {
        binding.tvBreed.text = receivedAppointment.breed
        binding.tvOwner.text = "Propietario: ${receivedAppointment.ownerName}"
        binding.tvPhone.text = "Teléfono: ${receivedAppointment.ownerPhone}"
        binding.tvNotes.text = receivedAppointment.symptoms

        receivedAppointment.photo?.let { url ->
            Glide.with(this)
                .load(url)
                .centerCrop()
                .into(binding.ivPetImage)
        }
    }
    private fun controladores() {
        binding.btnDelete.setOnClickListener {
            deleteInventory()
        }

        binding.btnEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("dataAppointment", receivedAppointment)
            findNavController().navigate(R.id.action_itemDetailsFragment_to_itemEditFragment, bundle)
        }
    }
    private fun deleteInventory(){
        appointmentViewModel.deleteAppointment(receivedAppointment)
        appointmentViewModel.getListAppointment()
        findNavController().navigate(R.id.action_detailAppointmentFragment_to_homeAppointments)
    }

    private fun goBack() {
        val backButton = binding.contentToolbar.backButton
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_detailAppointmentFragment_to_homeAppointments)
        }
    }

}