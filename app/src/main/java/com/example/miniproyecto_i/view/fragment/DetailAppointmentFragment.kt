package com.example.miniproyecto_i.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.AlertDialog
import androidx.core.content.ContextCompat
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
        binding.tvPetId.text = "#${receivedAppointment.id}"
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
            showDeleteConfirmationDialog()
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
    private fun showDeleteConfirmationDialog() {
        context?.let { ctx ->
            AlertDialog.Builder(ctx)
                .setTitle("⚠️ Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar la cita de ${receivedAppointment.petName}?\n\nEsta acción no se puede deshacer.")
                .setIcon(R.drawable.ic_delete)
                .setPositiveButton("Eliminar") { dialog, _ ->
                    deleteInventory()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(false)
                .create()
                .apply {
                    show()
                    getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
                        ContextCompat.getColor(ctx, android.R.color.holo_red_dark)
                    )
                    getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
                        ContextCompat.getColor(ctx, android.R.color.darker_gray)
                    )
                }
        }
    }

    private fun goBack() {
        val backButton = binding.contentToolbar.backButton
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_detailAppointmentFragment_to_homeAppointments)
        }
    }

}